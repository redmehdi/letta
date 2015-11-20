package es.uvigo.esei.dgss.letta.service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
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
public class UserEJB {
	@PersistenceContext
	EntityManager em;
	@Inject
	TestingMailerEJB tmejb;
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
	public void registerUser(Registration user) {
		// check if the user login and email already exists
		if (em.find(User.class, user.getLogin()) == null
				&& findUserByEmail(user.getEmail()) == null) {
			// Get the request path Ex:
			// http://localhost:8080/context_path/resource?params
			final String scheme = request.getScheme();
			final String serverName = request.getServerName();
			final int serverPort = request.getServerPort();
			final String contextPath = request.getContextPath();
			// Transform the request into Ex:
			// http://localhost:8080/context_path/UUID
			final String resultPath = scheme + "://" + serverName + ":"
					+ serverPort + contextPath + "/" + user.getUuid();
			try {
				tmejb.sendEmail(user.getEmail(),
						generateRegistrationMessage(resultPath));
			} catch (final MessagingException e) {
			}
		} else {
			throw new EntityExistsException("User already exists");
		}
	}

	/**
	 * 
	 * Confirms a user registration
	 * 
	 * @param user
	 *            indicates the user who confirms the registration
	 */
	public void userConfirmation(Registration user) {
		if (request.toString().contains(user.getUuid())) {
			User newUser = new User(user.getLogin(), user.getPassword(),
					user.getEmail());
			em.persist(newUser);
		}

		// if (tmejb.getMail().get(user.getEmail()).contains(user.getUuid())) {
		// User newUser = new User(user.getLogin(), user.getPassword(),
		// user.getEmail());
		// em.persist(newUser);
		// }

	}

	/**
	 * Generates a message to complete the user registration
	 * 
	 * @param path
	 *            indicates the path to confirm the user
	 * @return the message with the link
	 */
	private String generateRegistrationMessage(String path) {
		final StringBuilder message = new StringBuilder();
		message.append("<html>");
		message.append("<head><title>Confirm registration</title></head>");
		message.append("<body></br></br>");
		message.append("<a href='" + path + "'" + ">Click here to confirm</a>");
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
	public User findUserByEmail(String email) {
		return (User) em
				.createQuery("SELECT u FROM User WHERE u.email =: email")
				.setParameter("email", email).getResultList();
	}
}
