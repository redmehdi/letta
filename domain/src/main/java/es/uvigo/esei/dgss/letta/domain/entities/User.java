package es.uvigo.esei.dgss.letta.domain.entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;

/**
 * An entity that represents an user of the application. User's represented by
 * this entity have the USER role.
 *
 * @author Miguel Reboiro Jato
 * @author Adolfo Álvarez López
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String MD5_REGEX = "[a-zA-Z0-9]{32}";
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

	@Id
	@Column(length = 20)
	private String login;

	@XmlTransient
	@Column(length = 32, nullable = false)
	private String password;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	//optional data

	@Column(length = 30)
	private String completeName;

	@Column(length = 1000)
	private String description;

	@Column(length = 50)
	private String fbUrl;

	@Column(length = 50)
	private String twUrl;

	@Column(length = 50)
	private String personalUrl;

	@Column(nullable = false)
	private boolean notifications;

	@Lob
	@Column
	private byte[] image;


	@Column(length = 20)
	private String city;


	/**
	 * Constructs a new instance of {@link User}. This constructor is required
	 * by the JPA framework.
	 */
	User() {
	}

	/**
	 * Constructs a new instance of {@link User} with the USER role.
	 *
	 * @param login
	 *            the login of the new user. This login must be unique in the
	 *            system.
	 * @param password
	 *            the raw password of the user.
	 * @param email
	 *            the email of the user.
	 */
	public User(final String login, final String password, final String email) {
		this.setLogin(login);
		this.changePassword(password);
		this.setEmail(email);
		this.completeName = null;
		this.description = null;
		this.fbUrl = null;
		this.twUrl = null;
		this.personalUrl = null;
		this.image = null;
		this.notifications = false;

		this.role = Role.USER;
	}

	public User(final String login, final String password, final String email,final String city) {
		this.setLogin(login);
		this.changePassword(password);
		this.setEmail(email);
		this.completeName = null;
		this.description = null;
		this.fbUrl = null;
		this.twUrl = null;
		this.personalUrl = null;
		this.image = null;
		this.notifications = false;

		this.role = Role.USER;
	}





	public User(final String login, final String password, final String email,final boolean isAdmin) {
		this.setLogin(login);
		this.changePassword(password);
		this.setEmail(email);
		this.completeName = null;
		this.description = null;
		this.fbUrl = null;
		this.twUrl = null;
		this.personalUrl = null;
		this.image = null;
		this.notifications = false;
		if(isAdmin)
			this.role = Role.ADMIN;
		else
			this.role=Role.USER;
	}

	/**
	 * Constructs a new instance of {@link User} with the USER role.
	 *
	 * @param login
	 *            the login of the new user. This login must be unique in the
	 *            system.
	 * @param password
	 *            the raw password of the user.
	 * @param email
	 *            the email of the user.
	 * @param completeName
	 *            the complete name of the user.
	 * @param description
	 *            the description of the user.
	 * @param fbUrl
	 *            the link to the user's Facebook page.
	 * @param twUrl
	 *            the link to the user's Twitter page.
	 * @param personalUrl
	 *            the link to the user's personal web or blog.
	 * @param notifications
	 *            if the {@link User} wants {@link Notification}
	 * @param image
	 *            the image of the user's profile.
	 * @param city
	 *            the city of the user.
	 */
	public User(final String login, final String password, final String email, final String completeName,
			final String description, final String fbUrl, final String twUrl, final String personalUrl,
			final boolean notifications, final byte[] image,final String city) {
		this.setLogin(login);
		this.changePassword(password);
		this.setEmail(email);
		this.completeName = completeName;
		this.description = description;
		this.fbUrl = fbUrl;
		this.twUrl = twUrl;
		this.personalUrl = personalUrl;
		this.notifications = notifications;
		this.image = image;
		this.city=city;
		this.role = Role.USER;
	}

	/**
	 * Constructs a new instance of {@link User} with the USER role. This
	 * constructor is expected to be used only by the {@code Registration}
	 * class.
	 *
	 * @param login
	 *            the login of the new user. This login must be unique in the
	 *            system.
	 * @param password
	 *            the MD5 password of the user.
	 * @param email
	 *            the email of the user.
	 * @param role
	 *            the role of the user.
	 */
	User(final String login, final String password, final String email,
			final Role role) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.completeName = null;
		this.description = null;
		this.fbUrl = null;
		this.twUrl = null;
		this.personalUrl = null;
		this.notifications = false;
		this.image = null;
		this.role = role;
	}

	/**
	 * Constructs a new instance of {@link User} with the USER role. This
	 * constructor is expected to be used only by the {@code Registration}
	 * class.
	 *
	 * @param login
	 *            the login of the new user. This login must be unique in the
	 *            system.
	 * @param password
	 *            the MD5 password of the user.
	 * @param email
	 *            the email of the user.
	 * @param role
	 *            the role of the user.
	 * @param completeName
	 *            the complete name of the user.
	 * @param description
	 *            the description of the user.
	 * @param fbUrl
	 *            the link to the user's Facebook page.
	 * @param twUrl
	 *            the link to the user's Twitter page.
	 * @param personalUrl
	 *            the link to the user's personal web or blog.
	 * @param notifications
	 *            if the {@link User} wants {@link Notification}
	 * @param image
	 *            the image of the user's profile.
	 */
	User(final String login, final String password, final String email, final Role role, final String completeName,
			final String description, final String fbUrl, final String twUrl, final String personalUrl,
			final boolean notifications, final byte[] image,final String city) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.completeName = completeName;
		this.description = description;
		this.fbUrl = fbUrl;
		this.twUrl = twUrl;
		this.personalUrl = personalUrl;
		this.notifications = notifications;
		this.image = image;
		this.role = role;
		this.city=city;
	}

	/**
	 * Returns the login of this user.
	 *
	 * @return the login of this user.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Sets the login of this user.
	 *
	 * @param login
	 *            the login that identifies the user. This parameter must be a
	 *            non empty and non {@code null} string with a maximum length of
	 *            100 chars.
	 * @throws NullPointerException
	 *             if {@code null} is passed as parameter.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setLogin(final String login) {
		requireNonNull(login, "login can't be null");
		inclusiveBetween(1, 100, login.length(),
				"login must have a length between 1 and 100");

		this.login = login;
	}

	/**
	 * Returns the MD5 of the user's password. Capital letters are used in the
	 * returned string.
	 *
	 * @return the MD5 of the user's password. Capital letters are used in the
	 *         returned string.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the MD5 password of the user. The MD5 string is stored with
	 * lowercase letters.
	 *
	 * @param password
	 *            the MD5 password of the user. This parameter must be a non
	 *            {@code null} MD5 string.
	 * @throws NullPointerException
	 *             if {@code null} is passed as parameter.
	 * @throws IllegalArgumentException
	 *             if the string passed is not a valid MD5 string.
	 */
	public void setPassword(final String password) {
		requireNonNull(password, "password can't be null");
		matchesPattern(password, MD5_REGEX,
				"password must be a valid MD5 string");

		this.password = password.toLowerCase();
	}

	/**
	 * Changes the password of the user. This method receives the rawvalue of
	 * the password and stores it in MD5 format.
	 *
	 * @param password
	 *            the raw password of the user. This parameter must be a non
	 *            {@code null} string with a minimum length of 6 chars.
	 *
	 * @throws NullPointerException
	 *             if the {@code password} is {@code null}.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void changePassword(final String password) {
		requireNonNull(password, "password can't be null");
		if (password.length() < 6)
			throw new IllegalArgumentException(
					"password can't be shorter than 6");

		try {
			final MessageDigest digester = MessageDigest.getInstance("MD5");
			final HexBinaryAdapter adapter = new HexBinaryAdapter();

			this.password = adapter
					.marshal(digester.digest(password.getBytes()))
					.toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not found", e);
		}
	}

	/**
	 * Returns the email of the user.
	 *
	 * @return the email of the user.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user.
	 *
	 * @param email
	 *            the email of the user.
	 * @throws IllegalArgumentException
	 *             if the email provided is not a valid email.
	 */
	public void setEmail(final String email) {
		matchesPattern(email, EMAIL_REGEX, "invalid email");

		this.email = email;
	}

	/**
	 * Returns the role of the user.
	 *
	 * @return the role of the user.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Updates the role of the user.
	 *
	 * @param role Thew new {@link Role} to be set to this user.
	 *
	 * @throws NullPointerException If the given role is null.
	 */
	public void setRole(final Role role) throws NullPointerException {
        this.role = requireNonNull(role);
    }

	/**
	 * Sets the complete name of the user.
	 *
	 * @param completeName
	 *            the complete name of the user. This parameter can be an empty
	 *            or a {@code null} string and the maximum length is 30 chars.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setCompleteName(final String completeName) {
		if (completeName != null)
            inclusiveBetween(0, 30, completeName.length(), "complete name must have a length between 0 and 30");
		this.completeName = completeName;
	}

	/**
	 * Returns the complete name of the user.
	 *
	 * @return the complete name of the user.
	 */
	public String getCompleteName() {
		return completeName;
	}

	/**
	 * Sets the description of the user.
	 *
	 * @param description
	 *            the description of the user. This parameter can be an empty
	 *            or a {@code null} string and the maximum length is 1000 chars.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setDescription(final String description) {
		if (description != null)
            inclusiveBetween(0, 1000, description.length(), "description must have a length between 0 and 1000");
		this.description = description;
	}

	/**
	 * Returns the description of the user.
	 *
	 * @return the description of the user.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the url to the facebook profile of the user.
	 *
	 * @param fbUrl
	 *            the url to the facebook profile of the user.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setFbUrl(final String fbUrl) {
		if (fbUrl != null)
            inclusiveBetween(0, 50, fbUrl.length(), "fbUrl must have a length between 1 and 50");
		this.fbUrl = fbUrl;
	}

	/**
	 * Returns the url to the facebook profile of the user.
	 *
	 * @return the url to the facebook profile of the user.
	 */
	public String getFbUrl() {
		return fbUrl;
	}

	/**
	 * Sets the url to the twitter profile of the user.
	 *
	 * @param twUrl
	 *            the url to the twitter profile of the user.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setTwUrl(final String twUrl) {
		if (twUrl != null)
            inclusiveBetween(0, 50, twUrl.length(), "twUrl must have a length between 1 and 50");
		this.twUrl = twUrl;
	}

	/**
	 * Returns the url to the twitter profile of the user.
	 *
	 * @return the url to the twitter profile of the user.
	 */
	public String getTwUrl() {
		return twUrl;
	}

	/**
	 * Sets the url to the personal page or blog of the user.
	 *
	 * @param personalUrl
	 *            the url to the personal page or blog of the user.
	 * @throws IllegalArgumentException
	 *             if the length of the string passed is not valid.
	 */
	public void setPersonalUrl(final String personalUrl) {
		if (personalUrl != null)
            inclusiveBetween(0, 50, personalUrl.length(), "personalUrl must have a length between 1 and 50");
		this.personalUrl = personalUrl;
	}

	/**
	 * Returns the url to the personal page or blog of the user.
	 *
	 * @return the url to the personal page or blog of the user.
	 */
	public String getPersonalUrl() {
		return personalUrl;
	}

	/**
	 * Sets the image of the user's profile.
	 *
	 * @param image
	 *            the image of the user's profile.
	 */
	public void setImage(final byte[] image) {
		this.image = image;
	}

	/**
	 * Returns the image of the user's profile.
	 *
	 * @return the image of the user's profile.
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Returns {@code true} if the {@link User} wants to recieve
	 * {@link Notification} {@code null} otherwise
	 *
	 * @return {@code true} if the {@link User} wants to recieve
	 *         {@link Notification} {@code null} otherwise
	 */
	public boolean isNotifications() {
		return notifications;
	}

	/**
	 * Sets the value of the variable notifications
	 *
	 * @param notifications
	 *            the value of the variable
	 */
	public void setNotifications(final boolean notifications) {
		this.notifications = notifications;
	}

	/**
	 * Gets the value of the variable city
	 *
	 * @return the value of the variable city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the value of the variable city
	 *
	 * @param city
	 *            the value of the variable
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	@Override
    public final int hashCode() {
        return isNull(login) ? 0 : login.toLowerCase().hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) return true;
        if (isNull(obj) || !(obj instanceof User)) return false;

        final User that = (User) obj;
        return isNull(this.login)
             ? isNull(that.login)
             : this.login.equalsIgnoreCase(that.login);
    }

}
