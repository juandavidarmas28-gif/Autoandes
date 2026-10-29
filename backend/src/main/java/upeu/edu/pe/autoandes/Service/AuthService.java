package upeu.edu.pe.autoandes.Service;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.autoandes.Dto.LoginRequestDTO;
import upeu.edu.pe.autoandes.Dto.LoginResponseDTO;
import upeu.edu.pe.autoandes.Dto.UsuarioActualDTO;
import upeu.edu.pe.autoandes.Entity.Usuario;
import upeu.edu.pe.autoandes.Exception.AutenticacionException;
import upeu.edu.pe.autoandes.Config.JwtService;
import upeu.edu.pe.autoandes.Repository.UsuarioRepository;
import upeu.edu.pe.autoandes.Security.PermisosPorRol;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(request.username())
                .orElseThrow(AutenticacionException::new);
        if (!Boolean.TRUE.equals(usuario.getActivo())
                || !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new AutenticacionException();
        }
        return crearRespuesta(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioActualDTO usuarioActual(String username) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(AutenticacionException::new);
        Set<String> roles = PermisosPorRol.comoNombres(usuario.getRoles());
        Set<String> permisos = PermisosPorRol.obtener(usuario.getRoles()).stream()
                .map(Enum::name).collect(Collectors.toUnmodifiableSet());
        return new UsuarioActualDTO(usuario.getId(), usuario.getUsername(), usuario.getNombreCompleto(), roles, permisos);
    }

    private LoginResponseDTO crearRespuesta(Usuario usuario) {
        Set<String> roles = PermisosPorRol.comoNombres(usuario.getRoles());
        Set<String> permisos = PermisosPorRol.obtener(usuario.getRoles()).stream()
                .map(Enum::name).collect(Collectors.toUnmodifiableSet());
        String token = jwtService.generar(usuario.getUsername(), usuario.getNombreCompleto(), roles, permisos);
        return new LoginResponseDTO(token, "Bearer", jwtService.getExpirationSeconds(),
                usuario.getNombreCompleto(), roles, permisos);
    }
}
