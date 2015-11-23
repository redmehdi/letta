package es.uvigo.esei.dgss.letta.service.exceptions;

public class LoginDuplicateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public LoginDuplicateException(String mes) {
		this.message = mes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
