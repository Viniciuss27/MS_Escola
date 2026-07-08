package vinix.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import vinix.dto.AlunoDTO;
import vinix.entities.Matricula;
import vinix.entities.Turma;
import vinix.feign.AlunoFeignClient;
import vinix.repositories.MatriculaRepository;
import vinix.repositories.TurmaRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.MatriculaDuplicadaException;
import vinix.services.exceptions.ResourceNotFoundException;
import vinix.services.exceptions.VagasIndisponiveisException;

@Service
public class TurmaService {

	@Autowired
	private TurmaRepository turmaRep;

	@Autowired
	private MatriculaRepository matriculaRep;

	@Autowired
	private AlunoFeignClient alunoClient;

	public List<Turma> findAll() {
		return turmaRep.findAll();
	}

	public Turma findById(Long id) {
		return turmaRep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Turma insert(Turma obj) {
		return turmaRep.save(obj);
	}

	@Transactional
	public void deleteById(Long id) {
		try {
			turmaRep.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Transactional
	public Turma update(Long id, Turma obj) {
		try {
			Turma entity = turmaRep.getReferenceById(id);
			entity.setNome(obj.getNome());
			entity.setProfessorId(obj.getProfessorId());
			entity.setTurno(obj.getTurno());
			entity.setVagas(obj.getVagas());
			return turmaRep.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}

	}

	@Transactional
	public Matricula matricular(Long turmaId, Long alunoId) {
		Turma turma = turmaRep.findById(turmaId).orElseThrow(() -> new ResourceNotFoundException(turmaId));

		// verifica se tem vaga
		long matriculasAtuais = matriculaRep.countByTurmaId(turmaId);
		if (matriculasAtuais >= turma.getVagas()) {
			throw new VagasIndisponiveisException(turmaId);
		}

		// verifica se o aluno ja esta na turma
		Optional<Matricula> existente = matriculaRep.findByAlunoIdAndTurmaId(alunoId, turmaId);
		if (existente.isPresent()) {
			throw new MatriculaDuplicadaException(alunoId, turmaId);
		}

		// verifica se o aluno existe
		ResponseEntity<AlunoDTO> alunoResponse = alunoClient.findById(alunoId);
		if (!alunoResponse.getStatusCode().is2xxSuccessful() || alunoResponse.getBody() == null) {
			throw new ResourceNotFoundException(alunoId);
		}

		Matricula matricula = new Matricula();
		matricula.setTurmaId(turmaId);
		matricula.setAlunoId(alunoId);
		matricula.setDataMatricula(LocalDate.now());
		return matriculaRep.save(matricula);
	}

	public List<AlunoDTO> listarAlunosDaTurma(Long turmaId) {
		List<Matricula> matriculas = matriculaRep.findByTurmaId(turmaId);
		return matriculas.stream()
				.map(m -> alunoClient.findById(m.getAlunoId())
				.getBody()).filter(Objects::nonNull) // remove nulls vindos do fallback
				.collect(Collectors.toList());
	}
}