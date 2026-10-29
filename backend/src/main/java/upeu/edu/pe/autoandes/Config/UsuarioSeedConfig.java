package upeu.edu.pe.autoandes.Config;

import java.util.HashSet;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import upeu.edu.pe.autoandes.Entity.Rol;
import upeu.edu.pe.autoandes.Entity.Usuario;
import upeu.edu.pe.autoandes.Repository.RolRepository;
import upeu.edu.pe.autoandes.Repository.UsuarioRepository;

@Configuration
@RequiredArgsConstructor
public class UsuarioSeedConfig {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedUsuarios() {
        return args -> inicializar();
    }

    void inicializar() {
        Rol admin = obtenerOCrearRol("ADMIN");
        Rol asesor = obtenerOCrearRol("ASESOR");
        Rol jefe = obtenerOCrearRol("JEFE_TALLER");
        crearUsuarioSiFalta("admin", "Administrador AutoAndes", true, admin);
        crearUsuarioSiFalta("asesor", "Asesor AutoAndes", true, asesor);
        crearUsuarioSiFalta("jefe", "Jefe de Taller AutoAndes", true, jefe);
        crearUsuarioSiFalta("inactivo", "Asesor Inactivo", false, asesor);
    }

    private Rol obtenerOCrearRol(String nombre) {
        return rolRepository.findByNombre(nombre).orElseGet(() -> rolRepository.save(new Rol(nombre)));
    }

    private void crearUsuarioSiFalta(String username, String nombreCompleto,
            boolean activo, Rol rol) {
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) return;
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode("Auto2026*"));
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setActivo(activo);
        usuario.setRoles(new HashSet<>(List.of(rol)));
        usuarioRepository.save(usuario);
    }
}
