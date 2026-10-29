package upeu.edu.pe.autoandes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.autoandes.Entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
