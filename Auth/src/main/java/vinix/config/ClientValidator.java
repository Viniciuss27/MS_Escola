package vinix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {

    @Value("${security.oauth2.client.client-id}")
    private String expectedClientId;

    @Value("${security.oauth2.client.client-secret}")
    private String expectedClientSecret;

    //Confere se client-id / client-secret recebidas via header batem com as do yml
    public boolean isValid(String clientId, String clientSecret) {
        return expectedClientId.equals(clientId)
                && expectedClientSecret.equals(clientSecret);
    }
}