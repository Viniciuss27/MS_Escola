package vinix.config;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import vinix.entities.User;

@Component
public class JwtUtil {

	// Chave secreta para assinar e validar os tokens
	private final SecretKey jwtSecretKey;

	// Tempo de expiração
	@Value("${security.jwt.expiration}")
	private Long expiration;

	public JwtUtil(SecretKey jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}

	// Gera um token JWT assinado com os dados do usuário autenticado
	public String generateToken(User user) {
		return Jwts.builder()
				// "subject" do token: identifica o usuário dono do token
				.subject(user.getEmail())

				// claim com o id do usuário, evita ir ao banco de novo
				.claim("userId", user.getId())

				// claim com os nomes das roles do usuário, usada pra checar autorização por
				// rota
				.claim("roles", user.getRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toList()))

				// data/hora em que o token foi emitido
				.issuedAt(new Date())

				// data/hora de expiração do token (agora + tempo configurado no yml)
				.expiration(new Date(System.currentTimeMillis() + expiration))

				// assina o token com a chave secreta
				.signWith(jwtSecretKey)

				// gera a string final compactada do token
				.compact();
	}

	// Lê e valida um token JWT, retornando as claims (dados) contidas nele
	public Claims extractClaims(String token) {
		return Jwts.parser().verifyWith(jwtSecretKey).build().parseSignedClaims(token).getPayload();
	}

	// Atalho para extrair só o email (subject) do token
	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}
}