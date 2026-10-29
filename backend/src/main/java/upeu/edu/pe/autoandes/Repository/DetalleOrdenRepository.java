package upeu.edu.pe.autoandes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.autoandes.Entity.DetalleOrden;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    List<DetalleOrden> findAllByOrdenIdOrderByIdAsc(Long ordenId);
}
