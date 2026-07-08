package vinix.services.exceptions;

public class VagasIndisponiveisException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public VagasIndisponiveisException(Long msg) {
		super("vagas indisponiveis: " + msg);
	}
	
	

}
