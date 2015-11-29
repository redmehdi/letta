package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Location("faces/login.xhtml")
public class LoginPage {
	@Drone
	private WebDriver browser;

	@FindBy(id = "login-form")
	private LoginForm loginForm;

	public void login(String login, String password) {
		this.loginForm.login(login, password);
	}

	public void assertOnIt() {
		assertThat(browser.getCurrentUrl(),
				StringContains.containsString("/faces/login.xhtml"));
	}
}
