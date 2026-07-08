package vinix.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vinix.Dto.ProfessorDTO;

@FeignClient(name = "professor", 
path = "/professores", 
fallback = ProfessorFeignClientFallback.class)
public interface ProfessorFeignClient {

	@GetMapping("/{id}")
	ResponseEntity<ProfessorDTO> findById(@PathVariable Long id);
	
}
