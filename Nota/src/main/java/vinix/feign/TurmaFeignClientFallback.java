package vinix.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vinix.dto.TurmaDTO;

@Component
public class TurmaFeignClientFallback implements TurmaFeignClient {
	
	@Override
	public ResponseEntity<TurmaDTO> findById(Long id) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}
}