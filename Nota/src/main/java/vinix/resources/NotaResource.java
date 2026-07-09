package vinix.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import vinix.dto.NotaDTO;
import vinix.entities.Nota;
import vinix.services.NotaService;

@RestController
@RequestMapping(value = "/notas")
public class NotaResource {

	@Autowired
	private NotaService serv;

	@GetMapping
	public ResponseEntity<List<NotaDTO>> findAll() {
		List<NotaDTO> list = serv.findAll().stream()
				.map(NotaDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<NotaDTO> findById(@PathVariable Long id) {
		Nota nota = serv.findById(id);
		return ResponseEntity.ok(new NotaDTO(nota));
	}

	@GetMapping(value = "/aluno/{alunoId}")
	public ResponseEntity<List<NotaDTO>> findByAlunoId(@PathVariable Long alunoId) {
		List<NotaDTO> list = serv.findByAlunoId(alunoId).stream()
				.map(NotaDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@GetMapping(value = "/turma/{turmaId}")
	public ResponseEntity<List<NotaDTO>> findByTurmaId(@PathVariable Long turmaId) {
		List<NotaDTO> list = serv.findByTurmaId(turmaId).stream()
				.map(NotaDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<NotaDTO> insert(@RequestBody Nota nota) {
		nota = serv.insert(nota);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(nota.getId())
				.toUri();
		return ResponseEntity.created(uri).body(new NotaDTO(nota));
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		serv.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "{id}")
	public ResponseEntity<NotaDTO> update(@PathVariable Long id, @RequestBody Nota obj) {
		Nota nota = serv.update(id, obj);
		return ResponseEntity.ok(new NotaDTO(nota));
	}
}