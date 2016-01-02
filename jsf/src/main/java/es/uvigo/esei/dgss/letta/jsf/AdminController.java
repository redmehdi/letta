package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventIsCancelledException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.IllegalEventOwnerException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;

/**
 * {@linkplain AdminController} is a JSF controller to manage {@link User Users}
 * and perform any other admin-level operation.
 *
 * @author Alberto Pardellas Soto
 * @author Alberto Gutiérrez Jácome
 */
@ManagedBean(name = "adminController")
public class AdminController {
	@Inject
	private UserEJB userEJB;

	@Inject
	private EventEJB eventEJB;
	
	@Inject
	private UserAuthorizationEJB auth;

	/**
	 * Retrieves a {@link List} of {@link User Users} that they exist in the
     * database.
	 *
     * @return an ordered List of {@link User Users}.
	 */
	public List<User> getUsers() {
		return userEJB.getUsers();
	}

	/**
	 * Delete a {@link User} 
	 * @param user the {@link User} to be removed.
     * @throws SecurityException if the currently identified {@link User} is not found
     *         in the database.
     * @throws EventIsCancelledException if the {@link Event} is cancelled.
     * @throws IllegalEventOwnerException if the {@link Event} does not exist.
	 * @throws UserNotAuthorizedException if the {@link User} is not authorized.
	 */
	public void deleteUser(User user) 
			throws SecurityException, EventIsCancelledException, 
			IllegalEventOwnerException, UserNotAuthorizedException {

		if (!auth.getCurrentUser().equals(user)) {
			String login = user.getLogin();
			LocalDateTime today = LocalDateTime.now();
			List<Event> events = eventEJB.getEventsOwnedBy(user);

			for (Event event : events) {
				if (event.getDate().isAfter(today)) {
					if (!event.isCancelled()) {
						eventEJB.cancelEvent(event.getId());
					}
				}
			}
			
			for (Event event : eventEJB.getEventsOwnedBy(user)) {
				event.setOwner(auth.getCurrentUser());
				eventEJB.modifyEvent(event);
			}
			
			userEJB.removeUser(user.getLogin());
	        addMessage("User deleted", "The user " + login + " has been deleted.");
	        
		} else {
			addMessage("Error", "You can not delete the current user.");
		}
	}
	
    public void editUser(final String login) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(
            "modifyProfile.xhtml?login=" + login
        );
    }
    
	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
