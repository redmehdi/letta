package es.uvigo.esei.dgss.letta.jsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Size;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.exceptions.LoginDuplicateException;

/**
 * JSF controller to registration process.
 * 
 * @author abmiguez and bcgonzalez3
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "registerController")
@RequestScoped
public class RegisterUserController implements Serializable {
	@Inject
	private UserEJB userEJB;

	private boolean error = false;
	private String errorMessage;

	private String login;
	private String email;
	private String password;

	private User registration;

	/**
	 * Register a user. If login or email are duplicated, shows a message.
	 * 
	 * @return Redirect to index if login was fine or shows error message in
	 *         other case.
	 */
	public String doRegister() {
		registration = new User(login, password, email);		
		try{
			userEJB.registerUser(registration);
			this.error = false;
			return redirectTo("index.xhtml");
		}catch(final LoginDuplicateException e){
			this.error = true;
			this.errorMessage = "Login already exists";
			return this.getViewId();
		} catch (EmailDuplicateException e) {
			this.error = true;
			this.errorMessage = "Email already exists";
			return this.getViewId();
		}
	}

	/**
	 * Returns the id of the root view.
	 * 
	 * @return id of the root view
	 */
	private String getViewId() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}

	/**
	 * Returns a url with redirect faces param set to true.
	 * 
	 * @param url
	 *            indicates the url to redirect.
	 * @return url with redirect faces param set to true.
	 */
	private String redirectTo(String url) {
		return url + "?faces-redirect=true";
	}

	/**
	 * Getter method of login variable.
	 * 
	 * @return login global variable.
	 */
	@Size(min = 1, max = 20, message = "Login must be between 1 and 20 characters")
	public String getLogin() {
		return login;
	}

	/**
	 * Setter method of login variable.
	 * 
	 * @param login
	 *            login global variable.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Getter method of email variable.
	 * 
	 * @return email global variable.
	 */
	@Size(min = 1, max = 100, message = "Email must be between 1 and 100 characters")
	public String getEmail() {
		return email;
	}

	/**
	 * Setter method of email variable.
	 * 
	 * @param email
	 *            email global variable.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter method of password variable.
	 * 
	 * @return password global variable.
	 */
	@Size(min = 8, max = 32, message = "Password must have 8 characters or more")
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method of password variable.
	 * 
	 * @param password
	 *            password global variable.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter method of error global variable.
	 * 
	 * @return error global variable.
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Getter method of errorMessage global variable.
	 * 
	 * @return errorMessage global variable.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
