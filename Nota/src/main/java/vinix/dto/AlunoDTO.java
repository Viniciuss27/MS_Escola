package vinix.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class AlunoDTO {
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private LocalDate dataNascimento;
}