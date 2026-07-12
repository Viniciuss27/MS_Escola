package vinix.dto;

public record CriarUsuarioDTO(
        String name,
        String email,
        String password,
        String roleName
) {}