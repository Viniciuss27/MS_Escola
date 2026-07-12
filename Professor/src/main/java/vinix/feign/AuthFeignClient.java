package vinix.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import vinix.dto.CriarUsuarioDTO;
import vinix.dto.UserDTO;

@FeignClient(
        name = "auth",
        fallbackFactory = AuthFeignClientFallbackFactory.class)
public interface AuthFeignClient {

    @PostMapping("/auth/usuarios")
    UserDTO criarUsuario(
            @RequestHeader("client-id") String clientId,
            @RequestHeader("client-secret") String clientSecret,
            @RequestBody CriarUsuarioDTO dto
    );
}