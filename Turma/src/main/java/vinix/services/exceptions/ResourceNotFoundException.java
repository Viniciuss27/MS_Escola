package vinix.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(Object msg) {
		super("Recurso nã encontrado: " + msg);
	}
	
	

}
