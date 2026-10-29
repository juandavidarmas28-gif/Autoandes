package upeu.edu.pe.autoandes.Controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upeu.edu.pe.autoandes.Dto.EstadoServicioRequestDTO;
import upeu.edu.pe.autoandes.Dto.ServicioRequestDTO;
import upeu.edu.pe.autoandes.Dto.ServicioResponseDTO;
import upeu.edu.pe.autoandes.Service.ServicioService;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService service;

    @GetMapping
    public List<ServicioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ServicioResponseDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ServicioResponseDTO actualizar(@PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public ServicioResponseDTO cambiarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoServicioRequestDTO request) {
        return service.cambiarEstado(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
