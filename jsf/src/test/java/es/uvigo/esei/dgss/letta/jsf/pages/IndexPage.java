package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.hamcrest.core.StringContains.containsString;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

@Location("faces/index.xhtml")
public class IndexPage {

    @Drone
    private WebDriver browser;
    
    @FindBy(id="indexRepeat")
    private IndexUiRepeat indexRepeat;

    @FindBy(id = "highlights-carousel")
    private WebElement highlightsCarousel;
    
    public void joinEvent(final Event event){
    	indexRepeat.join(event);
    }

    public void waitForIt() {
        waitGui(browser).until().element(highlightsCarousel).is().visible();
    }

    public void assertOnIt() {
        assertThat(browser.getCurrentUrl(), containsString("/faces/index.xhtml"));
    }
    
    public void assertOnJoinedTrue() {
        assertThat(browser.getCurrentUrl(), containsString("/faces/index.xhtml?joined=true"));
    }
    
    public void assertOnJoinedFalse() {
        assertThat(browser.getCurrentUrl(), containsString("/faces/index.xhtml?joined=false"));
    }

}
