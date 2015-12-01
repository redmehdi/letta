package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

@Location("faces/createEvent.xhtml")
public class CreateEventPage {
	@Drone
	private WebDriver browser;

	@FindBy(id = "createEvent-form")
	private CreateEventForm createEventForm;

	public void assertOnIt() {
		assertThat(browser.getCurrentUrl(),
				StringContains.containsString("/faces/createEvent.xhtml"));
	}

	public void createEvent(Event newEventWithoutCreator) {
		this.createEventForm.createEvent(newEventWithoutCreator);
	}
}
