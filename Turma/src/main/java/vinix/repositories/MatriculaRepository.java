package vinix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByTurmaId(Long turmaId);
    long countByTurmaId(Long turmaId);
    Optional<Matricula> findByAlunoIdAndTurmaId(Long alunoId, Long turmaId);
}