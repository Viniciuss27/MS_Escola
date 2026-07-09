package vinix.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Nota;

public interface NotaRepository extends JpaRepository<Nota, Long> {
	List<Nota> findByAlunoId(Long alunoId);
	List<Nota> findByTurmaId(Long turmaId);
}