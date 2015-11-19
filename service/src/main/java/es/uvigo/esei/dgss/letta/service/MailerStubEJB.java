package es.uvigo.esei.dgss.letta.service;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
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
@Alternative
@Singleton
public class MailerStubEJB {

	final static String host = "localhost"; // indicates the destination host
	final static String port = "25"; // indicates the destination port
	final static String mailFrom = "registrationMail@letta.com"; // indicates
																	// the mail
																	// source
	final static String password = "registrationPassword";// indicates the mail
															// source password
	final static String subject = "Confirm your registration"; // indicates the
																// mail subject

	private String generateMessage() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title>Registration</title></head>");
		htmlBuilder.append("<body></br></br>");
		htmlBuilder.append("<a href='localhost:9080/confirmateRegistration/'"
				+ UUID.randomUUID() + ">Click here to confirm</a>");
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		return htmlBuilder.toString();
	}

	/**
	 * @param toAddress
	 *            indicates the mail receiver
	 * @throws MessagingException
	 *             exception thrown by the messaging classes
	 */
	public void sendEmail(String toAddress) throws MessagingException {

		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailFrom, password);
			}
		};

		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(mailFrom));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		// set plain text message
		msg.setContent(generateMessage(), "text/html");

		// sends the e-mail
		Transport.send(msg);
	}
}
