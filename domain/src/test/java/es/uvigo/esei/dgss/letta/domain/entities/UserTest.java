package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.aLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.aPassword;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.aPasswordMD5;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.anEmail;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.anEmailWithBadFormat;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.emptyLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.longLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.longestLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.newEmail;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.newLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.newPassword;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.newPasswordMD5;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.shortPassword;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.shortestLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.shortestPassword;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.shortestPasswordMD5;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class UserTest {
	@Test
	public void testConstructorValidLogins() {
		final String[] logins = { shortestLogin(), aLogin(), longestLogin() };
		final String password = aPassword();
		final String passwordMD5 = aPasswordMD5();
		final String email = anEmail();
		
		for (String login : logins) {
			final User user = new User(login, password, email);
	
			assertThat(user.getLogin(), is(equalTo(login)));
			assertThat(user.getPassword(), is(equalTo(passwordMD5)));
			assertThat(user.getEmail(), is(equalTo(email)));
			assertThat(user.getRole(), is(equalTo(Role.USER)));
		}
	}
	
	@Test
	public void testConstructorValidPasswords() {
		final String login = aLogin();
		final String[] passwords = { shortestPassword(), aPassword() };
		final String[] passwordsMD5 = { shortestPasswordMD5(), aPasswordMD5() };
		final String email = anEmail();
		
		for (int i = 0; i < passwords.length; i++) {
			final User user = new User(login, passwords[i], email);
	
			assertThat(user.getLogin(), is(equalTo(login)));
			assertThat(user.getPassword(), is(equalTo(passwordsMD5[i])));
			assertThat(user.getEmail(), is(equalTo(email)));
			assertThat(user.getRole(), is(equalTo(Role.USER)));
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorNullLogin() {
		new User(null, aPassword(), anEmail());
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorNullPassword() {
		new User(aLogin(), null, anEmail());
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorNullEmail() {
		new User(aLogin(), aPassword(), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmptyLogin() {
		new User(emptyLogin(), aPassword(), anEmail());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorLongLogin() {
		new User(longLogin(), aPassword(), anEmail());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorShortPassword() {
		new User(aLogin(), shortPassword(), anEmail());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorInvalidEmail() {
		new User(aLogin(), aPassword(), anEmailWithBadFormat());
	}

	@Test
	public void testSetLogin() {
		final String[] logins = { shortestLogin(), aLogin(), longestLogin() };
		final String newLogin = newLogin();
		
		for (String login : logins) {
			final User user = new User(login, aPassword(), anEmail());
			
			user.setLogin(newLogin);
	
			assertThat(user.getLogin(), is(equalTo(newLogin)));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetLoginEmpty() {
		final User user = validUser();
		
		user.setLogin(emptyLogin());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetLoginTooLong() {
		final User user = validUser();
		
		user.setLogin(longLogin());
	}

	@Test
	public void testSetPassword() {
		final User user = validUser();
		final String newPasswordMD5 = newPasswordMD5();
		
		user.setPassword(newPasswordMD5);

		assertThat(user.getPassword(), is(equalTo(newPasswordMD5)));
	}

	@Test(expected = NullPointerException.class)
	public void testSetPasswordNullValue() {
		final User user = validUser();
		
		user.setPassword(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPasswordNoMD5() {
		final User user = validUser();
		
		user.setPassword("No MD5 password");
	}

	@Test
	public void testChangePassword() {
		final User user = validUser();
		
		user.changePassword(newPassword());

		assertThat(user.getPassword(), is(equalTo(newPasswordMD5())));
	}

	@Test(expected = NullPointerException.class)
	public void testChangePasswordNull() {
		final User user = validUser();
		
		user.changePassword(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangePasswordTooShort() {
		final User user = validUser();
		
		user.changePassword(shortPassword());
	}

	@Test
	public void testSetEmail() {
		final User user = validUser();
		final String newEmail = newEmail();
		
		user.setEmail(newEmail);
		
		assertThat(user.getEmail(), is(equalTo(newEmail)));
	}
	
}
