package upeu.edu.pe.autoandes.Config;

import java.time.Instant;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    public String generar(String username, String nombreCompleto,
            Set<String> roles, Set<String> permisos) {
        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("autoandes-api")
                .subject(username)
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(expirationSeconds))
                .claim("nombreCompleto", nombreCompleto)
                .claim("roles", roles)
                .claim("permisos", permisos)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
