package com.example.trabajofinal.trabajofinal.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtVerification {

    private static final Logger logger = LoggerFactory.getLogger(JwtVerification.class);

    // Considera externalizar esta clave a un archivo de configuración o variable de entorno
    // para mayor seguridad en producción.
    private final String secretString = "O9s7KdW13a7ls9F8M0q4NhTRy6b8xWjk";
    private SecretKey secretKey;

    // Duración de la expiración del token. Usa TimeUnit para mayor claridad.
    private final long expirationMillis = TimeUnit.MINUTES.toMillis(15); // 15 minutos por defecto

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para confirmación de email.
     *
     * @param email Email del usuario
     * @return Token JWT firmado y con expiración
     * @throws IllegalArgumentException si el email es nulo o vacío
     */
    public String generateToken(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío para generar el token.");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(email) // Usa .subject()
                .issuedAt(now)  // Usa .issuedAt()
                .expiration(expiryDate) // Usa .expiration()
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Valida un token y devuelve el email si es válido y no ha expirado.
     * Captura excepciones específicas para un manejo de errores más preciso.
     *
     * @param token El token recibido por email
     * @return Email si el token es válido, null si no
     */
    public String validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Se intentó validar un token nulo o vacío.");
            return null;
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (SignatureException ex) {
            logger.error("Firma JWT inválida: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT malformado: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.warn("Token JWT expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT no soportado: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("Argumento ilegal en el token JWT: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error inesperado al validar el token JWT: {}", ex.getMessage(), ex);
        }
        return null;
    }
}