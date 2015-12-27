package es.uvigo.esei.dgss.letta.jsf;

import static java.time.LocalDateTime.now;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * {@linkplain UserPrivateController} is a JSF controller to list the events
 * registered by the current {@link User}
 *
 * @author Jesús Álvarez Casanova
 * @author Adolfo Álvarez López
 */
@RequestScoped
@ManagedBean(name = "privateController")
public class UserPrivateController {

	@Inject
	private EventEJB eventEJB;
	
	@Inject
	private UserEJB userEJB;
    
    @Inject
	private Principal currentUserPrincipal;

	/**
	 * Returns a {@link List} of {@link Event} that were created by the current
	 * user
	 *
	 * @return {@link List} of {@link Event}
	 */
	public List<Event> getOwnEvents() {
		return eventEJB.getEventsOwnedByCurrentUser();
	}
	
	/**
	 * Returns a {@link List} of {@link Event} that were created by the current
	 * user
	 *
	 * @return {@link List} of {@link Event}
	 */
	public List<Event> getAttendingEvents() {
		return eventEJB.getAttendingEvents(0, -1);
	}

	/**
	 * Returns a {@link String} representing the path to the corresponding
	 * {@link Category} icon image.
	 *
	 * @param eventType the {@link Category} to be translated into a icon path.
	 *
	 * @return a String with the path to the icon associated to the received
	 *         event type.
	 */
	public String getIconFor(final Event.Category eventType) {
		return EventMappings.getIconFor(eventType);
	}

	/**
	 * Checks if the {@link Event} {@link Date}, is before the current
	 * {@link Date}
	 *
	 * @param event {@link Event} to check
	 * @return true if the {@link Date} is before the current {@link Date},
	 *         false otherwise
	 */
	public boolean isPassed(final Event event) {
	    return event.getDate().isBefore(now());
	}
	
	public boolean isOnUserLocation(final Event event){
		final String location = userEJB.get(currentUserPrincipal.getName()).get().getCity();
		if(location == null){
			return false;
		}else{
			if(location.equals(event.getLocation()))
				return true;
			else
				return false;
		}			
	}

}
