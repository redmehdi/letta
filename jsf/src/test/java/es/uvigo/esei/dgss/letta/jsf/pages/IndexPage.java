package es.uvigo.esei.dgss.letta.jsf.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.hamcrest.core.StringContains.containsString;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertThat;

@Location("faces/index.xhtml")
public class IndexPage {

    @Drone
    private WebDriver browser;

    @FindBy(id = "highlights-carousel")
    private WebElement highlightsCarousel;

    public void waitForIt() {
        waitGui(browser).until().element(highlightsCarousel).is().visible();
    }

    public void assertOnIt() {
        assertThat(browser.getCurrentUrl(), containsString("/faces/index.xhtml"));
    }

}
