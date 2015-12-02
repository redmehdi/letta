package es.uvigo.esei.dgss.letta.mail;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A mailbox that stores email entities.
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public class MailBox {
	private final Deque<Email> emails;
	
	/**
	 * Constructs a new instance of {@link MailBox}.
	 */
	public MailBox() {
		this.emails = new LinkedList<>();
	}
	
	/**
	 * Stores a new email in this email box.
	 * 
	 * @param email an email.
	 */
	public void sendEmail(Email email) {
		this.emails.add(email);
	}

	/**
	 * Returns the last email sent.
	 * 
	 * @return the last email sent.
	 */
	public Email getLastEmail() {
		if (this.emails.isEmpty()) {
			return null;
		} else {
			return this.emails.getLast();
		}
	}
	
	/**
	 * Returns the emails sent.
	 * 
	 * @return the emails sent.
	 */
	public List<Email> getEmails() {
		return new ArrayList<>(this.emails);
	}
	
	/**
	 * Returns the number of emails sent.
	 * 
	 * @return the number of emails sent.
	 */
	public int countEmails() {
		return this.emails.size();
	}
	
	/**
	 * Clears this mail box, removing all the emails sent. 
	 */
	public void clear() {
		this.emails.clear();
	}
}
