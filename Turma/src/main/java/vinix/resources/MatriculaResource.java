package vinix.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vinix.entities.Matricula;
import vinix.services.MatriculaService;

@RestController
@RequestMapping(value = "/matricula")
public class MatriculaResource {

	@Autowired
	private MatriculaService serv;

	@GetMapping
	public ResponseEntity<List<Matricula>> findAll() {
		return ResponseEntity.ok(serv.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Matricula> findById(@PathVariable Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		serv.deleteById(id);
		return ResponseEntity.noContent().build();
	}	
}
