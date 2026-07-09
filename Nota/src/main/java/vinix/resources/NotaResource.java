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

import vinix.entities.Nota;
import vinix.services.NotaService;

@RestController
@RequestMapping(value = "/notas")
public class NotaResource {

	@Autowired
	private NotaService serv;

	@GetMapping
	public ResponseEntity<List<Nota>> findAll() {
		return ResponseEntity.ok(serv.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Nota> findById(@PathVariable Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@GetMapping(value = "/aluno/{alunoId}")
	public ResponseEntity<List<Nota>> findByAlunoId(@PathVariable Long alunoId) {
		return ResponseEntity.ok(serv.findByAlunoId(alunoId));
	}

	@GetMapping(value = "/turma/{turmaId}")
	public ResponseEntity<List<Nota>> findByTurmaId(@PathVariable Long turmaId) {
		return ResponseEntity.ok(serv.findByTurmaId(turmaId));
	}

	@PostMapping
	public ResponseEntity<Nota> insert(@RequestBody Nota nota) {
		nota = serv.insert(nota);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(nota.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nota);
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		serv.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "{id}")
	public ResponseEntity<Nota> update(@PathVariable Long id, @RequestBody Nota obj) {
		return ResponseEntity.ok(serv.update(id, obj));
	}	
}
