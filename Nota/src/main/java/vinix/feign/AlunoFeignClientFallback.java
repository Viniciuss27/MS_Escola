package vinix.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import vinix.dto.AlunoDTO;

@Component
public class AlunoFeignClientFallback implements AlunoFeignClient{

	@Override
	public ResponseEntity<AlunoDTO> findById(Long id) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}

}
