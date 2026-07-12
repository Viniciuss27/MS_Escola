package vinix.dto;
 
// Recebe email/senha que serão validadas pelo AuthenticationManager
public record LoginDTO(String email, String password) {}