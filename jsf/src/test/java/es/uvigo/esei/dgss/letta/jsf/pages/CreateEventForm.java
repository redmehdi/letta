package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import es.uvigo.esei.dgss.letta.domain.entities.EventType;

public class CreateEventForm {
	@FindBy(id = "createEvent-form:title-field")
	private WebElement inputTitle;

	@FindBy(id = "createEvent-form:shortDescription-field")
	private WebElement inputShortDescription;

	@FindBy(id = "createEvent-form:date-field")
	private WebElement inputDate;

	@FindBy(id = "createEvent-form:date-field_input")
	private WebElement inputDateSelector;

	@FindBy(id = "createEvent-form:location-field")
	private WebElement inputLocation;

	@FindBy(id = "createEvent-form:type-field")
	private WebElement inputType;

	@FindBy(id = "createEvent-form:submit-button")
	private WebElement buttonSubmit;

	public void createEvent(String title, String shortDescription, Date date,
			String location, EventType type) {
		inputTitle.clear();
		inputShortDescription.clear();
		inputLocation.clear();

		inputTitle.sendKeys(title);
		inputShortDescription.sendKeys(shortDescription);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		inputDateSelector.sendKeys(dateFormat.format(date));
//		inputDate.click();
//		inputDateSelector.click();
		
		inputLocation.sendKeys(location);
//		Select select = new Select(inputType);
//		select.deselectAll();
//		select.selectByVisibleText("TV");
		inputType.sendKeys("TV");

		guardHttp(buttonSubmit).click();
	}
}
