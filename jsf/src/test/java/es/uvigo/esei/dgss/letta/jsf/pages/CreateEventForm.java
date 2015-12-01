package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

public class CreateEventForm {
	@Drone
	private WebDriver browser;
	
	@FindBy(id = "createEvent-form:title-field")
	private WebElement inputTitle;

	@FindBy(id = "createEvent-form:shortDescription-field")
	private WebElement inputShortDescription;

	@FindBy(id = "createEvent-form:date-field_input")
	private WebElement inputDateSelector;

	@FindBy(id = "createEvent-form:location-field")
	private WebElement inputLocation;

	@FindBy(id = "createEvent-form:type-field")
	private WebElement inputType;

	@FindBy(id = "createEvent-form:submit-button")
	private WebElement buttonSubmit;
	
	@FindBy(id = "ui-datepicker-div")
	private WebElement datePicker;

	public void createEvent(Event event) {
		inputTitle.clear();
		inputShortDescription.clear();
		inputLocation.clear();

		inputTitle.sendKeys(event.getTitle());
		inputShortDescription.sendKeys(event.getShortDescription());
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		inputDateSelector.sendKeys(dateFormat.format(event.getDate()));
		
		hideDatePickerAndWait();
		
		inputLocation.sendKeys(event.getLocation());
		
		inputType.sendKeys(event.getEventType().toString());

		guardHttp(buttonSubmit).click();
	}

	/*
	 * Little trick to wait for the date picker to close.
	 * 
	 * Using this methods allows unexpected blocks when testing this form. It is
	 * recommended to use this method after an action that selects the input
	 * date selector, such as sending keys.
	 */
	private void hideDatePickerAndWait() {
		inputTitle.click();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		waitGui(browser).until().element(datePicker).is().not().visible();
	}
}