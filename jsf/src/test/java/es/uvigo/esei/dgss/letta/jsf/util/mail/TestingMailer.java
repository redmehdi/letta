package es.uvigo.esei.dgss.letta.jsf.util.mail;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

import es.uvigo.esei.dgss.letta.mail.Email;
import es.uvigo.esei.dgss.letta.mail.MailBox;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

/**
 * A fake {@link Mailer} class that stores the emails instead of sending them.
 * As this class is an extension of the {@link MailBox} class, the emails sent
 * can be accessed through this class methods.
 * 
 * @author Miguel Reboiro Jato
 *
 */
@Singleton
@Alternative
public class TestingMailer extends MailBox implements Mailer {
	@Override
	public void sendEmail(String from, String to, String subject, String message) {
		super.sendEmail(new Email(from, to, subject, message));
	}
}
