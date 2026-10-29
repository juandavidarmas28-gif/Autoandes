package upeu.edu.pe.autoandes.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upeu.edu.pe.autoandes.Dto.ReporteIngresoResponseDTO;
import upeu.edu.pe.autoandes.Enums.EstadoOrden;
import upeu.edu.pe.autoandes.Entity.OrdenServicio;

public interface OrdenServiceRepository extends JpaRepository<OrdenServicio, Long> {
    @Query(value = "SELECT seq_orden_numero.NEXTVAL FROM dual", nativeQuery = true)
    Long siguienteNumero();

    @EntityGraph(attributePaths = {"vehiculo", "detalles", "detalles.servicio"})
    @Query("select distinct o from OrdenServicio o where o.id = :id")
    Optional<OrdenServicio> buscarCompletaPorId(@Param("id") Long id);

    @EntityGraph(attributePaths = {"vehiculo", "detalles", "detalles.servicio"})
    @Query("""
            select distinct o from OrdenServicio o join o.vehiculo v
            where (:placa is null or upper(v.placa) = upper(:placa))
              and (:estado is null or o.estado = :estado)
              and (:desde is null or o.fecha >= :desde)
              and (:hastaExclusiva is null or o.fecha < :hastaExclusiva)
            order by o.fecha desc
            """)
    List<OrdenServicio> buscarConFiltros(@Param("placa") String placa,
            @Param("estado") EstadoOrden estado, @Param("desde") LocalDateTime desde,
            @Param("hastaExclusiva") LocalDateTime hastaExclusiva);

    Optional<OrdenServicio> findTopByVehiculoIdAndEstadoOrderByFechaDescIdDesc(Long vehiculoId, EstadoOrden estado);

    @Query("""
            select new upeu.edu.pe.autoandes.Dto.ReporteIngresoResponseDTO(
                s.codigo, s.nombre, sum(d.cantidad), sum(d.subtotal))
            from DetalleOrden d join d.orden o join d.servicio s
            where o.estado <> 'ANULADA' and o.fecha >= :desde and o.fecha < :hastaExclusiva
            group by s.codigo, s.nombre
            order by sum(d.subtotal) desc
            """)
    List<ReporteIngresoResponseDTO> reporteIngresos(@Param("desde") LocalDateTime desde,
            @Param("hastaExclusiva") LocalDateTime hastaExclusiva);
}
