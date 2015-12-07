package es.uvigo.esei.dgss.letta.jsf.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

import static org.hamcrest.core.StringContains.containsString;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertThat;

@Location("faces/index.xhtml")
public class IndexPage {

    @Drone
    private WebDriver browser;

    @FindBy(id = "events-list")
    private EventsList eventsList;

    @FindBy(id = "highlights-carousel")
    private EventsList highlights;

    public EventsList getEventsList() {
        return eventsList;
    }

    public EventsList getHighlights() {
        return highlights;
    }

    public void joinEvent(final Event event) {
        eventsList.join(event);
    }

    public void waitForIt() {
        waitGui(browser).until().element(By.id("events-list")).is().visible();
    }

    public void assertOnIt() {
        assertThat(
            browser.getCurrentUrl(),
            containsString("/faces/index.xhtml")
        );
    }

    public void assertOnJoinedTrue() {
        assertThat(
            browser.getCurrentUrl(),
            containsString("/faces/index.xhtml?joined=true")
        );
    }

    public void assertOnJoinedFalse() {
        assertThat(
            browser.getCurrentUrl(),
            containsString("/faces/index.xhtml?joined=false")
        );
    }

}
