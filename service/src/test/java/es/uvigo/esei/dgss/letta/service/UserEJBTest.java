package es.uvigo.esei.dgss.letta.service;

import static es.uvigo.esei.dgss.letta.domain.entities.RegistrationsDataset.newRegistration;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.userWithLogin;
import static es.uvigo.esei.dgss.letta.service.util.ServiceIntegrationTestBuilder.deployment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
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
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UsersDataset;
import es.uvigo.esei.dgss.letta.mail.Email;
import es.uvigo.esei.dgss.letta.mail.MailBox;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EmailDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.TestingMailer;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;

@RunWith(Arquillian.class)
@CleanupUsingScript({ "cleanup.sql" })
public class UserEJBTest {
	@Inject
	private UserEJB facade;

	@Inject
	private TestingMailer mailer;

	@Inject
    private TestPrincipal principal;

	@EJB(beanName = "user-caller")
    private RoleCaller asUser;
	
    @EJB(beanName = "admin-caller")
    private RoleCaller asAdmin;

    @Deployment
    public static Archive<?> deploy() {
        return deployment().withTestMailer().withTestPrincipal().withClasses(
            UserEJB.class, MailBox.class, Email.class, UserAuthorizationEJB.class
        ).build();
    }

    @Test
    public void getRegistrationWithLoginNull(){
    	assertThat(facade.registrationWithLogin("jake"),is(equalTo(null)));
    }
    
    @Test(expected=javax.ejb.EJBTransactionRolledbackException.class)
    public void getNullEmail(){
    	facade.getByEmail(null);
    }

    @Test
    public void getEmptyEmail(){
    	Optional<User> test =facade.getByEmail("jasdsa@adasd.com");
        assertThat(test, is(Optional.empty()));

    }

    @Test(expected=javax.ejb.EJBTransactionRolledbackException.class)
    public void get(){
    	facade.get(null);
    }

    @Test(expected=javax.ejb.EJBTransactionRolledbackException.class)
    public void getNull(){
    	facade.get(null);
    }
    
