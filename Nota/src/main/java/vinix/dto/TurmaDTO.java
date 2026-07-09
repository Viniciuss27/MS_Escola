package vinix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class TurmaDTO {
	private Long id;
	private String nome;
	private String turno;
	private Long professorId;
	private Integer vagas;
}