package es.uvigo.esei.dgss.letta.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * A {@linkplain Registration} is a temporary entity created when a user is
 * registered in the system. Each {@linkplain} Registration} has an unique
 * identifier that should be used to confirm the users registration.
 * 
 * @author Miguel Reboiro Jato
 */
@Entity
public class Registration {
	@Id
	@Column(length = 32, nullable = false)
	private String uuid;

	@Column(length = 20)
	private String login;

	@Column(length = 32, nullable = false)
	private String password;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * Constructs a new instance of {@link Registration}. This constructor is
	 * required by the JPA framework.
	 */
	Registration() {
	}

	/**
	 * Constructs a new instance of {@link Registration}. This instance contains
	 * a randomly generated unique identifier.
	 *
	 * @param user
	 *            the data of the user registered.
	 */
	public Registration(User user) {
		this.uuid = UUID.randomUUID().toString();
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.role = user.getRole();
	}

	/**
	 * Returns the unique identifiers of this registration.
	 * 
	 * @return the unique identifiers of this registration.
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Returns the login of the user to be registered.
	 * 
	 * @return the login of the user to be registered.
	 * @see User#getLogin()
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Returns the MD5 of the password of the user to be registered.
	 * 
	 * @return the MD5 of the password of the user to be registered.
	 * @see User#getPassword()
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the email of the user to be registered.
	 * 
	 * @return the email of the user to be registered.
	 * @see User#getEmail()
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the role of the user to be registered.
	 * 
	 * @return the role of the user to be registered.
	 * @see User#getRole()
	 */
	public Role getRole() {
		return role;
	}
	
	/**
	 * Returns the registered user.
	 * 
	 * @return the registered user.
	 */
	public User getUser() {
		return new User(this.login, this.email, this.password, this.role);
	}
}
