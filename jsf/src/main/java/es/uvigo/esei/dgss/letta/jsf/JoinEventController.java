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
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;

/**
 * {@linkplain JoinEventController} is a JSF controller to join a
 * {@link Event} by a {@link User}.
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
	 * Joins into a selected event. Redirects to index if joined succesfully.
	 * 
	 * @param event
	 *            the event user want join. This paramenter must be a non
	 *            {@code null} {@code Event}.
	 *
	 * @throws IOException
	 *             if an error happens on the redirection.
	 */
	public void doJoin(final Event event) throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			eventEJB.registerToEvent(event);
			context.redirect("index.xhtml?joined=true");
		} catch (EventAlredyJoinedException e) {
			context.redirect("index.xhtml?joined=false");
		}
	}
}
