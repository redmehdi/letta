package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.hamcrest.core.StringContains.containsString;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("faces/index.xhtml")
public class MainPage {
	@Drone
	private WebDriver browser;
	
	@FindBy(id = "highlights-carousel")
	private WebElement highlightsCarousel;
	
	public void waitForMainPage(){
		waitGui(browser).until().element(highlightsCarousel).is().visible();
	}

	public void assertOnMainPage() {
		assertThat(browser.getCurrentUrl(),
				containsString("/faces/index.xhtml"));
	}
}
