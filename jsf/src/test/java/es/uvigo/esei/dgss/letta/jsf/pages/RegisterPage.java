package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;

@Location("faces/register.xhtml")
public class RegisterPage {
	@Drone
	private WebDriver browser;

	public void assertOnRegisterPage() {
		assertThat(browser.getCurrentUrl(),
				containsString("/faces/register.xhtml"));
	}
}
