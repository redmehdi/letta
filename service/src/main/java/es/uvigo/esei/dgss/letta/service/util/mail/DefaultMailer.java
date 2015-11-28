package es.uvigo.esei.dgss.letta.service.util.mail;

import java.util.Date;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.Singleton;
import javax.enterprise.inject.Default;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Sends registration mails
 * 
 * @author jacasanova
 * @author arfarinha
 *
 */
@Default
@Singleton
public class DefaultMailer implements Mailer {

	@Resource(name = "java:/Mail")
	private Session mailSession;

	/**
	 * 
	 * Sends and email
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
	@PermitAll
	@Override
	public void sendEmail(String from, String email, String subject,
			String message) throws MessagingException {
		// creates a new e-mail message
		final Message msg = new MimeMessage(getEmailSession());

		msg.setFrom(new InternetAddress(from));
		final InternetAddress[] toAddresses = { new InternetAddress(email) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		// set plain text message
		msg.setContent(message, "text/html");

		// sends the e-mail
		Transport.send(msg);
	}

	/**
	 * 
	 * @return the mail session
	 */
	private Session getEmailSession() {
		return mailSession;
	}
}
