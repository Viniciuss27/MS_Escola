package vinix.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class AuthFeignClientFallbackFactory implements FallbackFactory<AuthFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(AuthFeignClientFallbackFactory.class);

    @Override
    public AuthFeignClient create(Throwable cause) {
        return (clientId, clientSecret, dto) -> {
            logger.error("Não foi possível provisionar acesso no auth para o email {}. Motivo: {}",
                    dto.email(), cause.getMessage());
            return null;
        };
    }
}