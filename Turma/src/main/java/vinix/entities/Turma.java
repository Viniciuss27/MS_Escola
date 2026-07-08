package vinix.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "tb_turma")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Turma implements Serializable {
	private static final long serialVersionUID = 1L;
	
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nome;      // ex: 'Turma A - 2025'
  private String turno;     // MANHA, TARDE, NOITE
  private Long professorId; // ID do professor responsável
  private Integer vagas;
}

