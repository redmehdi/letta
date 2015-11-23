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
import es.uvigo.esei.dgss.letta.service.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.exceptions.LoginDuplicateException;

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
	private Mailer dmejb;
	@Context
	HttpServletRequest request;

	/**
	 * 
	 * Register an user
	 * 
	 * @param user
	 *            indicates the user to register
	 * 
	 * @throws LoginDuplicateException
	 *             if the {@code user} login already exists
	 * @throws EmailDuplicateException
	 *             if the {@code user} email already exists
	 */
	@PermitAll
	public void registerUser(final User user)
			throws LoginDuplicateException, EmailDuplicateException {
		if (checkLogin(user.getLogin())) {
			throw new LoginDuplicateException("Login duplicated");
		} else {
			if (checkEmail(user.getEmail())) {
				throw new EmailDuplicateException("Email duplicated");
			} else {
				final Registration registration = new Registration(user);

				try {
					dmejb.sendEmail(user.getEmail(),
							generateRegistrationMessage(
									registration.getUuid()));

					em.persist(registration);
				} catch (final MessagingException e) {
				}
			}
		}
	}

	/**
	 * Returns the absolute path to the resource
	 * 
	 * @param uuid
	 *            uuid of the registration table
	 * @return absolute path
	 */
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
	 * Finds an user whose email is the same as the same passed as a parameter
	 * 
	 * @param email
	 *            indicates the user email
	 * @return the user whose email is the same than {@code email}
	 */
	private User findUserByEmail(final String email) {
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
	 * Check if a user exists with the user and {@code email}
	 * 
	 * @param email
	 *            indicates the user email
	 * @return the user if exists
	 * @return null if the user doesn't exist
	 */
	private Registration findEmailInRegistration(final String email) {
		try {
			return em
					.createQuery(
							"SELECT u FROM Registration u WHERE u.email=:email",
							Registration.class)
					.setParameter("email", email).getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds registrations in the Registration table
	 * 
	 * @param login
	 *            identifier of the user
	 * @return the result list of the query
	 * @return null if it doesn't exist
	 */
	private Registration findLoginInRegistration(final String login) {
		try {
			return em
					.createQuery(
							"SELECT u FROM Registration u WHERE u.login=:login",
							Registration.class)
					.setParameter("login", login).getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Checks if a login exists , both in the User table and in the Registration
	 * table
	 * 
	 * @param login
	 * @return true if it exists
	 * @return false if it does not exist
	 */
	private boolean checkLogin(final String login) {
		if (em.find(User.class, login) == null
				&& findLoginInRegistration(login) == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Check if an email exists in both User and Registration tables
	 * 
	 * @param email
	 * @return true if it exists
	 * @return false if it does not exist
	 */
	private boolean checkEmail(final String email) {
		if (findUserByEmail(email) == null
				&& findEmailInRegistration(email) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Finds a user by login in the User table
	 * 
	 * @param login
	 * @return the result list if it is found
	 * @return null if it is not found
	 */
	public User userWithLogin(String login) {
		return em.find(User.class, login);
	}

	/**
	 * Finds a registration by login
	 * 
	 * @param login
	 *            indicates the user with {@code login}
	 * @return the result list of registrations
	 * @return null if it does not exist
	 */
	public Registration registrationWithLogin(String login) {
		return em.find(Registration.class, login);
	}
}
