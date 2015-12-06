package es.uvigo.esei.dgss.letta.jsf;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventType;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain ListJoinedEventsController} is a JSF controller to list events
 *  that the current user are joined.
 *
 * @author Borja Cordeiro González
 * @author Adrian Rodriguez Fariña
 *
 */
@RequestScoped
@ManagedBean(name = "listJoinedEventsController")
public class ListJoinedEventsController{

	@Inject
	private EventEJB eventEJB;
	
    @Inject
    private EventMappings eventMapper;
        
	private int pageIndex=1;
    private int pages=0;
    private final int CTE_NUM_EVENTS_PAGE=4;
    
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
    /**
     * Initialize the number of pages in a paginated page.
     *
     */
    @PostConstruct
    public void init(){
    	pages=eventEJB.getCountEventsJoinedByUser()/CTE_NUM_EVENTS_PAGE;
    	if (eventEJB.getCountEventsJoinedByUser()%CTE_NUM_EVENTS_PAGE != 0)
    		pages++;
    }
	
    /**
     * Retrieves a {@link List} of {@link Event}s from
     * {@link EventEJB#getCountEventsJoinedByUser()}, sorted by desccending date and
     * ascending tittle.
     *
     * @return an ordered List of twenty events.
     */
    public List<Event> getEventList() {
        return eventEJB.getEventsJoinedByUser((this.pageIndex-1)*CTE_NUM_EVENTS_PAGE, CTE_NUM_EVENTS_PAGE);
    }
    

    /**
     * Method to be called when the next command button is pressed
     *
     */
    public void listJoinedEventsNext() {
        if(this.pageIndex < this.pages )
        	this.pageIndex++;
    }
    
    /**
     * Method to be called when the previous command button is pressed
     *
     */
    public void listJoinedEventsPrev(){
		if (this.pageIndex > 1)
			this.pageIndex--;
	}
	
    /**
     * Returns a {@link String} representing the path to the corresponding
     * {@link EventType} icon image.
     *
     * @param eventType
     *            the {@link EventType} to be translated into a icon path.
     *
     * @return a String with the path to the icon associated to the received
     *         event type.
     */
    public String getIconFor(final EventType eventType) {
        return eventMapper.getIconFor(eventType);
    }
	
}
