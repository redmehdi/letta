package es.uvigo.esei.dgss.letta.domain.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private String body;

	private boolean readed = false;

	@XmlTransient
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "UserNotifications", 
		joinColumns = {	@JoinColumn(name = "message_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "user_login", referencedColumnName = "login") })
	private Set<User> usersToNotify = new LinkedHashSet<>();

	/**
	 * Returns the title of the {@link Notification}
	 * 
	 * @return the title of the {@link Notification}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the {@link Notification}
	 * 
	 * @param title
	 *            of the {@link Notification}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the body of the {@link Notification}
	 * 
	 * @return the body of the {@link Notification}
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Sets the body of the {@link Notification}
	 * 
	 * @param body
	 *            of the {@link Notification}
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Returns the readed value of the {@link Notification}
	 * 
	 * @return the readed value of the {@link Notification}
	 */
	public boolean isReaded() {
		return readed;
	}

	/**
	 * Sets the readed value of the {@link Notification}
	 * 
	 * @param readed
	 *            of the {@link Notification}
	 */
	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	/**
	 * Returns the {@link User} who will get the {@link Notification}
	 * 
	 * @return the {@link User} who will get the {@link Notification}
	 */
	public Set<User> getUsersToNotify() {
		return usersToNotify;
	}

	/**
	 * Sets the {@link User} who will get the {@link Notification}
	 * 
	 * @param usersToNotify
	 *            the {@link User} who will get the {@link Notification}
	 */
	public void setUsersToNotify(Set<User> usersToNotify) {
		this.usersToNotify = usersToNotify;
	}

}
