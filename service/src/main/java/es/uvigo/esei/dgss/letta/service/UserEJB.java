package es.uvigo.esei.dgss.letta.service;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.User;

/**
 * EJB for the user registration
 * 
 * @author jacasanova and arfarinha
 *
 */
@Stateless
public class UserEJB {
	@PersistenceContext
	EntityManager em;

	@Inject
	Mailer dmejb;
	@Context
	HttpServletRequest request;

	/**
	 * Registers a user
	 * 
	 * @param user
	 *            indicates the user to register
	 * @throws EntityExistsException
	 *             if the {@code login} already exists
	 */
	@PermitAll
	public boolean registerUser(final User user) throws EntityExistsException {
		if (em.find(User.class, user.getLogin()) == null
				&& findUserInRegistration(user.getLogin(),
						user.getEmail()) == null
				&& findUserByEmail(user.getEmail()) == null) {
			final Registration registration = new Registration(user);

			try {
				dmejb.sendEmail(user.getEmail(),
						generateRegistrationMessage(registration.getUuid()));

				em.persist(registration);
				return true;
			} catch (final MessagingException e) {
				e.printStackTrace();
				return true;
			}
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private String getPath(final String uuid) {
		String scheme = "http";
		try {
			scheme = request.getScheme();
		} catch (final NullPointerException e) {
			scheme = "http";
		}

		final int serverPort = request.getServerPort();
		final String serverName = request.getServerName();
		final String contextPath = request.getContextPath();

		return scheme + "://" + serverName + ":" + serverPort + contextPath
				+ "/" + uuid;
	}

	/**
	 * Confirms a user registration
	 * 
	 * @param uuid
	 *            indicates the user uuid
	 * @return true if the {@code uuid} doesn't exist
	 * @return false if the {@code uuid} already exists
	 */
	@PermitAll
	public boolean userConfirmation(final String uuid) {
		if (em.find(Registration.class, uuid) == null) {
			return false;
		} else {

			final Registration registration = em.find(Registration.class, uuid);
			final User user = registration.getUser();
			em.persist(user);
			em.remove(registration);
			return true;
		}
	}

	/**
	 * Generates a message to complete the user registration
	 * 
	 * @param path
	 *            indicates the path to confirm the user
	 * @return the message with the link
	 */
	private String generateRegistrationMessage(final String uuid) {
		final StringBuilder message = new StringBuilder();
		message.append("<html>");
		message.append("<head><title>Confirm registration</title></head>");
		message.append("<body><br/><br/>");
		message.append(
				"<a href=\"http://localhost:9080/letta/jsf/faces/confirm.xhtml?uuid="
						+ uuid + "\">Click here to confirm</a>");
		message.append("</body>");
		message.append("</html>");
		return message.toString();
	}

	/**
	 * 
	 * @param email
	 *            indicates the user email
	 * @return the user whose email is the same than {@code email}
	 */
	public User findUserByEmail(final String email) {
		try {
			return em
					.createQuery("SELECT u FROM User u WHERE u.email=:email",
							User.class)
					.setParameter("email", email).getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}

	}

	/**
	 * 
	 * Check if a user exists with the user {@code login} and {@code email}
	 * 
	 * @param login
	 *            indicates the user login
	 * @param email
	 *            indicates the user email
	 * @return the user if exists
	 * @return null if the user doesn't exist
	 */
	public Registration findUserInRegistration(final String login,
			final String email) {
		try {
			return em
					.createQuery(
							"SELECT u FROM Registration u WHERE u.email=:email or u.login=:login",
							Registration.class)
					.setParameter("email", email).setParameter("login", login)
					.getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}
}
