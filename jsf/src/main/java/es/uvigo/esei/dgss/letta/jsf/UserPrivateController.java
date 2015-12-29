package es.uvigo.esei.dgss.letta.jsf;

import static java.time.LocalDateTime.now;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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

	private int pageIndex = 1;
	private int pages = 0;
	private final int CTE_NUM_EVENTS_PAGE = 4;
	private ArrayList<String> pagesLinks = new ArrayList<>();

	/**
	 * Initialize the number of page in the paginated page
	 * 
	 */
	@PostConstruct
	public void init() {
		pages = eventEJB.countEventsOwnedByCurrentUser() / CTE_NUM_EVENTS_PAGE;
		if (eventEJB.countEventsOwnedByCurrentUser() % CTE_NUM_EVENTS_PAGE != 0)
			pages++;
		for (int i = 0; i < pages; i++)
			pagesLinks.add(String.valueOf(i + 1));
	}

	/**
	 * This method change the page to the next valid page
	 *
	 */
	public void listCreatedEventsNext() {
		if (this.pageIndex < this.pages)
			this.pageIndex++;
	}

	/**
	 * This method change the page to the previous valid page
	 * 
	 */
	public void listCreatedEventsPrev() {
		if (this.pageIndex > 1)
			this.pageIndex--;
	}

	/**
	 * This method changes the page to the selected number
	 * 
	 * @param pageNumber
	 *            the number of the selected page
	 */
	public void jumpToPage(final String pageNumber) {
		this.pageIndex = Integer.parseInt(pageNumber);
	}

    /**
	 * Returns a {@link List} of {@link Event} that were created by the current
	 * user
	 *
	 * @return {@link List} of {@link Event}
	 */
	public List<Event> getOwnEvents() {
		return eventEJB.getEventsOwnedByCurrentUser((this.pageIndex - 1) * CTE_NUM_EVENTS_PAGE, CTE_NUM_EVENTS_PAGE);
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
	
	public boolean isAtUserLocation(final Event event){
		final String location = userEJB.get(currentUserPrincipal.getName()).get().getCity();
		if(location == null){
			return false;
		}else{
			if(location.equals(event.getPlace()))
				return true;
			else
				return false;
		}			
	}
	
	/**
	 * Returns the value of the variable pages
	 *
	 * @return the number of pages of events.
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * Setter method of pages variable.
	 *
	 * @param pages
	 *            number of pages of events.
	 */
	public void setPages(final int pages) {
		this.pages = pages;
	}

	/**
	 * Returns the value of the variable pageIndex
	 * 
	 * @return the number of the current page
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * Setter method for the pageIndex variable
	 * 
	 * @param pageIndex
	 *            The number of the current page of events.
	 */
	public void setPageIndex(final int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * Returns the value of the variable pagesLinks
	 * 
	 * @return the list of links to the available pages of events
	 */
	public ArrayList<String> getPagesLinks() {
		return pagesLinks;
	}

	/**
	 * Setter method for the pagesLinks variable
	 * 
	 * @param pagesLinks
	 *            The list of links to the available pages of events.
	 */
	public void setPagesLinks(final ArrayList<String> pagesLinks) {
		this.pagesLinks = pagesLinks;
	}

}
