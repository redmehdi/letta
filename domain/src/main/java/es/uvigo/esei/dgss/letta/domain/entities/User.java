package es.uvigo.esei.dgss.letta.domain.entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import static java.util.Objects.requireNonNull;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;

/**
 * An entity that represents an user of the application. User's represented by
 * this entity have the USER role.
 *
 * @author Miguel Reboiro Jato
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
	User(final String login, final String password, final String email, final Role role) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.role = role;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (email == null ? 0 : email.hashCode());
		result = prime * result + (login == null ? 0 : login.hashCode());
		result = prime * result
				+ (password == null ? 0 : password.hashCode());
		result = prime * result + (role == null ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		return true;
	}
}
