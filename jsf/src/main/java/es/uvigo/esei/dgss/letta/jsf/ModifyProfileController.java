package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;

/**
 * {@linkplain ModifyProfileController} is a JSF controller to modify user's
 * profile
 * 
 * @author Adolfo Alvarez Lopez
 */
@ViewScoped
@ManagedBean(name = "modifyProfileController")
public class ModifyProfileController {
	@Inject
	private UserEJB userEJB;

	@Inject
	private UserAuthorizationEJB auth;

	private boolean error = false;
	private String errorMessage;

	private String email;
	private String repEmail;
	private String password;
	private String repassword;
	private Part image;
	private String completeName;
	private String description;
	private String fbUrl;
	private String twUrl;
	private String personalUrl;
	private boolean notifications;
	private User user;

	@PostConstruct
	public void init() throws IOException {
		user = auth.getCurrentUser();
		email = user.getEmail();
		completeName = user.getCompleteName();
		description = user.getDescription();
		fbUrl = user.getFbUrl();
		twUrl = user.getTwUrl();
		notifications = user.isNotifications();
		personalUrl = user.getPersonalUrl();
	}

	/**
	 * Modify an {@link User}
	 * 
	 * @throws IOException
	 *             if an input/output error occurs
	 */
	public void doModify() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (!password.equals(repassword)) {
			error = true;
			errorMessage = "Passwords do not match.";
		} else {
			if (!email.equals(user.getEmail()) && !email.equals(repEmail)) {
				error = true;
				errorMessage = "Emails do not match";
			} else {
				byte[] imageRaw = null;
				if (image != null) {
					InputStream imageInputStream = image.getInputStream();
					imageRaw = IOUtils.toByteArray(imageInputStream);
					user.setImage(imageRaw);
				}
				user.setEmail(email);
				if (!password.equals("")) {
					user.changePassword(password);
				}
				user.setCompleteName(completeName);
				user.setDescription(description);
				user.setNotifications(notifications);

				try {
					userEJB.modifyProfile(user);
					context.redirect("userModified.xhtml");
				} catch (EmailDuplicateException e) {
					error = true;
					errorMessage = "Email already exists";
				}

			}
		}
	}

	/**
	 * Getter method of email variable.
	 *
	 * @return email global variable.
	 */
	// @Size(min = 1, max = 100, message = "Email must be between 1 and 100
	// characters")
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
	 * Getter method of repEmail variable.
	 *
	 * @return email global variable.
	 */
	public String getRepEmail() {
		return repEmail;
	}

	/**
	 * Setter method of repEmail variable.
	 *
	 * @param repEmail
	 *            email global variable.
	 */
	public void setRepEmail(final String repEmail) {
		this.repEmail = repEmail;
	}

	/**
	 * Getter method of password variable.
	 *
	 * @return password global variable.
	 */
	// @Size(min = 8, max = 32, message = "Password must have 8 characters or
	// more")
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

	/**
	 * Getter method of notifications global variable
	 * 
	 * @return notifications global variable
	 */
	public boolean isNotifications() {
		return notifications;
	}

	/**
	 * Setter method of notifications global variable
	 * 
	 * @param notifications
	 *            global variable
	 */
	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}
	

}
