package upeu.edu.pe.autoandes.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.autoandes.Entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
