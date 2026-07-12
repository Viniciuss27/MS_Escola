package vinix.dto;


 // Entrada para o endpoint POST /auth/usuarios
 // Usado pelos serviços professor e aluno (via Feign) para
 // provisionar o acesso de um novo usuário no momento do cadastro
 // roleName deve ser o nome exato de uma Role já existente no banco
 // (ex: "ROLE_PROFESSOR", "ROLE_ALUNO", "ROLE_ADMIN")
public record CriarUsuarioDTO(
        String name,
        String email,
        String password,
        String roleName
) {}