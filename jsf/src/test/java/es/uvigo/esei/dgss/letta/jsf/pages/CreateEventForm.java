package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.EventType;

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

	public void createEvent(String title, String shortDescription, Date date,
			String location, EventType type) {
		inputTitle.clear();
		inputShortDescription.clear();
		inputLocation.clear();

		inputTitle.sendKeys(title);
		inputShortDescription.sendKeys(shortDescription);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		inputDateSelector.sendKeys(dateFormat.format(date));
		
		hideDatePickerAndWait();
		
		inputLocation.sendKeys(location);
		
		inputType.sendKeys("TV");

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
