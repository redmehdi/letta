package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Location("faces/register.xhtml")
public class RegisterPage {
	@Drone
	private WebDriver browser;
	
	@FindBy(id = "register-form")
	private RegisterForm registerForm;
	
	public void register(String login, String password, String email) {
		this.registerForm.register(login, password, email);
	}
	
	public void assertOnRegisterPage() {
		assertThat(browser.getCurrentUrl(),
				containsString("/faces/register.xhtml"));
	}
}
