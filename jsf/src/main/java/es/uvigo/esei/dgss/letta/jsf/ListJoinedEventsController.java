package es.uvigo.esei.dgss.letta.jsf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
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
@SessionScoped
@ManagedBean(name = "listJoinedEventsController")
public class ListJoinedEventsController{

	@Inject
	private EventEJB eventEJB;

	private int pageIndex=1;
    private int pages=0;
    private final int CTE_NUM_EVENTS_PAGE=4;
	private ArrayList<String> pagesLinks = new ArrayList<>();

	public int getPages() {
		return pages;
	}
	public void setPages(final int pages) {
		this.pages = pages;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(final int pageIndex) {
		this.pageIndex = pageIndex;
	}
    public ArrayList<String> getPagesLinks() {
		return pagesLinks;
	}
	public void setPagesLinks(final ArrayList<String> pagesLinks) {
		this.pagesLinks = pagesLinks;
	}
	/**
     * Initialize the number of pages in a paginated page.
     *
     */
    @PostConstruct
    public void init(){
    	pages=eventEJB.countAttendingEvents()/CTE_NUM_EVENTS_PAGE;
    	if (eventEJB.countAttendingEvents()%CTE_NUM_EVENTS_PAGE != 0)
    		pages++;
		for (int i = 0; i < pages; i++)
            pagesLinks.add(String.valueOf(i + 1));
    }

    /**
     * Retrieves a {@link List} of {@link Event}s from
     * {@link EventEJB#countAttendingEvents()}, sorted by desccending date and
     * ascending title.
     *
     * @return an ordered List of twenty events.
     */
    public List<Event> getEventList() {
		return eventEJB.getAttendingEvents(
				(this.pageIndex - 1) * CTE_NUM_EVENTS_PAGE,
				CTE_NUM_EVENTS_PAGE);
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
     * {@link Category} icon image.
     *
     * @param eventType the {@link Category} to be translated into a icon path.
     *
     * @return a String with the path to the icon associated to the received
     *         event type.
     */
    public String getIconFor(final Category eventType) {
        return EventMappings.getIconFor(eventType);
    }

    /**
     * Update the value of pageIndex and redirect.
     *
     * @param pageNumber, number of page to
     *
     */
	public void jumpToPage(final String pageNumber) {
		this.pageIndex = Integer.parseInt(pageNumber);
	}

	public boolean isAfter(final LocalDateTime d) {
	    return d.isAfter(LocalDateTime.now());
	}
}
