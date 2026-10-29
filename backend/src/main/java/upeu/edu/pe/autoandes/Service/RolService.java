package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.RolResponseDTO;
import upeu.edu.pe.autoandes.Repository.RolRepository;
import upeu.edu.pe.autoandes.Security.PermisosPorRol;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponseDTO> listar() {
        return rolRepository.findAll().stream()
                .map(rol -> new RolResponseDTO(rol.getId(), rol.getNombre(),
                        PermisosPorRol.obtener(java.util.Set.of(rol)).stream().map(Enum::name)
                                .collect(java.util.stream.Collectors.toUnmodifiableSet())))
                .toList();
    }
}
