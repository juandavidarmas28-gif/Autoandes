package upeu.edu.pe.autoandes.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.autoandes.Entity.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    List<Servicio> findAllByOrderByIdAsc();
}
