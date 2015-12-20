package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Objects.nonNull;

import java.util.UUID;

import javax.jws.soap.SOAPBinding.Use;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

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
	@Column(length = 36, nullable = false)
	private String uuid;

	@Column(length = 20, nullable = false, unique = true)
	private String login;

	@Column(length = 32, nullable = false)
	private String password;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

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
	
	@Lob
	@Column
	private byte[] image;
	
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
	 *            the data of the user to be registered.
	 */
	public Registration(User user) {
		this(user, UUID.randomUUID().toString());
	}

	/**
	 * Constructs a new instance of {@link Registration}. This constructor
	 * should only be used for testing purposes.
	 *
	 * @param user
	 *            the data of the user to be registered.
	 * @param uuid
	 *            the UUID of the registration.
	 */
	Registration(User user, String uuid) {
		this.uuid = uuid;
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.completeName = user.getCompleteName();
		this.description = user.getDescription();
		this.fbUrl = user.getFbUrl();
		this.twUrl = user.getTwUrl();
		this.personalUrl = user.getPersonalUrl();
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
	 * Returns the complete name of the user to be registered.
	 * 
	 * @return the complete name of the user to be registered.
	 * @see User#getCompleteName()
	 */
	public String getCompleteName() {
		return completeName;
	}

	/**
	 * Returns the description of the user to be registered.
	 * 
	 * @return the cdescription of the user to be registered.
	 * @see User#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the url to the facebook profile of the user to be registered.
	 * 
	 * @return the url to the facebook profile of the user to be registered.
	 * @see User#getFbUrl()
	 */
	public String getFbUrl() {
		return fbUrl;
	}

	/**
	 * Returns the url to the twitter profile of the user to be registered.
	 * 
	 * @return the url to the twitter profile of the user to be registered.
	 * @see User#getTwUrl()
	 */
	public String getTwUrl() {
		return twUrl;
	}

	/**
	 * Returns the url to the personal website or blog profile of the user to be registered.
	 * 
	 * @return the url to the personal website or blog profile of the user to be registered.
	 * @see User#getPersonalUrl()
	 */
	public String getPersonalUrl() {
		return personalUrl;
	}
	
	/**
	 * Returns the image of the profile of the user to be registered.
	 * 
	 * @return the image of the profile of the user to be registered.
	 * @see User#getImage()
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Returns the registered user.
	 * 
	 * @return the registered user.
	 */
	public User getUser() {
		return new User(this.login, this.password, this.email, this.role, this.completeName, this.description, this.fbUrl, this.twUrl, this.personalUrl, this.image);
	}

	@Override
	public final int hashCode() {
		return ((uuid == null) ? 0 : uuid.toLowerCase().hashCode());
	}

	@Override
	public final boolean equals(final Object that) {
		return this == that || nonNull(that) && that instanceof Registration
				&& this.uuid.toLowerCase().equals(((Registration) that).uuid.toLowerCase());
	}
	
}
