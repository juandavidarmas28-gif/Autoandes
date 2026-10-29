package upeu.edu.pe.autoandes.Dto;

import java.math.BigDecimal;

public record ServicioResponseDTO(Long id, String codigo, String nombre,
        BigDecimal precio, Integer duracionMin, Boolean activo) {
}
