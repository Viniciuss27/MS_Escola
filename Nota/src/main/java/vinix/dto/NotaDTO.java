package vinix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vinix.entities.Nota;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class NotaDTO {
	private Long id;
	private Long alunoId;
	private Long turmaId;
	private Double nota1;
	private Double nota2;
	private Double nota3;
	private Double media;
	private String situacao;

	public NotaDTO(Nota entity) {
		id = entity.getId();
		alunoId = entity.getAlunoId();
		turmaId = entity.getTurmaId();
		nota1 = entity.getNota1();
		nota2 = entity.getNota2();
		nota3 = entity.getNota3();
		media = entity.calcularMedia();
		situacao = entity.getSituacao();
	}
}