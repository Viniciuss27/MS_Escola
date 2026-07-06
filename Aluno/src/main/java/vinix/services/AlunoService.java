package vinix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import vinix.entities.Aluno;
import vinix.repositories.AlunoRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository rep;

	public Aluno findByEmail(String email) {
		return rep.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email));
	}

	public Aluno findByCpf(String cpf) {
		return rep.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException(cpf));
	}

	public List<Aluno> findAll() {
		return rep.findAll();
	}

	public Aluno findById(Long id) {
		return rep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Aluno insert(Aluno aluno) {
		return rep.save(aluno);
	}

	@Transactional
	public Aluno update(Long id, Aluno obj) {
		try {
			Aluno entity = rep.getReferenceById(id);
			entity.setNome(obj.getNome());
			entity.setEmail(obj.getEmail());
			entity.setCpf(obj.getCpf());
			entity.setDataNascimento(obj.getDataNascimento());
			return rep.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		try {
			rep.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}
