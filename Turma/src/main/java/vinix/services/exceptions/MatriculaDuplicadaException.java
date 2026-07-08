package vinix.services.exceptions;

public class MatriculaDuplicadaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MatriculaDuplicadaException(Long alunoId, Long turmaId) {
        super("Aluno " + alunoId + " já matriculado na turma " + turmaId);
    }
}
