package upeu.edu.pe.autoandes.Mapper;

import org.springframework.stereotype.Component;
import upeu.edu.pe.autoandes.Dto.ServicioRequestDTO;
import upeu.edu.pe.autoandes.Dto.ServicioResponseDTO;
import upeu.edu.pe.autoandes.Entity.Servicio;

@Component
public class ServicioMapper {
    public Servicio toEntity(ServicioRequestDTO request) {
        Servicio servicio = new Servicio();
        servicio.setCodigo(request.codigo());
        servicio.setNombre(request.nombre());
        servicio.setPrecio(request.precio());
        servicio.setDuracionMin(request.duracionMin());
        return servicio;
    }

    public void updateEntity(Servicio servicio, ServicioRequestDTO request) {
        servicio.setCodigo(request.codigo());
        servicio.setNombre(request.nombre());
        servicio.setPrecio(request.precio());
        servicio.setDuracionMin(request.duracionMin());
    }

    public ServicioResponseDTO toResponse(Servicio servicio) {
        return new ServicioResponseDTO(servicio.getId(), servicio.getCodigo(),
                servicio.getNombre(), servicio.getPrecio(), servicio.getDuracionMin(), servicio.getActivo());
    }
}
