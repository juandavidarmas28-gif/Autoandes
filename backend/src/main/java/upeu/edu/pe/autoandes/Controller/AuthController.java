package upeu.edu.pe.autoandes.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upeu.edu.pe.autoandes.Dto.LoginRequestDTO;
import upeu.edu.pe.autoandes.Dto.LoginResponseDTO;
import upeu.edu.pe.autoandes.Dto.UsuarioActualDTO;
import upeu.edu.pe.autoandes.Service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public UsuarioActualDTO usuarioActual(Authentication authentication) {
        return authService.usuarioActual(authentication.getName());
    }
}
