package es.uvigo.esei.dgss.letta.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public class ConfirmUserController implements JSFController {

	@Inject
	private UserEJB userEJB;

	/**
	 * Confirm a user registration.
	 *
	 * @return Redirect to index.
	 */
	public String doConfirm() {
		userEJB.userConfirmation(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("uuid"));
		return redirectTo("index.xhtml");
	}

}
