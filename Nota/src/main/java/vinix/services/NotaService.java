package vinix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import vinix.dto.AlunoDTO;
import vinix.dto.TurmaDTO;
import vinix.entities.Nota;
import vinix.feign.AlunoFeignClient;
import vinix.feign.TurmaFeignClient;
import vinix.repositories.NotaRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class NotaService {

	@Autowired
	private NotaRepository rep;

	@Autowired
	private TurmaFeignClient feignTurma;

	@Autowired
	private AlunoFeignClient feignAluno;

	public List<Nota> findAll() {
		return rep.findAll();
	}

	public Nota findById(Long id) {
		return rep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public List<Nota> findByAlunoId(Long alunoId) {
		return rep.findByAlunoId(alunoId);
	}

	public List<Nota> findByTurmaId(Long turmaId) {
		return rep.findByTurmaId(turmaId);
	}

	public Nota insert(Nota entity) {
		ResponseEntity<AlunoDTO> aluno = feignAluno.findById(entity.getAlunoId());
		if (!aluno.getStatusCode().is2xxSuccessful()) {
			throw new ResourceNotFoundException("Aluno não encontrado: " + entity.getAlunoId());
		}

		ResponseEntity<TurmaDTO> turma = feignTurma.findById(entity.getTurmaId());
		if (!turma.getStatusCode().is2xxSuccessful()) {
			throw new ResourceNotFoundException("Turma não encontrada: " + entity.getTurmaId());
		}

		return rep.save(entity);
	}

	@Transactional
	public void delete(Long id) {
		try {
			rep.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public Nota update(Long id, Nota obj) {
		try {
			Nota entity = rep.getReferenceById(id);
			entity.setNota1(obj.getNota1());
			entity.setNota2(obj.getNota2());
			entity.setNota3(obj.getNota3());
			entity.setAlunoId(obj.getAlunoId());
			entity.setTurmaId(obj.getTurmaId());
			return rep.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
}
