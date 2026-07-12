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

import vinix.dto.AlunoInsertDTO;
import vinix.entities.Aluno;
import vinix.services.AlunoService;

@RestController
@RequestMapping(value = "/alunos")
public class AlunoResource {

	@Autowired
	private AlunoService serv;
	
	@GetMapping
	public ResponseEntity<List<Aluno>> findAll() {
		return ResponseEntity.ok(serv.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Aluno> findById(@PathVariable Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<Aluno> findByCpf(@PathVariable String cpf) {
		return ResponseEntity.ok(serv.findByCpf(cpf));
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<Aluno> findByEmail(@PathVariable String email) {
		return ResponseEntity.ok(serv.findByEmail(email));
	}

	@PostMapping
    public ResponseEntity<Aluno> insert(@RequestBody AlunoInsertDTO dto) {
        
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setCpf(dto.cpf());
        aluno.setDataNascimento(dto.dataNascimento());
 
        aluno = serv.insert(aluno, dto.senha());
 
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(aluno.getId())
                .toUri();
        return ResponseEntity.created(uri).body(aluno);
    }

	@PutMapping(value = "/{id}")
	public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno aluno) {
		return ResponseEntity.ok(serv.update(id, aluno));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
	    serv.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
		
}
