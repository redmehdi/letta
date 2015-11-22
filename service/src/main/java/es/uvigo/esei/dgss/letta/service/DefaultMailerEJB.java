package es.uvigo.esei.dgss.letta.service;

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
 * EJB for the registration mails
 * 
 * @author jacasanova and arfarinha
 *
 */
@Default
@Singleton
public class DefaultMailerEJB implements Mailer {

	@Resource(name = "java:/Mail")
	private Session mailSession;

	final static String subject = "Confirm your registration"; // indicates the
																// email subject
	final static String from = "no_reply@letta.com"; // indicates the email
														// source

	/**
	 * @param email
	 *            indicates the mail receiver
	 * @throws MessagingException
	 *             exception thrown by the messaging classes
	 */
	@PermitAll
	@Override
	public void sendEmail(String email, String message)
			throws MessagingException {
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
