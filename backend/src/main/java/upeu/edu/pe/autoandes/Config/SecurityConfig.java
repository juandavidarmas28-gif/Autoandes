package upeu.edu.pe.autoandes.Config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import upeu.edu.pe.autoandes.Security.RestAccessDeniedHandler;
import upeu.edu.pe.autoandes.Security.RestAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {
    @Bean
    SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 bytes");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return NimbusJwtEncoder.withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtFilter,
            RestAuthenticationEntryPoint entryPoint,
            RestAccessDeniedHandler deniedHandler) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(errors -> errors.authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/auth/login", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/clientes").hasAuthority("VEHICULO_LEER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/servicios", "/api/v1/servicios/**").hasAuthority("SERVICIO_LEER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/servicios").hasAuthority("SERVICIO_ESCRIBIR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/servicios/**").hasAuthority("SERVICIO_ESCRIBIR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/servicios/**").hasAuthority("SERVICIO_ESCRIBIR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/servicios/**").hasAuthority("SERVICIO_ESCRIBIR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/vehiculos", "/api/v1/vehiculos/**").hasAuthority("VEHICULO_LEER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/vehiculos").hasAuthority("VEHICULO_ESCRIBIR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/vehiculos/**").hasAuthority("VEHICULO_ESCRIBIR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vehiculos/**").hasAuthority("VEHICULO_ESCRIBIR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/ordenes").hasAuthority("ORDEN_REGISTRAR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/ordenes", "/api/v1/ordenes/**").hasAuthority("ORDEN_LEER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/ordenes/*/anular").hasAuthority("ORDEN_ANULAR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reportes/**").hasAuthority("REPORTE_VER")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
