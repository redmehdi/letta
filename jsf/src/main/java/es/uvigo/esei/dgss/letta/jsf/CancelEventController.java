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
 * {@linkplain CancelEventController} is a JSF controller to join an {@link Event}
 * by a {@link User}.
 *
 * @author Borja Cordeiro Gonzalez
 *
 */
@RequestScoped
@ManagedBean(name = "cancelEventController")
public class CancelEventController {

	@Inject
	private EventEJB eventEJB;

	/**
	 * Cancel a owned event. The event identifier should be received as a
	 * request parameters. This action redirects, immediately, to the index
	 * page, including the request parameter "joined" with {@code true} value if
	 * the user was effectively joined, or {@code false} otherwise.
	 *
	 * @throws IOException
	 *             if an error happens on the redirection.
	 */
	public void cancelEvent() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String id = context.getRequestParameterMap().getOrDefault("id", null);

		try {
			eventEJB.cancelEvent(Integer.parseInt(id));
			context.redirect("index.xhtml?cancelled=true");
		} catch (Exception e) {
			e.printStackTrace();
			context.redirect("index.xhtml?cancelled=false");
		}
	}

	/**
	 * Retrieves if a event is cancelled.
	 * 
	 * @param eventId the id of the event to check
	 * @return true if the event is cancelled, false otherwhise
	 */
	public boolean isCancelled(final int eventId)  {
		return eventEJB.isCancelled(eventId);
	}

}
