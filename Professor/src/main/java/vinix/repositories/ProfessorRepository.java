package vinix.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Professor;


public interface ProfessorRepository extends JpaRepository<Professor, Long>{
	
	Optional<Professor> findByCpf(String cpf);
	Optional<Professor> findByEmail(String email);
}
