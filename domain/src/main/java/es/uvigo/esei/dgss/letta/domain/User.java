package es.uvigo.esei.dgss.letta.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private String login;
	private String password;
	private String name;
	private String email;

	// attribute to determinate if the user has received and confirmed his
	// registration mail
	private boolean isConfirmed = false;

	User() {
	}

	public User(String login, String password, String name, String email) {
		this.login = login;
		this.password = password;
		this.name = name;
		this.email = email;
		this.isConfirmed = false;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

}
