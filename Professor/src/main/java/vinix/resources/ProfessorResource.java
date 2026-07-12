package vinix.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import vinix.dto.ProfessorInsertDTO;
import vinix.entities.Professor;
import vinix.services.ProfessorService;

@RestController @RequestMapping(value = "/professores")
public class ProfessorResource {

	@Autowired
	private ProfessorService serv;

	@GetMapping
	public ResponseEntity<List<Professor>> findAll() {
		return ResponseEntity.ok(serv.findAll());
	}

	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<Professor> findByCpf(@PathVariable String cpf) {
		return ResponseEntity.ok(serv.findByCpf(cpf));
	}

	@GetMapping(value = "/email/{email}")
	public ResponseEntity<Professor> findByEmail(@PathVariable String email) {
		return ResponseEntity.ok(serv.findByEmail(email));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Professor> findById(@PathVariable Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		serv.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Professor> update(@PathVariable Long id, @RequestBody Professor entity) {
		return ResponseEntity.ok(serv.update(id, entity));
	}

	@PostMapping
    public ResponseEntity<Professor> insert(@RequestBody ProfessorInsertDTO dto) {
        
        Professor professor = new Professor();
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setCpf(dto.cpf());
        professor.setDisciplina(dto.disciplina());
 
        professor = serv.insert(professor, dto.senha());
 
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(professor.getId())
                .toUri();
        return ResponseEntity.created(uri).body(professor);
    }
}