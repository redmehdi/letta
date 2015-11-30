package es.uvigo.esei.dgss.letta.service;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.TestingMailer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.RegistrationsDataset.newRegistration;
import static es.uvigo.esei.dgss.letta.service.util.ServiceIntegrationTestBuilder.deployment;

@RunWith(Arquillian.class)
@CleanupUsingScript({ "cleanup.sql" })
public class UserEJBTest {
	@Inject
	private UserEJB facade;

	@Inject
	private TestingMailer mailer;

    @Deployment
    public static Archive<?> deploy() {
        return deployment().withTestMailer().withClasses(
            UserEJB.class
        ).build();
    }

	@Test
	@UsingDataSet("registrations.xml")
	@ShouldMatchDataSet("registrations-create.xml")
	public void testRegisterUser() throws Exception {
		Registration registration = newRegistration();

		facade.registerUser(registration);

		assertThat(mailer.getEmail(), is(equalTo(registration.getEmail())));
		assertThat(mailer.getMessage(), containsString(registration.getUuid()));
	}

	@Test(expected = LoginDuplicateException.class)
	@UsingDataSet("registrations.xml")
	@ShouldMatchDataSet("registrations-create.xml")
	public void testRegisterUserLoginDuplicated() throws Exception {
		Registration registration = newRegistration();

		facade.registerUser(registration);
		facade.registerUser(registration);
	}

	@Test(expected = MessagingException.class)
	@UsingDataSet("registrations.xml")
	@ShouldMatchDataSet("registrations-create.xml")
	public void testRegisterUserSendEmailException() throws Exception {
		Registration registration = newRegistration();

		facade.registerUser(registration);
		mailer.setForceException(true);
		mailer.sendEmail("", "", "", "");

	}

//	@Test
//	@UsingDataSet("registrations-create.xml")
//	@ShouldMatchDataSet("registrations-register-user.xml")
//	public void testConfirmateUser() {
//		Registration registration = newRegistration();
//
//		facade.userConfirmation(registration.getUuid());
//
//		assertThat(registration.getUser(),is(equalTo(registration.getEmail())));
//	}
}