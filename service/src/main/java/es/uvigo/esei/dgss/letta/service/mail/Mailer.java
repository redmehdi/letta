package es.uvigo.esei.dgss.letta.service.mail;

import javax.mail.MessagingException;

/**
 * @author jacasanova and arfarinha
 *
 */
public interface Mailer {
	/**
	 * 
	 * Sends an email
	 * 
	 * @param from
	 *            indicates the mail sender
	 * @param email
	 *            indicates the mail receiver
	 * @param subject
	 *            indicates the mail subject
	 * @param message
	 *            indicates the email message
	 * 
	 * @throws MessagingException
	 *             exception thrown by the Messaging classes
	 */
	public void sendEmail(String from, String email, String subject,
			String message) throws MessagingException;
}
