package upeu.edu.pe.autoandes.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import upeu.edu.pe.autoandes.Enums.EstadoOrden;

public record OrdenResponseDTO(Long id, String numero, LocalDateTime fecha,
        Long vehiculoId, String placa, String clienteNombre, Integer kilometraje, EstadoOrden estado,
        BigDecimal total, String usuarioRegistro, String motivoAnulacion,
        List<DetalleOrdenResponseDTO> detalles) {
}
