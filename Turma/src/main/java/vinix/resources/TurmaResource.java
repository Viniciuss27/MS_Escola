package vinix.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import vinix.dto.AlunoDTO;
import vinix.entities.Matricula;
import vinix.entities.Turma;
import vinix.services.TurmaService;

@RestController
@RequestMapping(value = "/turmas")
public class TurmaResource {

	@Autowired
	private TurmaService serv;

	@GetMapping
	public ResponseEntity<List<Turma>> findAll() {
		return ResponseEntity.ok(serv.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Turma> findById(@PathVariable Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@PostMapping
	public ResponseEntity<Turma> insert(@RequestBody Turma turma) {
		turma = serv.insert(turma);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(turma.getId())
				.toUri();
		return ResponseEntity.created(uri).body(turma);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		serv.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Turma> update(@PathVariable Long id, @RequestBody Turma obj) {
		return ResponseEntity.ok(serv.update(id, obj));
	}

	@PostMapping(value = "/{turmaId}/matricular/{alunoId}")
	public ResponseEntity<Matricula> matricular(@PathVariable Long turmaId, @PathVariable Long alunoId) {
		Matricula matricula = serv.matricular(turmaId, alunoId);
		return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
	}

	@GetMapping(value = "/{id}/alunos")
	public ResponseEntity<List<AlunoDTO>> listarAlunosDaTurma(@PathVariable Long id) {
		return ResponseEntity.ok(serv.listarAlunosDaTurma(id));
	}
}