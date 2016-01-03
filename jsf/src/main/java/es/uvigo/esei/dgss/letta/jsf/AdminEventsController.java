package es.uvigo.esei.dgss.letta.jsf;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain AdminEventsController} is a JSF controller to manage {@link Event Events}
 * and perform any other admin-level operation.
 *
 * @author Adolfo Álvarez López
 */
@RequestScoped
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

	public LazyDataModel<Event> getEvents() {
		return this.events;
	}

	public void setEvents(LazyDataModel<Event> events) {
		this.events = events;
	}

}
