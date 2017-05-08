package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UserNotifications;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.UserEJB;

@ManagedBean(name = "userNotifications")
@SessionScoped
public class ListUserNotifications {
	@Inject
	private UserEJB userEJB;

	private int pageIndex=1;
    private int pages=0;
    private final int CTE_NUM_MESSAGES_PAGE=4;
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
    	pages=getNotifications().size()/CTE_NUM_MESSAGES_PAGE;
    	if (getNotifications().size()%CTE_NUM_MESSAGES_PAGE != 0)
    		pages++;
		for (int i = 0; i < pages; i++)
            pagesLinks.add(String.valueOf(i + 1));
    }

    /**
	 * Get a list of {@link UserNotifications}.
	 * @return a sorted list of {@link UserNotifications}.
	 */
	public List<UserNotifications> getNotifications() {
		List<UserNotifications> userNotifications = userEJB.getNotifications();
		Collections.sort(userNotifications, (c1, c2) -> c2.getNotificationId() - c1.getNotificationId());

		return  userNotifications;
	}

    /**
     * Method to be called when the next command button is pressed
     *
     */
    public void listUserNotificationsNext() {
        if(this.pageIndex < this.pages )
        	this.pageIndex++;
    }

    /**
     * Method to be called when the previous command button is pressed
     *
     */
    public void listUserNotificationsPrev(){
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

	/**
	 * Get the current {@link UserNotifications}
	 * @return a {@link UserNotifications}.
	 * @throws IOException if an error happens while redirecting.
	 */
	public UserNotifications getCurrentNotification() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int notificationId = Integer.parseInt(params.get("notificationId"));

		UserNotifications userNotifications = userEJB.getNotification(notificationId);

		if (userNotifications == null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("messages.xhtml");
		}

		return userNotifications;
	}

	/**
	 * Count the unread {@link ListUserNotifications} by the current {@link User}.
	 * @return a number of unread {@link ListUserNotifications}
	 */
	public Long countUnreadNotifications() {
		return userEJB.countUnreadNotifications();
	}
}
