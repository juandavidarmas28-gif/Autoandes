package upeu.edu.pe.autoandes.Dto;

import java.util.Set;

public record RolResponseDTO(Long id, String nombre, Set<String> permisos) {
}
