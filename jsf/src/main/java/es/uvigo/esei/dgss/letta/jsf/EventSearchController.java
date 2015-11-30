package es.uvigo.esei.dgss.letta.jsf;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.jsf.util.LazyEventList;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain EventSearchController} is a JSF controller to perform event
 * search actions.
 *
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 */
@ViewScoped
@ManagedBean(name = "searchController")
public class EventSearchController {

    @Inject
    private EventEJB searchEJB;

    private String terms = null;
    private LazyDataModel<Event> searchResults2;
    private List<Event> searchResults;
    private boolean flag = false;

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
     * Returns the lazy data model
     *
     * @return the lazy data model
     */
    public LazyDataModel<Event> getSearchResults2() {
        return searchResults2;
    }

    /**
     * Set the lazy data model to an external value
     *
     * @param searchResults2
     *            the external value
     */
    public void setSearchResults2(final LazyDataModel<Event> searchResults2) {
        this.searchResults2 = searchResults2;
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
    public String doSearch() {

        searchResults2 = new LazyEventList() {

            private static final long serialVersionUID = 1L;

            @Override
            public List<Event> findEvent(final int first, final int pageSize) {
                searchResults = searchEJB.search(terms, first, pageSize);
                this.setCount(searchEJB.count());
                System.out.println("Search result " + searchResults.size());
                return searchResults;
            }

        };
        flag = true;
        return "si";
    }

}
