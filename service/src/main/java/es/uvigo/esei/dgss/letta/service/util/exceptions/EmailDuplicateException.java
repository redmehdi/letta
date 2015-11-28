package es.uvigo.esei.dgss.letta.service.util.exceptions;

public class EmailDuplicateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public EmailDuplicateException(String mes) {
		this.message = mes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
