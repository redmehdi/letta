package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventIsCancelledException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.IllegalEventOwnerException;

/**
 * {@linkplain AdminEventsController} is a JSF controller to manage {@link Event Events}
 * and perform any other admin-level operation.
 *
 * @author Adolfo Álvarez López
 */
@ViewScoped
@ManagedBean(name = "adminEventsController")
public class AdminEventsController {

	@Inject
	private EventEJB eventEJB;

	private LazyDataModel<Event> events;

	@PostConstruct
	public void init(){
		this.events = new LazyDataModel<Event>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Event> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				List<Event> data =eventEJB.listByCreatedAt(first, pageSize);
				return data;
			}
		};
		events.setRowCount(eventEJB.countAll());
	}

	/**
	 * Cancels the selected {@link Event}.
	 *
	 * @param event
	 *            The {@link Event} to cancel.
	 * @throws SecurityException
	 *             If the currently identified {@link User} is not found in the
	 *             database.
	 * @throws IllegalArgumentException
	 *             if the {@link Event} does not exist.
	 * @throws EventIsCancelledException
	 *             if the {@link Event} is still cancelled.
	 * @throws IllegalEventOwnerException
	 *             if the {@link Event} does not exist.
	 */
	public void cancelEvent(Event event)
			throws SecurityException, IllegalArgumentException, EventIsCancelledException, IllegalEventOwnerException {
		String title = event.getTitle();
		eventEJB.cancelEvent(event.getId());
		addMessage("Event Cancelled", "The event " + title + " has been cancelled.");
	}

	/**
	 * Removes the selected {@link Event}.
	 *
	 * @param event The {@link Event} to remove.
	 */
	public void removeEvent(Event event){
		String title = event.getTitle();
		eventEJB.removeEvent(event.getId());
        addMessage("Event removed", "The event " + title + " has been cancelled.");
	}


	/**
	 * Redirects to the form for edit the selected {@link Event}
	 *
	 * @param event The {@link Event} selected for edit.
	 * @throws IOException if an error happens during the redirection.
	 */
	public void editEvent(final Event event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect(
            "modifyEvent.xhtml?id=" + event.getId()
        );
    }

	public LazyDataModel<Event> getEvents() {
		return this.events;
	}

	public void setEvents(LazyDataModel<Event> events) {
		this.events = events;
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
