package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.VehiculoRequestDTO;
import upeu.edu.pe.autoandes.Dto.VehiculoResponseDTO;
import upeu.edu.pe.autoandes.Entity.Cliente;
import upeu.edu.pe.autoandes.Entity.Vehiculo;
import upeu.edu.pe.autoandes.Exception.ReglaNegocioException;
import upeu.edu.pe.autoandes.Exception.RecursoNoEncontradoException;
import upeu.edu.pe.autoandes.Mapper.VehiculoMapper;
import upeu.edu.pe.autoandes.Repository.ClienteRepository;
import upeu.edu.pe.autoandes.Repository.VehiculoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculoService {
    private final VehiculoRepository repository;
    private final ClienteRepository clienteRepository;
    private final VehiculoMapper mapper;

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listar(Long clienteId) {
        if (clienteId == null) {
            return repository.findAllByOrderByIdAsc().stream().map(mapper::toResponse).toList();
        }
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("No existe el cliente " + clienteId);
        }
        return repository.findAllByClienteIdOrderByIdAsc(clienteId).stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional
    public VehiculoResponseDTO crear(VehiculoRequestDTO request) {
        validarPlacaUnica(request.placa(), null);
        Cliente cliente = buscarCliente(request.clienteId());
        Vehiculo guardado = repository.save(mapper.toEntity(request, cliente));
        log.info("Vehículo creado: {}", guardado.getPlaca());
        return mapper.toResponse(guardado);
    }

    @Transactional
    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO request) {
        Vehiculo vehiculo = buscar(id);
        validarPlacaUnica(request.placa(), id);
        Cliente cliente = buscarCliente(request.clienteId());
        mapper.updateEntity(vehiculo, request, cliente);
        Vehiculo guardado = repository.save(vehiculo);
        log.info("Vehículo actualizado: {}", guardado.getPlaca());
        return mapper.toResponse(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = buscar(id);
        try {
            repository.delete(vehiculo);
            repository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ReglaNegocioException("No se puede eliminar un vehículo con órdenes registradas");
        }
        log.info("Vehículo eliminado: {}", vehiculo.getPlaca());
    }

    private Vehiculo buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo " + id));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente " + id));
    }

    private void validarPlacaUnica(String placa, Long idActual) {
        boolean duplicada = repository.existsByPlacaIgnoreCase(placa)
                && (idActual == null || !repository.findById(idActual).orElseThrow().getPlaca().equalsIgnoreCase(placa));
        if (duplicada) {
            throw new ReglaNegocioException("La placa ya está registrada");
        }
    }
}
