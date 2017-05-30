package es.uvigo.esei.dgss.letta.service;

import static es.uvigo.esei.dgss.letta.domain.entities.FriendshipState.ACCEPTED;
import static es.uvigo.esei.dgss.letta.domain.entities.FriendshipState.PENDING;
import static es.uvigo.esei.dgss.letta.domain.entities.FriendshipState.REJECTED;
import static es.uvigo.esei.dgss.letta.domain.entities.Role.USER;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.Validate.isTrue;

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

import es.uvigo.esei.dgss.letta.domain.entities.Friendship;
import es.uvigo.esei.dgss.letta.domain.entities.FriendshipState;
import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.Role;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UserNotifications;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

/**
 * {@linkplain UserEJB} is a service bean providing all the required
 * {@linkplain User user-related} methods.
 *
 * @author Jesús Álvarez Casanova
 * @author Adrián Rodríguez Fariña
 * @author world1mehdi
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

    /**
     * Make {@link Friendship} in the database.
     *
     * @param friendLogin the received User of the {@link Friendship}
     *
     */
    @RolesAllowed({"ADMIN", "USER"})
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void sendRequest(final String friendLogin) {
    	isTrue(nonNull(friendLogin), "Friend cannot be null");
    	final User user = auth.getCurrentUser();
    	final User friend = em.find(User.class, friendLogin);
        if (user == null && friend ==null ){
        	throw new EJBTransactionRolledbackException("User cannot be null");
        }else {
        	Friendship friendship = new Friendship();
        	friendship.setUser(user);
        	friendship.setFriend(friend);
        	friendship.setFriendshipState(PENDING);
            em.persist(friendship);

        }
    }

    /**
     * Find the fiends request {@link Friendship}
     * @return the {@link List} in all {@link Friendship}
     */
    @RolesAllowed({"ADMIN", "USER"})
    public List<Friendship> friendRequestList() {
		final User user = auth.getCurrentUser();
		if (user == null)
			return null;
		else {
			return em
					.createQuery("SELECT f FROM Friendship f WHERE f.friend.login = :userId "
							+ "AND f.friendshipState = 'PENDING' ", Friendship.class)
					.setParameter("userId", user.getLogin()).getResultList();
		}
    }

    /**
     * Accept or reject friend's request {@link Friendship}
     * @param friendLogin The login' friend {@link User}}
     * @param acceptFriendShip true in case accept and vice versa
     *
     */
    @RolesAllowed({"ADMIN", "USER"})
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void acceptOrRejectFriendRequest(final String friendLogin, final boolean acceptFriendShip) {
    	isTrue(nonNull(friendLogin), "FriendName cannot be null");
    	isTrue(nonNull(acceptFriendShip), "The acceptance category cannot be null");
    	final User user = auth.getCurrentUser();
		Friendship friendship = em
				.createQuery(
						"SELECT f FROM Friendship f WHERE f.user.login = "
						+ ":userId AND f.friendshipState = 'PENDING' "
							+ "AND f.friend.login = :friendId ",
										Friendship.class)
								.setParameter("friendId", user.getLogin())
								.setParameter("userId", friendLogin)
										.getSingleResult();
		if (acceptFriendShip) {
			friendship.setFriendshipState(ACCEPTED);
		} else {
			friendship.setFriendshipState(REJECTED);
		}
		em.merge(friendship);

	}

    /**
     * Get all friend' requests be sent {@link Friendship}
     * @return The {@link List} in all {@link Friendship}
     */
    @RolesAllowed({"ADMIN", "USER"})
    public List<Friendship> getFriendRequestBeSentByUserList() {
    	final User user = auth.getCurrentUser();
        if (user == null)
            return null;
        else {

            return em.createQuery(
    				"SELECT f FROM Friendship f WHERE f.user.login = :userId "
    						+ "AND f.friendshipState = 'PENDING' ", Friendship.class)
    						.setParameter("userId", user.getLogin()).getResultList();
        }
    }

    /**
     * Remove {@link Friendship} from database
     * @param friendLogin The login's {@link User}
     *
     */
    @RolesAllowed({"ADMIN", "USER"})
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeFriendship(final String friendLogin) {
		final User user = auth.getCurrentUser();
		Friendship friendship = em
				.createQuery(
						"SELECT f FROM Friendship f WHERE "
						+ "((f.user.login = :userId "
						+ " AND f.friend.login = :friendId)"
						+ " OR (f.user.login = :friendId "
						+ " AND f.friend.login = :userId))"
						+ " AND f.friendshipState = 'ACCEPTED'",
									Friendship.class)
						.setParameter("friendId", friendLogin)
						.setParameter("userId", user.getLogin())
									.getSingleResult();

		if (friendship != null) {
			em.remove(friendship);
		} else {
			throw new EJBTransactionRolledbackException("Friendship cannot be null");
		}
	}

    /**
     * Cancel {@link Friendship} by user
     * @param friendLogin {@link User}
     */
    @RolesAllowed({"ADMIN", "USER"})
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelFriendship(final String friendLogin) {
    	isTrue(nonNull(friendLogin), "friend cannot be null");
    	final User user = auth.getCurrentUser();
		Friendship friendship = em
				.createQuery(
						"SELECT f FROM Friendship f WHERE f.user.login = :userId "
								+ "AND f.friendshipState = 'ACCEPTED' "
								+ "AND f.friend.login = :friendId",
						Friendship.class)
						.setParameter("friendId", friendLogin)
						.setParameter("userId", user.getLogin())
						.getSingleResult();
		if(friendship != null){
			friendship.setFriendshipState(FriendshipState.CANCELLED);
			em.merge(friendship);

		} else {
			throw new EJBTransactionRolledbackException("Friendship cannot be null");
		}

	}

    /**
     * Search the user {@link User} by full name
     * @param name
     *             Value to be found in the complete name or user's login.
     * @return The {@link List} with all the {@link User}
     */
    @RolesAllowed({"ADMIN", "USER"})
	public List<User> searchUser(final String name) {
		isTrue(nonNull(name), "Search query cannot be null");
		final User user = auth.getCurrentUser();
		if (user == null) {
			return null;
		}

		TypedQuery<User> users = em
				.createQuery("SELECT u FROM User u WHERE " + "( LOWER(u.completeName) LIKE :name"
						+ " OR LOWER(u.login) LIKE :name) " + "", User.class)
				.setParameter("name", "%" + name.toLowerCase() + "%");

		return users.getResultList();

	}

    /**
     * Get the friend {@link User} by the current {@link User}.
     *
     * @param loginFriend The friend of user
     * @return the {@link User}.
     */
    @RolesAllowed({"ADMIN", "USER"})
	public Friendship getFriend(final String loginFriend) {
		isTrue(nonNull(loginFriend), "login name query cannot be null");
		final User user = auth.getCurrentUser();
		 if (user == null)
	            return null;
	        else {
	        	return em
	        			.createQuery("SELECT f FROM Friendship f where  f.friend.login =:loginFriend AND f.user.login =:login", Friendship.class)
	        			.setParameter("login", user.getLogin()).setParameter("loginFriend", loginFriend)
	        			.getSingleResult();
	        	}
	}
    
    /**
     * Get the friend {@link User} by the current {@link User}.
     *
     * @return the {@link User}.
     */
    @RolesAllowed({"ADMIN", "USER"})
	public List<User> getFriend() {
		final User user = auth.getCurrentUser();
		 if (user == null)
	            return null;
	        else {
	        	return em
	        			.createQuery("SELECT f.user FROM Friendship f where f.user.login =:login OR f.friend.login =:login ", User.class)
	        			.setParameter("login", user.getLogin()).getResultList();
	        	}
	}

    /**
     * Get the friend {@link User} by the current {@link User}.
     *
     * @param loginFriend The friend of user {@link User}
     * @return the {@link Friendship}.
     */
    @RolesAllowed({"ADMIN", "USER"})
	public FriendshipState getFriendshipState(final String loginFriend) {
		isTrue(nonNull(loginFriend), "Search query cannot be null");
		final User user = auth.getCurrentUser();
		 if (user == null)
	            return null;
	        else {
	        	try {
					return em
							.createQuery(
									"SELECT f.friendshipState FROM Friendship f where"
											+ " f.friend.login =:loginFriend AND f.user.login =:login "
											+ " OR f.friend.login =:login AND f.user.login =:loginFriend",
									FriendshipState.class)
							.setParameter("login", user.getLogin()).setParameter("loginFriend", loginFriend)
							.getSingleResult();
				 } catch (final NoResultException e) {
			            return null;
			            
				 }
	        	
	        }
		 
    }




}
