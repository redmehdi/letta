package es.uvigo.esei.dgss.letta.service.util.mail;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import javax.mail.MessagingException;

import es.uvigo.esei.dgss.letta.mail.Email;
import es.uvigo.esei.dgss.letta.mail.MailBox;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

/**
 * Sends registration mails (testing)
 * 
 * @author jacasanova
 * @author arfarinha
 *
 */
@Alternative
@Singleton
public class TestingMailer extends MailBox implements Mailer {
	private boolean forceException = false;
	private Email email;

	@Override
	public void sendEmail(String from, String email, String subject,
			String message) throws MessagingException {
		
		this.email = new Email(from, email, subject, message);
		super.sendEmail(this.email);
		
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
		return this.email.getFrom();
	}

	public String getSubject() {
		return this.email.getSubject();
	}

	public String getEmail() {
		return this.email.getTo();
	}

	public String getMessage() {
		return this.email.getMessage();
	}
}
