package es.uvigo.esei.dgss.letta.service.mail;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import javax.mail.MessagingException;

import es.uvigo.esei.dgss.letta.service.mail.Mailer;

/**
 * Sends registration mails (testing)
 * 
 * @author jacasanova
 * @author arfarinha
 *
 */
@Alternative
@Singleton
public class TestingMailer implements Mailer {
	private boolean forceException = false;

	private String from;
	private String subject;
	private String email;
	private String message;

	@Override
	public void sendEmail(String from, String email, String subject,
			String message) throws MessagingException {
		this.from = from;
		this.subject = subject;
		this.email = email;
		this.message = message;

		if (this.forceException)
			throw new MessagingException("Email could not be sent");
	}

	public boolean isForceException() {
		return forceException;
	}

	public void setForceException(boolean forceException) {
		this.forceException = forceException;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public String getEmail() {
		return email;
	}

	public String getMessage() {
		return message;
	}
}
