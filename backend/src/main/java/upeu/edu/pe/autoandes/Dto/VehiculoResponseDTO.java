package upeu.edu.pe.autoandes.Dto;

public record VehiculoResponseDTO(Long id, String placa, String marca,
        String modelo, Integer anio, Long clienteId, String clienteNombre) {
}
