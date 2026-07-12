package vinix.dto;

import java.time.LocalDate;

public record AlunoInsertDTO(
		String nome,
		String email,
		String cpf,
		LocalDate dataNascimento, 
		String senha) 
{}
