package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.ClienteResponseDTO;
import upeu.edu.pe.autoandes.Repository.ClienteRepository;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return repository.findAll().stream()
                .map(cliente -> new ClienteResponseDTO(cliente.getId(), cliente.getDocumento(),
                        cliente.getNombres(), cliente.getTelefono()))
                .toList();
    }
}
