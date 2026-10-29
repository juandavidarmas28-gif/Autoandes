package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.UsuarioResponseDTO;
import upeu.edu.pe.autoandes.Repository.UsuarioRepository;
import upeu.edu.pe.autoandes.Security.PermisosPorRol;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(),
                        usuario.getNombreCompleto(), usuario.getActivo(),
                        PermisosPorRol.comoNombres(usuario.getRoles())))
                .toList();
    }
}
