package es.uvigo.esei.dgss.letta.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import javax.mail.MessagingException;

/**
 * EJB for the registration mails
 * 
 * @author jacasanova and arfarinha
 *
 */
@Alternative
@Singleton
public class TestingMailerEJB implements Mailer {

	Map<String, String> mail = new HashMap<String, String>();

	public Map<String, String> getMail() {
		return mail;
	}

	public void setMail(Map<String, String> mail) {
		this.mail = mail;
	}

	@Override
	public void sendEmail(String email, String message)
			throws MessagingException {
		mail.put(email, message);
	}
}
