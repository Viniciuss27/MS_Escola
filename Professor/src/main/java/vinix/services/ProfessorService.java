package vinix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import vinix.entities.Professor;
import vinix.repositories.ProfessorRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class ProfessorService {
	
	@Autowired
	private ProfessorRepository rep;
	
	public List<Professor> findAll() {
		return rep.findAll();
	}

	public Professor findByCpf(String cpf) {
		return rep.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException(cpf));
	}

	public Professor findByEmail(String email) {
		return rep.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email));
	}

	public Professor findById(Long id) {
		return rep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Transactional
	public void deleteById(Long id) {
		try{
			rep.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public Professor update(Long id, Professor entity) {
		try {
		Professor obj = rep.getReferenceById(id);
		obj.setNome(entity.getNome());
		obj.setEmail(entity.getEmail());
		obj.setCpf(entity.getCpf());
		obj.setDisciplina(entity.getDisciplina());
		return rep.save(obj);		
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		
	}
	
	public Professor insert(Professor entity) {
		return rep.save(entity);
	}

	
}
