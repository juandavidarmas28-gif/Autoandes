package upeu.edu.pe.autoandes.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.AnularOrdenRequestDTO;
import upeu.edu.pe.autoandes.Dto.DetalleOrdenRequestDTO;
import upeu.edu.pe.autoandes.Dto.OrdenRequestDTO;
import upeu.edu.pe.autoandes.Dto.OrdenResponseDTO;
import upeu.edu.pe.autoandes.Dto.ReporteIngresoResponseDTO;
import upeu.edu.pe.autoandes.Entity.DetalleOrden;
import upeu.edu.pe.autoandes.Entity.OrdenServicio;
import upeu.edu.pe.autoandes.Entity.Servicio;
import upeu.edu.pe.autoandes.Entity.Vehiculo;
import upeu.edu.pe.autoandes.Enums.EstadoOrden;
import upeu.edu.pe.autoandes.Exception.ReglaNegocioException;
import upeu.edu.pe.autoandes.Exception.RecursoNoEncontradoException;
import upeu.edu.pe.autoandes.Exception.SolicitudInvalidaException;
import upeu.edu.pe.autoandes.Mapper.OrdenMapper;
import upeu.edu.pe.autoandes.Repository.OrdenServiceRepository;
import upeu.edu.pe.autoandes.Repository.ServicioRepository;
import upeu.edu.pe.autoandes.Repository.VehiculoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdenServicioService {
    private static final EstadoOrden REGISTRADA = EstadoOrden.REGISTRADA;
    private static final EstadoOrden ANULADA = EstadoOrden.ANULADA;

    private final OrdenServiceRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenMapper mapper;

    @Transactional
    public OrdenResponseDTO registrar(OrdenRequestDTO request, String username) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo " + request.vehiculoId()));

        var ultimaOrden = ordenRepository.findTopByVehiculoIdAndEstadoOrderByFechaDescIdDesc(
                vehiculo.getId(), REGISTRADA);
        if (ultimaOrden.isPresent() && request.kilometraje() < ultimaOrden.get().getKilometraje()) {
            throw new ReglaNegocioException("El kilometraje no puede ser menor que el último registrado: "
                    + ultimaOrden.get().getKilometraje());
        }

        Set<Long> serviciosIncluidos = new HashSet<>();
        for (int i = 0; i < request.detalles().size(); i++) {
            DetalleOrdenRequestDTO detalle = request.detalles().get(i);
            if (!serviciosIncluidos.add(detalle.servicioId())) {
                throw new SolicitudInvalidaException("No se puede repetir un servicio en la orden",
                        Map.of("detalles[" + i + "].servicioId", "El servicio ya fue agregado"));
            }
        }

        OrdenServicio orden = new OrdenServicio();
        orden.setFecha(LocalDateTime.now());
        orden.setVehiculo(vehiculo);
        orden.setKilometraje(request.kilometraje());
        orden.setEstado(REGISTRADA);
        orden.setUsuarioRegistro(username);

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleOrdenRequestDTO detalleRequest : request.detalles()) {
            Servicio servicio = servicioRepository.findById(detalleRequest.servicioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el servicio " + detalleRequest.servicioId()));
            if (!Boolean.TRUE.equals(servicio.getActivo())) {
                throw new ReglaNegocioException("El servicio " + servicio.getCodigo() + " está inactivo");
            }
            BigDecimal subtotal = servicio.getPrecio().multiply(BigDecimal.valueOf(detalleRequest.cantidad()));
            DetalleOrden detalle = new DetalleOrden();
            detalle.setServicio(servicio);
            detalle.setCantidad(detalleRequest.cantidad());
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setSubtotal(subtotal);
            orden.agregarDetalle(detalle);
            total = total.add(subtotal);
        }
        orden.setTotal(total);
        orden.setNumero("OS-%06d".formatted(ordenRepository.siguienteNumero()));

        OrdenServicio guardada = ordenRepository.save(orden);
        log.info("Orden {} registrada por {}", guardada.getNumero(), username);
        return mapper.toResponse(ordenRepository.buscarCompletaPorId(guardada.getId()).orElse(guardada));
    }

    @Transactional(readOnly = true)
    public List<OrdenResponseDTO> buscar(String placa, String estado, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new SolicitudInvalidaException("Rango de fechas inválido",
                    Map.of("desde", "La fecha inicial no puede ser posterior a la fecha final"));
        }
        String filtroPlaca = placa == null || placa.isBlank() ? null : placa.trim();
        EstadoOrden filtroEstado = null;
        if (estado != null && !estado.isBlank()) {
            try {
                filtroEstado = EstadoOrden.valueOf(estado.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new SolicitudInvalidaException("Estado de orden inválido",
                        Map.of("estado", "Usa REGISTRADA o ANULADA"));
            }
        }
        LocalDateTime inicio = desde == null ? null : desde.atStartOfDay();
        LocalDateTime finExclusivo = hasta == null ? null : hasta.plusDays(1).atStartOfDay();
        return ordenRepository.buscarConFiltros(filtroPlaca, filtroEstado, inicio, finExclusivo)
                .stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrdenResponseDTO obtener(Long id) {
        OrdenServicio orden = ordenRepository.buscarCompletaPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden " + id));
        return mapper.toResponse(orden);
    }

    @Transactional
    public OrdenResponseDTO anular(Long id, AnularOrdenRequestDTO request) {
        OrdenServicio orden = ordenRepository.buscarCompletaPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden " + id));
        if (!REGISTRADA.equals(orden.getEstado())) {
            throw new ReglaNegocioException("Solo se puede anular una orden REGISTRADA");
        }
        if (!orden.getFecha().toLocalDate().equals(LocalDate.now())) {
            throw new ReglaNegocioException("La orden solo se puede anular el mismo día de su registro");
        }
        orden.setEstado(ANULADA);
        orden.setMotivoAnulacion(request.motivo().trim());
        log.info("Orden {} anulada", orden.getNumero());
        return mapper.toResponse(orden);
    }

    @Transactional(readOnly = true)
    public List<ReporteIngresoResponseDTO> reporte(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new SolicitudInvalidaException("El rango de fechas es obligatorio y debe ser válido",
                    Map.of("desde", "Indica un rango donde desde no sea posterior a hasta"));
        }
        return ordenRepository.reporteIngresos(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }
}
