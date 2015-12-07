package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;

public class EventSearchForm {
	@Drone
	private WebDriver browser=new FirefoxDriver();
	
	@FindBy(id = "search_event_form:srch-term")
	private WebElement inputSearch;
	@FindBy(id="search_event_form:search_event_button")
	private WebElement submitButton;
	
	public void searchEvent(String text){
		inputSearch.sendKeys(text);
		guardHttp(submitButton).click();

		
		
	}

}
