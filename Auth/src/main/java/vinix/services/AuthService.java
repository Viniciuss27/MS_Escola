package vinix.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import vinix.config.JwtUtil;
import vinix.dto.CriarUsuarioDTO;
import vinix.dto.LoginDTO;
import vinix.dto.Token;
import vinix.dto.UserDTO;
import vinix.entities.Role;
import vinix.entities.User;
import vinix.repositories.RoleRepository;
import vinix.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Autentica o email + senha, se válido, gera o token JWT
    // Se estiverem erradas, o AuthenticationManager já lança excessão
    public Token login(LoginDTO loginDTO) {
        // busca o usuário (UserDetailsService) e compara a senha
        var authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.email(), loginDTO.password());

        authenticationManager.authenticate(authToken);

        // autenticação passou -> busca o usuário completo para gerar o token
        User user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário autenticado não encontrado no banco: " + loginDTO.email()));

        String jwt = jwtUtil.generateToken(user);
        return new Token(jwt, "Bearer");
    }

    //Cria um novo usuário no auth, com a role informada
    public UserDTO criarUsuario(CriarUsuarioDTO dto) {
        // busca a role pelo nome (ex: "ROLE_PROFESSOR")
        Role role = roleRepository.findByRoleName(dto.roleName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Role não encontrada: " + dto.roleName()));

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());

        // para não salva senha em texto puro
        user.setPassword(passwordEncoder.encode(dto.password()));

        user.getRoles().add(role);

        User salvo = userRepository.save(user);

        return toDTO(salvo);
    }

    // Converte a Entity para o DTO de resposta da API, sem expor a senha na API
    private UserDTO toDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), roleNames);
    }
}