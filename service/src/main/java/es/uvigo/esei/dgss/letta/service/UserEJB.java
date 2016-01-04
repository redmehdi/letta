package es.uvigo.esei.dgss.letta.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.Role;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UserNotifications;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.Validate.isTrue;

import static es.uvigo.esei.dgss.letta.domain.entities.Role.USER;

/**
 * {@linkplain UserEJB} is a service bean providing all the required
 * {@linkplain User user-related} methods.
 *
 * @author Jesús Álvarez Casanova
 * @author Adrián Rodríguez Fariña
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Mailer mailer;

    @Resource(lookup = "java:/letta/confirmation-url")
    private String confirmationUrl;

    @Resource
    private SessionContext ctx;

    @EJB
    private UserAuthorizationEJB auth;

    /**
     * Retrieves the {@link User} associated with the received login name,
     * returned as an {@link Optional} value that will be empty if the user is
     * not found. If the login is null, a {@link NullPointerException} will be
     * thrown.
     *
     * @param login The login {@link String} to find in the database.
     *
     * @return An {@link Optional} value holding the uniquely found {@link User}
     *         object, or empty otherwise.
     *
     * @throws IllegalArgumentException if the received login is null.
     */
    @PermitAll
    public Optional<User> get(
        final String login
    ) throws IllegalArgumentException {
        isTrue(nonNull(login), "Login string cannot be null");
        return ofNullable(em.find(User.class, login));
    }

    /**
     * Retrieves the {@link User} associated with the received email, returned
     * as an {@link Optional} value that will be empty if the user is not found.
     * If the email is null, a {@link NullPointerException} will be thrown/
     *
     * @param email The email {@link String} to find in the database.
     *
     * @return An {@link Optional} value holding the uniquely found
     *         {@link User}, or empty otherwise.
     *
     * @throws IllegalArgumentException if the received email is null.
     */
    @PermitAll
    public Optional<User> getByEmail(
        final String email
    ) throws IllegalArgumentException {
        isTrue(nonNull(email), "Email string cannot be null");

        final TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User.class
        ).setParameter("email", email);

        try {
            return Optional.of(query.getSingleResult());
        } catch (final NoResultException nre) {
            return Optional.empty();
        }
    }

    /**
     * Register an user
     *
     * @param registration
     *            indicates the user to register
     *
     * @throws LoginDuplicateException
     *             if the {@code user} login already exists
     * @throws EmailDuplicateException
     *             if the {@code user} email already exists
     * @throws MessagingException
     *             exception thrown by the Messaging classes
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(
        final Registration registration
    ) throws LoginDuplicateException, EmailDuplicateException, MessagingException {
        if (checkLogin(registration.getLogin())) {
        	ctx.setRollbackOnly();

            throw new LoginDuplicateException("Login duplicated");
        }

        if (checkEmail(registration.getEmail())) {
        	ctx.setRollbackOnly();

            throw new EmailDuplicateException("Email duplicated");
        }

        em.persist(registration);

        mailer.sendEmail("no_reply@letta.com", registration.getEmail(),
                "Confirm your registration",
                generateRegistrationMessage(registration.getUuid()));
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean userConfirmation(final String uuid) {
        if (em.find(Registration.class, uuid) == null)
            return false;
        else {
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
     * @param uuid
     *            indicates the user uuid
     * @return the registration message
     */
    private String generateRegistrationMessage(final String uuid) {
        return new StringBuilder().append("<html>")
                .append("<head><title>Confirm registration</title></head>")
                .append("<body><br/><br/>").append(confirmationUrl).append(uuid)
                .append("</body>").append("</html>").toString();
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
        return get(login).isPresent()
            || nonNull(findLoginInRegistration(login));
    }

    /**
     * Check if an email exists in both User and Registration tables
     *
     * @param email
     * @return true if it exists
     * @return false if it does not exist
     */
    private boolean checkEmail(final String email) {
        return getByEmail(email).isPresent()
            || nonNull(findEmailInRegistration(email));
    }

    /**
     * Finds a user by login in the User table
     *
     * @param login
     *            indicates the user with {@code login}
     * @return the result list if it is found
     * @return null if it is not found
     *
     * @deprecated Consider using {@link UserEJB#get(String)} instead, as it
     *             already checks the failure case where the {@link User} is not
     *             found, by returning it as an {@link Optional} value.
     */
    @Deprecated
    public User userWithLogin(final String login) {
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
    @PermitAll
    public Registration registrationWithLogin(final String login) {
        return em.find(Registration.class, login);
    }

    @RolesAllowed({ "ADMIN", "USER" })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User update(
        final User user
    ) throws IllegalArgumentException, SecurityException, EmailDuplicateException {
        isTrue(nonNull(user), "User cannot be null");

        final User current   = auth.getCurrentUser();
        final User persisted = get(user.getLogin()).orElseThrow(IllegalArgumentException::new);

        if (current.getRole().equals(USER)) {
            if (!current.equals(user))        throw new SecurityException("You are not allowed to update other users.");
            if (!user.getRole().equals(USER)) throw new SecurityException("You are not allowed to change your Role");
        }

        if (!persisted.getEmail().equals(user.getEmail()) && checkEmail(user.getEmail()))
            throw new EmailDuplicateException("Email already present in database");

        return em.merge(user);
    }

    /**
     * Gets all the {@link User} in the database.
     *
     * @return The {@link List} with all the {@link User} sorted alphabetically.
     * @throws EJBTransactionRolledbackException if the currently identified 
     * 		   {@link User} is not admin.
     */
	@RolesAllowed("ADMIN")
    public List<User> getUsers() throws EJBTransactionRolledbackException {
    	User currentUser = auth.getCurrentUser();
    	
		if (!currentUser.getRole().equals(Role.ADMIN)) {
			throw new EJBTransactionRolledbackException("User must be admin.");
		}

		return em.createQuery(
				"SELECT u FROM User u ORDER BY u.completeName ASC, u.login ASC", User.class)
				.getResultList();
    }

    /**
     * Remove the {@link User} in the database.
     *
     * @param login the {@link User} login.
     * @throws EJBTransactionRolledbackException if the {@link User} is null or
     *  	   the currently identified {@link User} is not admin.
     */
	@RolesAllowed("ADMIN")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeUser(final String login) 
    	throws EJBTransactionRolledbackException {
		
		User user = em.find(User.class, login);
    	User currentUser = auth.getCurrentUser();
		
		if (user == null) {
			throw new EJBTransactionRolledbackException("User cannot be null");
		}
		
		if (!currentUser.getRole().equals(Role.ADMIN)) {
			throw new EJBTransactionRolledbackException("User must be admin.");
		}

		em.createNativeQuery("DELETE FROM EventAttendees WHERE user_login = ?")
        .setParameter(1, user.getLogin())
        .executeUpdate();

		em.createQuery("DELETE FROM UserNotifications WHERE user = :user")
        .setParameter("user", user)
        .executeUpdate();

		em.createQuery("DELETE FROM User u WHERE u.login = :login")
        .setParameter("login", login)
        .executeUpdate();
    }
	
	/**
	 * Get all the {@link UserNotifications} by the current {@link User}.
	 * @return a list with {@link UserNotifications}.
	 */
    @RolesAllowed({"ADMIN", "USER"})
    public List<UserNotifications> getNotifications() {
    	User user = auth.getCurrentUser();
    	
		return em.createQuery(
			"SELECT un FROM UserNotifications un WHERE  un.userId = :login", UserNotifications.class)
			.setParameter("login", user.getLogin())
			.getResultList();
    }
    
	/**
	 * Count the unread {@link UserNotifications} by the current {@link User}.
	 * @return a number of unread {@link UserNotifications}
	 */
    @RolesAllowed({"ADMIN", "USER"})
    public Long countUnreadNotifications() {
    	User user = auth.getCurrentUser();
    	
		return em.createQuery(
			"SELECT COUNT (un) FROM UserNotifications un WHERE un.userId = :login "
			+ "AND un.readed = false", Long.class)
			.setParameter("login", user.getLogin())
			.getSingleResult();
    }
    
    /**
     * Find a {@link UserNotifications} in the database.
     * @param notificationId the id of the {@link UserNotifications}.
     * @return the {@link UserNotifications}.
     */
    @RolesAllowed({"ADMIN", "USER"})
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserNotifications getNotification(int notificationId) {
    	User user = auth.getCurrentUser();
    	UserNotifications userNotifications = null;

		try {
			userNotifications = em.createQuery(
				"SELECT un FROM UserNotifications un WHERE un.userId = :userId "
				+ "AND un.notificationId = :notificationId", UserNotifications.class)
				.setParameter("userId", user.getLogin())
				.setParameter("notificationId", notificationId)
				.getSingleResult();

		} catch (NoResultException nre) {
			return null;
		}
		
		userNotifications.setReaded(true);
		em.persist(userNotifications);
		
		return userNotifications;
    }
}
