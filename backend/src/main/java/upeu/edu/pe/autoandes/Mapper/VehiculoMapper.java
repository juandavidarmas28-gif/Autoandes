package upeu.edu.pe.autoandes.Mapper;

import org.springframework.stereotype.Component;
import upeu.edu.pe.autoandes.Dto.VehiculoRequestDTO;
import upeu.edu.pe.autoandes.Dto.VehiculoResponseDTO;
import upeu.edu.pe.autoandes.Entity.Cliente;
import upeu.edu.pe.autoandes.Entity.Vehiculo;

@Component
public class VehiculoMapper {
    public Vehiculo toEntity(VehiculoRequestDTO request, Cliente cliente) {
        Vehiculo vehiculo = new Vehiculo();
        updateEntity(vehiculo, request, cliente);
        return vehiculo;
    }

    public void updateEntity(Vehiculo vehiculo, VehiculoRequestDTO request, Cliente cliente) {
        vehiculo.setPlaca(request.placa());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setAnio(request.anio());
        vehiculo.setCliente(cliente);
    }

    public VehiculoResponseDTO toResponse(Vehiculo vehiculo) {
        return new VehiculoResponseDTO(vehiculo.getId(), vehiculo.getPlaca(), vehiculo.getMarca(),
                vehiculo.getModelo(), vehiculo.getAnio(), vehiculo.getCliente().getId(),
                vehiculo.getCliente().getNombres());
    }
}
