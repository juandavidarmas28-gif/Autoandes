package upeu.edu.pe.autoandes.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upeu.edu.pe.autoandes.Dto.ClienteResponseDTO;
import upeu.edu.pe.autoandes.Service.ClienteService;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return service.listar();
    }
}
