package es.uvigo.esei.dgss.letta.jsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * JSF controller to confirmation the register process.
 * 
 * @author abmiguez and bcgonzalez3
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "confirmController")
@RequestScoped
public class ConfirmUserController implements Serializable {
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
}
