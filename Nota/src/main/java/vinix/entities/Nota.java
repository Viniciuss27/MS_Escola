package vinix.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Table(name = "tb_nota")
@Data @NoArgsConstructor @AllArgsConstructor
public class Nota implements Serializable {
	private static final long serialVersionUID = 1L;
	
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long alunoId;
  private Long turmaId;
  private Double nota1;
  private Double nota2;
  private Double nota3;
  
  public Double calcularMedia() {
	  return (nota1 + nota2 + nota3) / 3;
	}

	public String getSituacao() {
	  return calcularMedia() >= 6.0 ? "APROVADO" : "REPROVADO";
	}

}

