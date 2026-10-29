package upeu.edu.pe.autoandes.Mapper;

import java.util.Comparator;

import org.springframework.stereotype.Component;
import upeu.edu.pe.autoandes.Dto.DetalleOrdenResponseDTO;
import upeu.edu.pe.autoandes.Dto.OrdenResponseDTO;
import upeu.edu.pe.autoandes.Entity.OrdenServicio;

@Component
public class OrdenMapper {
    public OrdenResponseDTO toResponse(OrdenServicio orden) {
        var detalles = orden.getDetalles().stream()
                .sorted(Comparator.comparing(detalle -> detalle.getId() == null ? Long.MAX_VALUE : detalle.getId()))
                .map(detalle -> new DetalleOrdenResponseDTO(detalle.getId(),
                        detalle.getServicio().getId(), detalle.getServicio().getCodigo(),
                        detalle.getServicio().getNombre(), detalle.getCantidad(),
                        detalle.getPrecioUnitario(), detalle.getSubtotal()))
                .toList();
        return new OrdenResponseDTO(orden.getId(), orden.getNumero(), orden.getFecha(),
                orden.getVehiculo().getId(), orden.getVehiculo().getPlaca(), orden.getVehiculo().getCliente().getNombres(),
                orden.getKilometraje(), orden.getEstado(), orden.getTotal(),
                orden.getUsuarioRegistro(), orden.getMotivoAnulacion(), detalles);
    }
}
