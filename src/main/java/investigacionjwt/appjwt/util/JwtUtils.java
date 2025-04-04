package investigacionjwt.appjwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j // Anotación para habilitar un logger (registro de logs) en esta clase.
public class JwtUtils {
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String ISSUER = "server";
    private JwtUtils() {
    }
    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent(); // Llama a parseToken y verifica si devuelve un objeto presente.
    }
    private static Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey) // Configura el parser para usar la clave secreta.
                .build();
        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT Exception occurred: " + e.getMessage());
        }
        return Optional.empty();
    }
    public static Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken); // Obtiene los Claims del token.
        return claimsOptional.map(Claims::getSubject); // Extrae el subject (nombre de usuario) si los Claims están
    }
    public static String generateToken(String username) {
        var currentDate = new Date();
        var jwtExpirationInMinutes = 10;
        var expiration = DateUtils.addMinutes(currentDate, jwtExpirationInMinutes);
        return Jwts.builder()
                .id(UUID.randomUUID().toString()) // Asigna un identificador único al token.
                .issuer(ISSUER) // Establece el emisor del token.
                .subject(username) // Establece el nombre de usuario como subject del token.
                .signWith(secretKey) // Firma el token con la clave secreta.
                .issuedAt(currentDate) // Establece la fecha en que el token fue emitido.
                .expiration(expiration) // Establece la fecha de expiración del token.
                .compact(); // Genera el token en formato string compacto.
    }
}
