package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class RegistrationTest {
	@Test
	public void testConstructor() {
		final User user = validUser();
		
		final Registration registration = new Registration(user);
		
		assertIsAnUuid(registration.getUuid());
		assertThat(registration.getLogin(), is(equalTo(user.getLogin())));
		assertThat(registration.getPassword(), is(equalTo(user.getPassword())));
		assertThat(registration.getEmail(), is(equalTo(user.getEmail())));
		assertThat(registration.getRole(), is(equalTo(user.getRole())));
		assertThat(registration.getCompleteName(), is(equalTo(user.getCompleteName())));
		assertThat(registration.getDescription(), is(equalTo(user.getDescription())));
		assertThat(registration.getFbUrl(), is(equalTo(user.getFbUrl())));
		assertThat(registration.getTwUrl(), is(equalTo(user.getTwUrl())));
		assertThat(registration.getPersonalUrl(), is(equalTo(user.getPersonalUrl())));
		assertThat(registration.getImage(), is(equalTo(user.getImage())));
		
		final User registeredUser = registration.getUser();
		
		assertThat(registeredUser.getLogin(), is(equalTo(user.getLogin())));
		assertThat(registeredUser.getPassword(), is(equalTo(user.getPassword())));
		assertThat(registeredUser.getEmail(), is(equalTo(user.getEmail())));
		assertThat(registeredUser.getRole(), is(equalTo(user.getRole())));
		assertThat(registeredUser.getCompleteName(), is(equalTo(user.getCompleteName())));
		assertThat(registeredUser.getDescription(), is(equalTo(user.getDescription())));
		assertThat(registeredUser.getFbUrl(), is(equalTo(user.getFbUrl())));
		assertThat(registeredUser.getTwUrl(), is(equalTo(user.getTwUrl())));
		assertThat(registeredUser.getPersonalUrl(), is(equalTo(user.getPersonalUrl())));
		assertThat(registeredUser.getImage(), is(equalTo(user.getImage())));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorNull() {
		new Registration(null);
	}
	
	private static void assertIsAnUuid(String uuid) {
		final String uuidRegex = "[0-9A-Z]{8}-([0-9A-Z]{4}-){3}[0-9A-Z]{12}";
		
		if (!uuid.toUpperCase().matches(uuidRegex)) {
			fail("Invalid UUID: " + uuid);
		}
	}
	
    @Test
    public void testEqualsHashCodeContract() {
        EqualsVerifier.forClass(Registration.class).suppress(
            Warning.NULL_FIELDS,
            Warning.NONFINAL_FIELDS
        ).verify();
    }
}
