package upeu.edu.pe.autoandes.Dto;

import java.math.BigDecimal;

public record ReporteIngresoResponseDTO(String codigo, String servicio,
        Long cantidadTotal, BigDecimal importeTotal) {
}
