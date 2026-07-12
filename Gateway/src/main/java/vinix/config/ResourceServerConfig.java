package vinix.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    // mesma chave usada pelo ms-auth para assinar os tokens
    @Autowired
    private SecretKey secretKey;

    // valida a assinatura do token localmente, sem chamar o ms-auth a cada requisição
    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }

    // Converte a claim customizada "roles" em GrantedAuthority.
    @Bean
    Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> rolesConverter = jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }
            // as roles já vêm com o prefixo "ROLE_" (ex: "ROLE_ADMIN"),
            // não precisa do JwtGrantedAuthoritiesConverter padrão
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        };

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(rolesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(exchange -> exchange
                // login e criação de usuário (via Feign) não exigem token
                .pathMatchers("/auth/**").permitAll()

                .pathMatchers(org.springframework.http.HttpMethod.POST, "/professores/**")
                .hasAuthority("ROLE_ADMIN") // professor, somente adm 

                .pathMatchers(org.springframework.http.HttpMethod.POST, "/alunos/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESSOR") // alunos, todos permitidos 

                .pathMatchers("/notas/**")// notas, somente professor e adm
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESSOR")

                // qualquer outra rota autenticada, sem restrição extra de role
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }
}