package vinix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import vinix.entities.Nota;
import vinix.feign.TurmaFeignClient;
import vinix.repositories.NotaRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class NotaService {

	
	
}
