package vinix.resources.exceptions;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;
import vinix.services.exceptions.VagasIndisponiveisException;

@ControllerAdvice
public class ResourceExceptionHandler {

	 //para registrar erros no console 
	private static final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(
			ResourceNotFoundException e,
			HttpServletRequest request){
		
		logger.error("Recurso não encontrado error: ", e); // log do erro
		
		
		String error = "recurso não encontrado";
		HttpStatus status = HttpStatus.NOT_FOUND;// 404
		
		StandardError err = new StandardError(
				Instant.now(), 
				status.value(), 
				error, 
				e.getMessage(), 
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(
			DatabaseException e,
			HttpServletRequest request){
		
		 logger.error("erro violação de integridade: ", e); // log do erro

	        String error = "violação de integridade";
	        HttpStatus status = HttpStatus.CONFLICT; // HTTP 409 
		
		StandardError err = new StandardError(
				Instant.now(), 
				status.value(), 
				error, 
				e.getMessage(), 
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(VagasIndisponiveisException.class)
	public ResponseEntity<StandardError> vagasIndisponiveis(
	        VagasIndisponiveisException e,
	        HttpServletRequest request) {

	    logger.error("Vagas indisponíveis: ", e);

	    String error = "Vagas indisponíveis";
	    HttpStatus status = HttpStatus.BAD_REQUEST; // 400

	    StandardError err = new StandardError(
	            Instant.now(),
	            status.value(),
	            error,
	            e.getMessage(),
	            request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> exception(
	        Exception e,
	        HttpServletRequest request) {

	    logger.error("Unexpected error: ", e);

	    String error = "Unexpected error";
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;// 500

	    StandardError err = new StandardError(
	            Instant.now(),
	            status.value(),
	            error,
	            "An unexpected error occurred", // mensagem genérica, para não voltar para o client
	            request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	
	
}
