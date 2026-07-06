package vinix.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vinix.entities.Aluno;


public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	Optional<Aluno> findByEmail(String email);
	Optional<Aluno> findByCpf(String cpf);
}
