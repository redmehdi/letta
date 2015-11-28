package es.uvigo.esei.dgss.letta.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.constraints.Size;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.jsf.util.JSFPagePathUtils;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;

/**
 * JSF controller to registration process.
 *
 * @author abmiguez and bcgonzalez3
 *
 */
@RequestScoped
@ManagedBean(name = "registerController")
public class RegisterUserController {

	@Inject
	private UserEJB userEJB;

    @Inject
    private JSFPagePathUtils path;

	private boolean error = false;
	private String errorMessage;

	private String login;
	private String email;
	private String password;

	/**
	 * Register a user. If login or email are duplicated, shows a message.
	 *
	 * @return Redirect to index if login was fine or shows error message in
	 *         other case.
	 */
	public String doRegister() {
		final Registration registration =
			new Registration(new User(login, password, email));

		try{
			userEJB.registerUser(registration);
			error = false;
			return path.redirectToPage("index.xhtml");
		} catch(final LoginDuplicateException e) {
			error = true;
			errorMessage = "Login already exists";
			return path.getCurrentPage();
		} catch (EmailDuplicateException e) {
			error = true;
			errorMessage = "Email already exists";
			return path.getCurrentPage();
		} catch (MessagingException e) {
			error = true;
			errorMessage = "An error happened while sending the confirmation email. "
				+ "Please, try again in a few minutes or contact with the page administrators";
			return path.getCurrentPage();
		}
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
	public void setLogin(final String login) {
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
	public void setEmail(final String email) {
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
	public void setPassword(final String password) {
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
