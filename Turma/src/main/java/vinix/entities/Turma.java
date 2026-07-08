package vinix.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  private String nome;     // ex: 'Turma A - 2025'
  @Enumerated(EnumType.STRING) // para ter o nome e não nuemro no banco
  private Turno turno;     
  private Long professorId; 
  private Integer vagas;
}

