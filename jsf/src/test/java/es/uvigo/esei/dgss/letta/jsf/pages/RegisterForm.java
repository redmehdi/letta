package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterForm {
	@FindBy(id = "register-form:login")
	private WebElement inputLogin;
	
	@FindBy(id = "register-form:email")
	private WebElement inputEmail;
	
	@FindBy(id = "register-form:password")
	private WebElement inputPassword;
	
	@FindBy(id = "register-form:submit-button")
	private WebElement buttonSubmit;
	
	
	public void register(String login, String password, String email) {
		inputLogin.clear();
		inputPassword.clear();
		inputEmail.clear();
		
		inputLogin.sendKeys(login);
		inputPassword.sendKeys(password);
		inputEmail.sendKeys(email);

		guardHttp(buttonSubmit).click();

	}
}
