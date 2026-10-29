package upeu.edu.pe.autoandes.Controller;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import upeu.edu.pe.autoandes.Dto.ReporteIngresoResponseDTO;
import upeu.edu.pe.autoandes.Service.OrdenServicioService;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final OrdenServicioService service;

    @GetMapping("/ingresos-por-servicio")
    public List<ReporteIngresoResponseDTO> ingresosPorServicio(
            @RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return service.reporte(desde, hasta);
    }
}
