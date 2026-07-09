package vinix.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vinix.dto.TurmaDTO;

@FeignClient(
		name = "turma",
        path = "/turmas",
        fallback = TurmaFeignClientFallback.class)
public interface TurmaFeignClient {
	
	@GetMapping("/{id}")
	ResponseEntity<TurmaDTO> findById(@PathVariable Long id);
}