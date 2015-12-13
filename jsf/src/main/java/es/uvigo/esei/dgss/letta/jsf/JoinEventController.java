package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain JoinEventController} is a JSF controller to join an {@link Event}
 * by a {@link User}.
 *
 * @author abmiguez
 * @author bcgonzalez3
 *
 */
@ManagedBean(name = "joinController")
@RequestScoped
public class JoinEventController {

	@Inject
	private EventEJB eventEJB;

	/**
	 * Joins into a selected event. The event identifier should be received as
	 * a request parameters. This action redirects, immediately, to the index
	 * page, including the request parameter "joined" with {@code true} value if
	 * the user was effectively joined, or {@code false} otherwise.
	 *
	 * @throws IOException
	 *             if an error happens on the redirection.
	 */
	public void doJoin() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance()
			.getExternalContext();
		
		final String id = context.getRequestParameterMap()
			.getOrDefault("id", null);

		try {
			eventEJB.attendToEvent(Integer.parseInt(id));
			context.redirect("index.xhtml?joined=true");
		} catch (Exception e) {
			e.printStackTrace();
			context.redirect("index.xhtml?joined=false");
		}
	}
}
