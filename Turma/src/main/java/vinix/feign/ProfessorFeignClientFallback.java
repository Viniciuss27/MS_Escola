package vinix.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import vinix.dto.ProfessorDTO;

@Component
public class ProfessorFeignClientFallback implements ProfessorFeignClient{

	@Override
	public ResponseEntity<ProfessorDTO> findById(Long id) {
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}
}