    @Test
	@UsingDataSet("users.xml")
    public void getReal(){
    	final User u = UsersDataset.existentUser();
    	Optional<User> test =facade.get(u.getLogin());
    	assertThat(u.getLogin(),is(equalTo(test.get().getLogin()))
    			);
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

	@Test(expected = EmailDuplicateException.class)
	@UsingDataSet("registrations.xml")
	@ShouldMatchDataSet("registrations-create.xml")
	public void testRegisterUserEmailDuplicated() throws Exception {
		Registration registration = new Registration(new User("login", "password", "email@email.com", "complete name",
				"description", "fb url", "tw url", "personal url", false, null,"Cuenca"));
		facade.registerUser(registration);

		Registration registration2 = new Registration(new User("login2", "password", "email@email.com", "complete name",
				"description", "fb url", "tw url", "personal url", false, null,"Cuenca"));
		facade.registerUser(registration2);
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

	@Test
	@UsingDataSet("users.xml")
	@ShouldMatchDataSet("userJohnModified.xml")
	public void testModifyUser() throws EmailDuplicateException {
		User user = UsersDataset.modifiedUser();
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.update(user));
	}

	@Test
	@UsingDataSet("users.xml")
	@ShouldMatchDataSet("users.xml")
	public void testModifyUserPasswordNotChange() throws EmailDuplicateException {
		User user = UsersDataset.existentUser();
		principal.setName(user.getLogin());
		user.changePassword(UsersDataset.passwordFor(user.getLogin()));
		asUser.throwingRun(() -> facade.update(user));
	}

	@Test(expected=EmailDuplicateException.class)
	@UsingDataSet({"users.xml", "registrations.xml"})
	public void testModifyUserDuplicatedEmail() throws EmailDuplicateException {
		User user = UsersDataset.existentUser();
		principal.setName(user.getLogin());
		user.setEmail("anne@email.com");
		asUser.throwingRun(() -> facade.update(user));
	}
	
	@Test
	@UsingDataSet("users.xml")
	public void testGetUsers() {
		final User user = userWithLogin("kurt");	   
	    principal.setName(user.getLogin());	
	    
	    asAdmin.throwingRun(() -> assertThat(
	    	facade.getUsers().size(), is(equalTo(6))));
	}
	
	@Test(expected=EJBTransactionRolledbackException.class)
	@UsingDataSet("users.xml")
	public void testGetUsersByUser() {
		final User user = userWithLogin("anne");	   
	    principal.setName(user.getLogin());	
	    
	    asUser.throwingRun(() -> facade.getUsers());
	}
	
	@Test
	@UsingDataSet("users.xml")
	public void testRemoveUser() {
		final User user = userWithLogin("kurt");	   
	    principal.setName(user.getLogin());	
	    
	    asAdmin.throwingRun(() -> facade.removeUser("anne"));
	}
	
	@Test(expected=EJBTransactionRolledbackException.class)
	@UsingDataSet("users.xml")
	public void testRemoveNullUser() {
		final User user = userWithLogin("kurt");	   
	    principal.setName(user.getLogin());	
	    
	    asAdmin.throwingRun(() -> facade.removeUser(""));
	}
	
	@Test(expected=EJBTransactionRolledbackException.class)
	@UsingDataSet("users.xml")
	public void testRemoveUserByUser() {
		final User user = userWithLogin("john");	   
	    principal.setName(user.getLogin());	
	    
	    asUser.throwingRun(() -> facade.removeUser("mike"));
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
	
	@Test
	@UsingDataSet("users.xml")
	@ShouldMatchDataSet("friendship-send-user.xml")
	public void testFriendshipRequest() {
		final User user = userWithLogin("john");	   
	    principal.setName(user.getLogin());
		User friend = UsersDataset.existentUserOther();
		asUser.throwingRun(() ->assertThat(facade.sendRequest(friend.getLogin()).getFriend().getLogin(),
				is(equalTo(friend.getLogin()))
    			));
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-send-user.xml"})
	@ShouldMatchDataSet({"users.xml","friendship-send-user.xml"})
	public void testReceivedFriendRequestList() {
		final User user = userWithLogin("mary");
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> assertThat(facade.friendRequestList(), hasSize(lessThan(2))));
    			
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-send-user.xml"})
	@ShouldMatchDataSet({"users.xml","friendship-accept-user.xml"})
	public void testAcceptFriendRequest() {
		final User user = userWithLogin("mary");
		final User friend = UsersDataset.existentUser();
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.acceptOrRejectFriendRequest(friend.getLogin(), true));
    			
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-send-user.xml"})
	@ShouldMatchDataSet({"users.xml","friendship-reject-user.xml"})
	public void testRejectFriendRequest() {
		final User user = userWithLogin("mary");
		final User friend = UsersDataset.existentUser();
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.acceptOrRejectFriendRequest(friend.getLogin(), false));
    			
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-accept-user.xml"})
	@ShouldMatchDataSet("users.xml")
	public void testRemoveFriendShip() {
		User user = userWithLogin("john");
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.removeFriendship("mary"));
    			
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-accept-user.xml"})
	@ShouldMatchDataSet("users.xml")
	public void testRemoveReversedFriendShip() {
		User user = userWithLogin("mary");
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.removeFriendship("john"));
    			
	}
	
	@Test
	@UsingDataSet({"users.xml","friendship-accept-user.xml"})
	@ShouldMatchDataSet({"users.xml","friendship-cancel-user.xml"})
	public void testCancelledFriendShip() {
		User user = userWithLogin("john");
		principal.setName(user.getLogin());
		asUser.throwingRun(() -> facade.cancelFriendship("mary"));	
	}
	
	@Test
	@UsingDataSet("users.xml")
	@ShouldMatchDataSet("users.xml")
	public void testSearchUser(){
		User user = userWithLogin("john");
		principal.setName(user.getLogin());
		asUser.throwingRun(() ->assertThat(facade.searchUser("mary"), hasSize(1)));
	}
	
	
}