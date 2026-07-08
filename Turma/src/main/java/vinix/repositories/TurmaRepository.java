package vinix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
}
