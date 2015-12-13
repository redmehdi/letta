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

	private ExternalContext context = FacesContext.getCurrentInstance()
			.getExternalContext();

	private boolean error = false;
	private String errorMessage;

	private String login;
	private String password;

	/**
	 * Login petition. If login or password dont match, shows a message.
	 *
	 * @throws IOException
	 *             if an input/output error occurs
	 */
	public void doLogin() throws IOException {
		try {
			HttpServletRequest request = (HttpServletRequest) context
					.getRequest();
			request.login(this.getLogin(), this.getPassword());
			this.error = false;
			context.redirect("index.xhtml?login=true");
		} catch (ServletException e) {
			this.error = true;
			this.errorMessage = "Login or password don't match";
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
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
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

	/**
	 * Checks if the current user is the "anonymous" user.
	 * 
	 * @return {@code true} if the current user is the "anonymous" user.
	 *         {@code false} otherwise.
	 */
	public boolean isAnonymous() {
		return "anonymous".equals(this.getCurrentUser().getName());
	}

	/**
	 * Forces a page redirect to the index if the current user is the anonymous
	 * user.
	 * 
	 * @throws IOException
	 *             if an error happens while redirecting.
	 */
	public void redirectIfAnonymous() throws IOException {
		if (this.isAnonymous()) {
			redirectToIndex();
		}
	}

	/**
	 * Forces a page redirect to the index if the current user is not an
	 * anonymous user.
	 * 
	 * @throws IOException
	 *             if an error happens while redirecting.
	 */
	public void redirectIfNotAnonymous() throws IOException {
		if (!this.isAnonymous()) {
			redirectToIndex();
		}
	}

	private void redirectToIndex() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("index.xhtml");
	}
}
