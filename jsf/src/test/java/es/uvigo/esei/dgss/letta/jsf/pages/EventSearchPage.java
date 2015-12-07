package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Location("faces/searchResults.xhtml")
public class EventSearchPage{
	@Drone
	private WebDriver browser;
	
	@FindBy(id = "search_event_form")
	private EventSearchForm eventSearchForm;
	
	public void assertOnIt() {
		assertThat(browser.getCurrentUrl(), containsString("/faces/searchResults.xhtml"));
	}	

	public void search(String word) {
		this.eventSearchForm.searchEvent(word);
	}
	
	
}
