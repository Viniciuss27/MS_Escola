package vinix.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import vinix.dto.AlunoDTO;

@FeignClient(
		name = "aluno",
		path = "/alunos",
		fallback = AlunoFeignClientFallback.class)
public interface AlunoFeignClient {

	@GetMapping("/{id}")
	ResponseEntity<AlunoDTO> findById(Long id);
	
}
