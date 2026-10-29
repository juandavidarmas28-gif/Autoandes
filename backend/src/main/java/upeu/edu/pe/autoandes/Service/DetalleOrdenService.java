package upeu.edu.pe.autoandes.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.DetalleOrdenResponseDTO;
import upeu.edu.pe.autoandes.Exception.RecursoNoEncontradoException;
import upeu.edu.pe.autoandes.Repository.OrdenServiceRepository;

@Service
@RequiredArgsConstructor
public class DetalleOrdenService {
    private final OrdenServiceRepository ordenRepository;

    @Transactional(readOnly = true)
    public List<DetalleOrdenResponseDTO> listarPorOrden(Long ordenId) {
        var orden = ordenRepository.buscarCompletaPorId(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden " + ordenId));
        return orden.getDetalles().stream()
                .map(detalle -> new DetalleOrdenResponseDTO(detalle.getId(),
                        detalle.getServicio().getId(), detalle.getServicio().getCodigo(),
                        detalle.getServicio().getNombre(), detalle.getCantidad(),
                        detalle.getPrecioUnitario(), detalle.getSubtotal()))
                .toList();
    }
}
