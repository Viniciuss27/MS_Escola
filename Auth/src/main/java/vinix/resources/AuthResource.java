package vinix.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vinix.config.ClientValidator;
import vinix.dto.CriarUsuarioDTO;
import vinix.dto.LoginDTO;
import vinix.dto.Token;
import vinix.dto.UserDTO;
import vinix.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClientValidator clientValidator;

    //Login: autentica o email + senha e retorna o token JWT
    @PostMapping("/token")
    public ResponseEntity<Token> login(
            @RequestHeader("client-id") String clientId,
            @RequestHeader("client-secret") String clientSecret,
            @RequestBody LoginDTO loginDTO) {

        if (!clientValidator.isValid(clientId, clientSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Token token = authService.login(loginDTO);
        return ResponseEntity.ok(token);
    }

    //chamado internamente pelo professor e aluno, no momento do cadastro, para provisionar o acesso
    @PostMapping("/usuarios")
    public ResponseEntity<UserDTO> criarUsuario(
            @RequestHeader("client-id") String clientId,
            @RequestHeader("client-secret") String clientSecret,
            @RequestBody CriarUsuarioDTO dto) {

        if (!clientValidator.isValid(clientId, clientSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO criado = authService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}