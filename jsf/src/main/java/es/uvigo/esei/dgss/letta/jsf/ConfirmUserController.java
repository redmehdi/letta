package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * JSF controller to confirmation the register process.
 *
 * @author abmiguez and bcgonzalez3
 *
 */
@RequestScoped
@ManagedBean(name = "confirmController")
public class ConfirmUserController {
	@Inject
	private UserEJB userEJB;

	/**
	 * Confirm a user registration.
	 *
	 * @throws IOException if an error happens on the redirection. 
	 */
	public void doConfirm() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		final String uuid = context.getRequestParameterMap().getOrDefault("uuid", null);
		
		final boolean confirmed = uuid != null && userEJB.userConfirmation(uuid);
		
		context.redirect("index.xhtml?confirmed=" + confirmed);
	}
}
