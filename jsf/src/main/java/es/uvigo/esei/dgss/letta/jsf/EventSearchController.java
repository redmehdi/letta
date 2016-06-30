package es.uvigo.esei.dgss.letta.jsf;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Capital;
import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.State;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * {@linkplain EventSearchController} is a JSF controller to perform event
 * search actions.
 *
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 * @author Abel Fernández Nandín
 */

@ViewScoped
@ManagedBean(name = "searchController")
public class EventSearchController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EventEJB searchEJB;

	private int pageIndex = 0;
	private int pages = 0;
	private int currentPage = 1;

	private List<String> pagesLinks = new ArrayList<>();
	private String terms;
	private String location;
	private List<String> locations = new ArrayList<>();
	private List<Event> searchResults;
	private List<String> categories = new ArrayList<>();
	private String category;
	private List<String> states = new ArrayList<>();
	private String state;
	@Inject
	private Principal currentUserPrincipal;
	@Inject 
	private UserEJB userEJB;
	@Inject
	private UserAuthorizationEJB auth;
	
	@PostConstruct
	public void init() {
		for(Capital capital : searchEJB.getCapitals()) {
			locations.add(capital.getCapital());
		}
		
		this.categories = searchEJB.getEventCategories();
		this.states = searchEJB.getEventStates();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getRequestParameterMap()
				.get("term") != null) {
			String srch_terms = (String) facesContext.getExternalContext()
					.getRequestParameterMap().get("term");

			int page = Integer.parseInt(facesContext.getExternalContext()
					.getRequestParameterMap().get("page"));
			
			int elements_per_page = Integer
					.parseInt(facesContext.getExternalContext()
							.getRequestParameterMap().get("count"));
			
			this.location = facesContext.getExternalContext()
					.getRequestParameterMap().get("location");
			
			this.category = facesContext.getExternalContext()
					.getRequestParameterMap().get("category");
			
			this.state = facesContext.getExternalContext()
					.getRequestParameterMap().get("state");
			
			State state = null;
			
			if (this.state != null && this.state != "") {
				state = State.valueOf(this.state);
			}
			
			if (srch_terms != null && srch_terms != "") {
				this.terms = srch_terms;
			}

			//FIXME: count does not work for advanced search 
			int count = searchEJB.count(srch_terms);
			this.pageIndex = page;

			if (count > 0) {
				int num_pages = count / 4;
				if (count % 4 != 0)
					num_pages++;

				this.pages = num_pages;
				if (pagesLinks.isEmpty()) {
					for (int i = 0; i < pages; i++)
						pagesLinks.add(String.valueOf(i + 1));
				}
				
				if(this.category != null && this.category != "") {
					searchResults = searchEJB.advancedSearch(this.terms, state, this.category, this.pageIndex * 4, 4);
				} else {
					String location = this.location;
					
					if("anonymous".equals(this.currentUserPrincipal.getName())) {
						if(location.isEmpty()) {
							searchResults = searchEJB.search(this.terms, this.pageIndex * 4, 4);
						} else {
							searchResults = searchEJB.searchWithLocation(this.terms, location, this.pageIndex * 4, 4);
						}
					} else {
						if(location.isEmpty()) {						
							location = userEJB.get(currentUserPrincipal.getName()).get().getCity();
						}
						
						searchResults = searchEJB.searchWhileLoggedIn(this.terms, location, this.pageIndex * 4, 4);
					}
				}
				
			}
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(final int currentPage) {
		this.currentPage = currentPage;
	}

	public List<String> getPagesLinks() {
		return pagesLinks;
	}

	public void setPagesLinks(final List<String> pagesLinks) {
		this.pagesLinks = pagesLinks;
	}

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

	/**
	 * Set the result list to an external value
	 *
	 * @param searchResults
	 *            an external result list
	 */
	public void setSearchResults(final List<Event> searchResults) {
		this.searchResults = searchResults;
	}

	/**
	 * Returns the search term
	 *
	 * @return the search term
	 */
	public String getTerms() {
		return terms;
	}

	/**
	 * Set the internal search term to the actual value
	 *
	 * @param terms
	 *            the external search term
	 */
	public void setTerms(final String terms) {
		this.terms = terms;
	}
	
	/**
	 * Returns the current location of the user
	 *
	 * @return the current location of the user
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set the current location of the user to the actual value
	 *
	 * @param location
	 *            the current location of the user
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Returns the result list of locations
	 *
	 * @return the result {link List} of locations
	 */
	public List<String> getLocations() {
		return locations;
	}

	/**
	 * Returns the result list of events
	 *
	 * @return the result {link List} of events
	 */
	public List<Event> getSearchResults() {
		return searchResults;
	}
	
	/**
	 * Returns the result list of event categories
	 *
	 * @return the result {link List} of categories
	 */
	public List<String> getCategories() {
		return categories;
	}
	
	/**
	 * Returns the result list of event states
	 *
	 * @return the result {link List} of states
	 */
	public List<String> getStates() {
		return states;
	}

	/**
	 * Returns the current category
	 *
	 * @return the current category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Set the current category of the event
	 *
	 * @param category
	 *            the category of the event
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Returns the current state
	 *
	 * @return the current state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Set the current state of the event
	 *
	 * @param state
	 *            the state of the event
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Method to be called when the search command button is pressed
	 *
	 * @return a url to be redirected to (yet to be set)
	 */
	public String doSearchNext() {

		if (this.pageIndex < pages)
			this.pageIndex++;
		return "searchResults.xhtml";

	}

	public String doSearchPrev() {
		if (this.pageIndex > 1)
			this.pageIndex--;

		return "searchResults.xhtml";
	}

	public String jumpToPage(final String pageNumber) {
		return doSearchNext();
		// this.searchResults = searchEJB.search(terms,
		// (Integer.parseInt(pageNumber) - 1) * 4, 4);
		// this.currentPage = Integer.parseInt(pageNumber);
		// this.pageIndex = this.currentPage - 1;
		// return "searchResults.xhtml";
	}

}