package vinix.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import vinix.Dto.ProfessorDTO;

@Component
public class ProfessorFeignClientFallback implements ProfessorFeignClient{

	@Override
	public ResponseEntity<ProfessorDTO> findById(@PathVariable Long id){
		ProfessorDTO  professor = new ProfessorDTO(id, " fallback", "sem email ", "000-000-000.00 ", "sem disciplina");
		return ResponseEntity.ok(professor);
	}
}
