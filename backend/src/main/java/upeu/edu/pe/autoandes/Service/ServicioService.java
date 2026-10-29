package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.EstadoServicioRequestDTO;
import upeu.edu.pe.autoandes.Dto.ServicioRequestDTO;
import upeu.edu.pe.autoandes.Dto.ServicioResponseDTO;
import upeu.edu.pe.autoandes.Entity.Servicio;
import upeu.edu.pe.autoandes.Exception.ReglaNegocioException;
import upeu.edu.pe.autoandes.Exception.RecursoNoEncontradoException;
import upeu.edu.pe.autoandes.Mapper.ServicioMapper;
import upeu.edu.pe.autoandes.Repository.ServicioRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServicioService {
    private final ServicioRepository repository;
    private final ServicioMapper mapper;

    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listar() {
        return repository.findAllByOrderByIdAsc().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponseDTO obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional
    public ServicioResponseDTO crear(ServicioRequestDTO request) {
        validarCodigoUnico(request.codigo(), null);
        Servicio guardado = repository.save(mapper.toEntity(request));
        log.info("Servicio creado: {}", guardado.getCodigo());
        return mapper.toResponse(guardado);
    }

    @Transactional
    public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO request) {
        Servicio servicio = buscar(id);
        validarCodigoUnico(request.codigo(), id);
        mapper.updateEntity(servicio, request);
        Servicio guardado = repository.save(servicio);
        log.info("Servicio actualizado: {}", guardado.getCodigo());
        return mapper.toResponse(guardado);
    }

    @Transactional
    public ServicioResponseDTO cambiarEstado(Long id, EstadoServicioRequestDTO request) {
        Servicio servicio = buscar(id);
        servicio.setActivo(request.activo());
        Servicio guardado = repository.save(servicio);
        log.info("Estado actualizado para servicio {}: activo={}", guardado.getCodigo(), guardado.getActivo());
        return mapper.toResponse(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = buscar(id);
        try {
            repository.delete(servicio);
            repository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ReglaNegocioException("No se puede eliminar un servicio usado en una orden");
        }
        log.info("Servicio eliminado: {}", servicio.getCodigo());
    }

    private Servicio buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el servicio " + id));
    }

    private void validarCodigoUnico(String codigo, Long idActual) {
        boolean duplicado = repository.existsByCodigoIgnoreCase(codigo)
                && (idActual == null || !repository.findById(idActual).orElseThrow().getCodigo().equalsIgnoreCase(codigo));
        if (duplicado) {
            throw new ReglaNegocioException("El código de servicio ya está registrado");
        }
    }
}
