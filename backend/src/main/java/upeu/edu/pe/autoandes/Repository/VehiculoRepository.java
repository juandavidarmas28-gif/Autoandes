package upeu.edu.pe.autoandes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.autoandes.Entity.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByPlacaIgnoreCase(String placa);

    @EntityGraph(attributePaths = "cliente")
    List<Vehiculo> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "cliente")
    List<Vehiculo> findAllByClienteIdOrderByIdAsc(Long clienteId);
}
