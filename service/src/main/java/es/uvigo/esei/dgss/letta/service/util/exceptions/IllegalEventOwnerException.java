package es.uvigo.esei.dgss.letta.service.util.exceptions;

public class IllegalEventOwnerException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public IllegalEventOwnerException(String mes) {
		this.setMessage(mes);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

