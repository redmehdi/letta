package es.uvigo.esei.dgss.letta.service;

import javax.ejb.Local;
import javax.mail.MessagingException;

/**
 * @author jacasanova and arfarinha
 *
 */
@Local
public interface Mailer {
	/**
	 * 
	 * @param email
	 *            indicates the mail receiver
	 * @param message
	 *            indicates the email message
	 * @throws MessagingException
	 *             exception thrown by the Messaging classes
	 */
	public void sendEmail(String email, String message)
			throws MessagingException;
}
