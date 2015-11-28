package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.security.Principal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import es.uvigo.esei.dgss.letta.jsf.util.JSFPagePathUtils;

/**
 * {@linkplain LoginUserController} is a JSF controller to perform login
 * actions.
 *
 * @author abmiguez
 * @author bcgonzalez3
 *
 */
@ManagedBean(name = "loginController")
@RequestScoped
public class LoginUserController {

	@Inject
	private Principal currentUserPrincipal;

	@Inject
	private HttpServletRequest request;

	@Inject
	private JSFPagePathUtils path;

	private boolean error = false;
	private String errorMessage;

	private String login;
	private String password;

	/**
	 * Login petition. If login or password dont match, shows a message.
	 *
	 * @return Redirect to index if login and password match or shows error
	 *         message in other case.
	 */
	public String doLogin() {
		try {
			request.login(this.getLogin(), this.getPassword());
			this.error = false;
			return path.redirectToPage("index.xhtml");
		} catch (ServletException e) {
			this.error = true;
			this.errorMessage = "Login or password don't match";
			return path.getCurrentPage();
		}
	}

	/**
	 * Logout petition. Redirect to index closing the session.
	 *
	 * @throws ServletException
	 *             if an error happens while logging out the user.
	 * @throws IOException
	 *             if an error happens on the redirection.
	 */
	public void doLogout() throws ServletException, IOException {
		final ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		request.logout();
		context.redirect("index.xhtml?logout=true");
	}

	/**
	 * Getter method of currentUserPrincipal variable.
	 *
	 * @return currentUserPrincipal global variable.
	 */
	public Principal getCurrentUser() {
		return this.currentUserPrincipal;
	}

	/**
	 * Getter method of login variable.
	 *
	 * @return login global variable.
	 */
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
	 * Getter method of password variable.
	 *
	 * @return password global variable.
	 */
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
