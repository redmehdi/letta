package es.uvigo.esei.dgss.letta.service.util.exceptions;

public class EmailDuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailDuplicateException(String message) {
		super(message);
	}
}
