package upeu.edu.pe.autoandes.Dto;

import java.util.Set;

public record LoginResponseDTO(String token, String tipo, long expiraEnSegundos,
        String nombreCompleto, Set<String> roles, Set<String> permisos) {
}
