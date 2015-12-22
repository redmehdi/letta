package es.uvigo.esei.dgss.letta.service.util.exceptions;

public class LoginDuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoginDuplicateException(String message) {
		super(message);
	}
}
