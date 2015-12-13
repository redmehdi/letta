package es.uvigo.esei.dgss.letta.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain EventSearchController} is a JSF controller to perform event
 * search actions.
 *
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 */

@SessionScoped
@ManagedBean(name = "searchController")
public class EventSearchController implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EventEJB searchEJB;
	private int pageIndex = 0;
	private int pages;
	private int currentPage = 1;

	private ArrayList<String> pagesLinks = new ArrayList<>();
	private String terms = null;
	private List<Event> searchResults;
	private boolean flag;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(final int currentPage) {
		this.currentPage = currentPage;
	}

	public ArrayList<String> getPagesLinks() {
		return pagesLinks;
	}

	public void setPagesLinks(final ArrayList<String> pagesLinks) {
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
	 * Returns the EJB to work with events
	 *
	 * @return searchEJB
	 */
	public EventEJB getSearchEJB() {
		return searchEJB;
	}

	/**
	 * Set the internal EJB
	 *
	 * @param searchEJB
	 *            the external EJB
	 */
	public void setSearchEJB(final EventEJB searchEJB) {
		this.searchEJB = searchEJB;
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
	 * Returns the flag used to control whether to render the datable or not
	 *
	 * @return the boolean flag
	 */
	public boolean getFlag() {
		return flag;
	}

	/**
	 * Set the internal flag to an external value
	 *
	 * @param flag
	 *            the external value
	 */
	public void setFlag(final boolean flag) {
		this.flag = flag;
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
	 * Returns the result list of events
	 *
	 * @return the result {link List} of events
	 */
	public List<Event> getSearchResults() {
		return searchResults;
	}

	/**
	 * Method to be called when the search command button is pressed
	 *
	 * @return a url to be redirected to (yet to be set)
	 */
	public String doSearchNext() {

		this.pages = searchEJB.search(terms, 0, searchEJB.count()).size() / 4;
		if (this.pageIndex < pages)
            this.pageIndex++;
		if (terms != null)
            if (pageIndex == 1) {
				searchResults = searchEJB.search(terms, pageIndex - 1, 4);

				for (int i = 0; i < pages; i++)
                    pagesLinks.add(String.valueOf(i + 1));
			} else
                searchResults = searchEJB.search(terms, (pageIndex - 1) * 4, 4);
		this.currentPage = pageIndex;

		return "searchResults.xhtml";
	}

	public String doSearchPrev() {
		this.pages = searchEJB.search(terms, 0, searchEJB.count()).size() / 4;
		if (this.pageIndex > 1)
            this.pageIndex--;
		if (terms != null)
            if (pageIndex == 0) {
			} else
                searchResults = searchEJB.search(terms, (pageIndex - 1) * 4, 4);
		this.currentPage = pageIndex;
		return "si";
	}

	public String jumpToPage(final String pageNumber) {
		System.out.println("PAGENUMBER " + pageNumber);
		searchResults = searchEJB.search(terms, (Integer.parseInt(pageNumber) - 1) * 4, 4);
		this.currentPage = Integer.parseInt(pageNumber);
		return "si";
	}

}
