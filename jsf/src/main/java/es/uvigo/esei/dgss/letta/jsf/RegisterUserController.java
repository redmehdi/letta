package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.Part;
import javax.validation.constraints.Size;

import org.apache.commons.io.IOUtils;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;

/**
 * {@linkplain RegisterUserController} is a JSF controller to perform the
 * registration process.
 *
 * @author abmiguez
 * @author bcgonzalez3
 *
 */
@RequestScoped
@ManagedBean(name = "registerController")
public class RegisterUserController {

	@Inject
	private UserEJB userEJB;

	private ExternalContext context = FacesContext.getCurrentInstance()
			.getExternalContext();

	private boolean error = false;
	private String errorMessage;

	private String login;
	private String email;
	private String password;
	private String repassword;
	private Part image;
	private String completeName;
	private String description;
	private String fbUrl;
	private String twUrl;
	private String personalUrl;

	/**
	 * Register a user. If login or email are duplicated, shows a message.
	 * 
	 * @throws IOException
	 *             if an input/output error occurs
	 *
	 */
	public void doRegister() throws IOException {
		if (!password.equals(repassword)) {
			error = true;
			errorMessage = "Passwords do not match.";
		} else {
			byte[] imageRaw = null;
			if(image!=null){
				InputStream imageInputStream=image.getInputStream();
				imageRaw =IOUtils.toByteArray(imageInputStream);
			}
	        final Registration registration = new Registration(
					new User(login, password, email, completeName, description, fbUrl, twUrl, personalUrl, false, imageRaw));

			try {
				userEJB.registerUser(registration);
				error = false;
				context.redirect("registrationSuccess.xhtml");
			} catch (final LoginDuplicateException e) {
				error = true;
				errorMessage = "Login already exists";
			} catch (EmailDuplicateException e) {
				error = true;
				errorMessage = "Email already exists";
			} catch (MessagingException e) {
				error = true;
				errorMessage = "An error happened while sending the confirmation email. "
						+ "Please, try again in a few minutes or contact with the page administrators";
			}
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

	/**
	 * Getter method of repassword global variable.
	 *
	 * @return repassword global variable.
	 */
	public String getRepassword() {
		return repassword;
	}

	/**
	 * Setter method of repassword variable.
	 *
	 * @param repassword
	 *            repassword global variable.
	 */
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	/**
	 * Getter method of image global variable.
	 *
	 * @return image global variable.
	 */
	public Part getImage() {
		return image;
	}

	/**
	 * Setter method of image variable.
	 *
	 * @param image
	 *            image global variable.
	 */
	public void setImage(Part image) {
		this.image = image;
	}

	/**
	 * Getter method of completeName global variable.
	 *
	 * @return completeName global variable.
	 */
	public String getCompleteName() {
		return completeName;
	}

	/**
	 * Setter method of completeName variable.
	 *
	 * @param completeName
	 *            completeName global variable.
	 */
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	/**
	 * Getter method of description global variable.
	 *
	 * @return description global variable.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter method of description variable.
	 *
	 * @param description
	 *            description global variable.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter method of fbUrl global variable.
	 *
	 * @return fbUrl global variable.
	 */
	public String getFbUrl() {
		return fbUrl;
	}

	/**
	 * Setter method of fbUrl variable.
	 *
	 * @param fbUrl
	 *            fbUrl global variable.
	 */
	public void setFbUrl(String fbUrl) {
		this.fbUrl = fbUrl;
	}

	/**
	 * Getter method of twUrl global variable.
	 *
	 * @return twUrl global variable.
	 */
	public String getTwUrl() {
		return twUrl;
	}

	/**
	 * Setter method of twUrl variable.
	 *
	 * @param twUrl
	 *            twUrl global variable.
	 */
	public void setTwUrl(String twUrl) {
		this.twUrl = twUrl;
	}

	/**
	 * Getter method of personalUrl global variable.
	 *
	 * @return personalUrl global variable.
	 */
	public String getPersonalUrl() {
		return personalUrl;
	}

	/**
	 * Setter method of personalUrl variable.
	 *
	 * @param personalUrl
	 *            personalUrl global variable.
	 */
	public void setPersonalUrl(String personalUrl) {
		this.personalUrl = personalUrl;
	}
	
}
