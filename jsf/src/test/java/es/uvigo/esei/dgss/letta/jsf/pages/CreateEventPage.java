package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.EventType;

@Location("faces/createEvent.xhtml")
public class CreateEventPage {
	@Drone
	private WebDriver browser;

	@FindBy(id = "createEvent-form")
	private CreateEventForm createEventForm;

	public void createEvent(String title, String shortDescription, Date date,
			String location, EventType type) {
		this.createEventForm.createEvent(title, shortDescription, date,
				location, type);
	}

	public void assertOnIt() {
		assertThat(browser.getCurrentUrl(),
				StringContains.containsString("/faces/createEvent.xhtml"));
	}
}
