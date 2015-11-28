package es.uvigo.esei.dgss.letta.jsf.util;

import javax.faces.context.FacesContext;
import javax.inject.Singleton;

/**
 * Provides utility methods to work with the current JSF view's path (the URL).
 *
 * @author Alberto Gutiérrez Jácome
 * @author Redouane Mehdi
 */
@Singleton
public class JSFPagePathUtils {

    /**
     * Returns a url with redirect faces param set to true.
     *
     * @param url
     *            indicates the url to redirect.
     * @return url with redirect faces param set to true.
     */
    public String redirectToPage(final String url) {
        return url + "?faces-redirect=true";
    }

    /**
     * Returns the id of the current view.
     *
     * @return id of the current view
     */
    public String getCurrentPage() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

}
