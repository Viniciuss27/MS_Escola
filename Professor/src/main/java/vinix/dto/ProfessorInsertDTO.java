package vinix.dto;

public record ProfessorInsertDTO(
		String nome, 
		String email, 
		String cpf, 
		String disciplina, 
		String senha) 
{}