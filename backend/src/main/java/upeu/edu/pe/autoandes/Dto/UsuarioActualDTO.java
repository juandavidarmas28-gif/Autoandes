package upeu.edu.pe.autoandes.Dto;

import java.util.Set;

public record UsuarioActualDTO(Long id, String username, String nombreCompleto, Set<String> roles, Set<String> permisos) {
}
