package es.uvigo.esei.dgss.letta.mail;

/**
 * An entity that represents an email and contains the basic information that an
 * email usually has (sender, recipient, subject and message). 
 * 
 * @author Miguel Reboiro-Jato
 */
public class Email {
	private final String from;
	private final String to;
	private final String subject;
	private final String message;

	/**
	 * Constructs a new instance of {@link Email}.
	 *
	 * @param from email of the sender.
	 * @param to email of the recipient.
	 * @param subject subject of the email.
	 * @param message body of the email.
	 */
	public Email(String from, String to, String subject, String message) {
		this.from = from;
		this.subject = subject;
		this.to = to;
		this.message = message;
	}

	/**
	 * Returns from email of the sender.
	 * 
	 * @return from email of the sender.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Returns to email of the recipient.
	 * 
	 * @return to email of the recipient.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Returns subject subject of the email.
	 * 
	 * @return subject subject of the email.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns message body of the email.
	 * 
	 * @return message body of the email.
	 */
	public String getSubject() {
		return subject;
	}
}
