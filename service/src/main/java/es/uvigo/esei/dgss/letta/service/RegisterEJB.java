package es.uvigo.esei.dgss.letta.service;

import javax.ejb.EJB;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.letta.domain.User;

/**
 * EJB for the user registration
 * 
 * @author jacasanova and arfarinha
 *
 */
public class RegisterEJB {
	@PersistenceContext
	EntityManager em;
	@EJB
	MailerEJB mejb;

	/**
	 * Returns the registered user
	 * 
	 * @param login
	 *            indicates the user login
	 * @param pass
	 *            indicates the user password
	 * @param email
	 *            indicates the user email
	 * @return the user who has been registered
	 * @throws NullPointerException
	 *             if the {@code login} is null
	 * @throws EntityExistsException
	 *             if the {@code login} already exists
	 */
	public User registerUser(String login, String pass, String email) {
		if (login == null) {
			throw new NullPointerException();
		} else {
			if (em.find(User.class, login) == null) {
				User user = new User(login, pass, email);
				em.persist(user);
				try {
					mejb.sendEmail(login, user.getEmail());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return user;
			} else {
				throw new EntityExistsException("User already exists");
			}
		}
	}

	/**
	 * 
	 * @param login
	 *            indicates the user login
	 * @return the user who has been confirmed
	 */
	public User userConfirmation(String login) {
		User user = em.find(User.class, login);
		em.persist(user);
		return user;
	}
}
