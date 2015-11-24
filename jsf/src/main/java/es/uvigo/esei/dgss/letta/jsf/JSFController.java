package es.uvigo.esei.dgss.letta.jsf;

import javax.faces.context.FacesContext;

/**
 * Common interface to all JSF controllers.
 *
 * @author Alberto Gutiérrez Jácome
 */
public interface JSFController {

    /**
     * Returns a url with redirect faces param set to true.
     *
     * @param url
     *            indicates the url to redirect.
     * @return url with redirect faces param set to true.
     */
    default public String redirectTo(final String url) {
        return url + "?faces-redirect=true";
    }

    /**
     * Returns the id of the root view.
     *
     * @return id of the root view
     */
    default public String getRootViewId() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

}
