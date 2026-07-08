package vinix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vinix.entities.Matricula;
import vinix.repositories.MatriculaRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository rep;

    public List<Matricula> findAll() {
        return rep.findAll();
    }

    public Matricula findById(Long id) {
        return rep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
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

    // sem "matricular" aqui — essa regra já mora no TurmaService
}
