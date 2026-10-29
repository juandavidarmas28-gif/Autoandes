package upeu.edu.pe.autoandes.Dto;

import java.util.Set;

public record UsuarioResponseDTO(Long id, String username, String nombreCompleto,
        Boolean activo, Set<String> roles) {
}
