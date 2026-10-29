package upeu.edu.pe.autoandes.Dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(LocalDateTime timestamp, int status, String error,
        String mensaje, String ruta, Map<String, String> campos) {
}
