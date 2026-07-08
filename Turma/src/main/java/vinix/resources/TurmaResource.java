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
	public List<Turma> findAll() {
		return serv.findAll();
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

	@PutMapping(value = "/turmaId/{turmaId}/alunoId/{alunoId}")// id da turma e do aluno
	public Matricula matricular(@PathVariable Long turmaId, @PathVariable Long alunoId) {
		return serv.matricular(turmaId, alunoId);
	}

	@GetMapping(value = "/AlunosDaTurma/{id}")//id da turma
	public List<AlunoDTO> listarAlunosDaTurma(@PathVariable Long turmaId) {
		return serv.listarAlunosDaTurma(turmaId);
	}
	
	
	
}
