package es.uvigo.esei.dgss.letta.jsf;

import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.newUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.passwordFor;
import static es.uvigo.esei.dgss.letta.mail.RegexMatcher.matches;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.jsf.pages.IndexPage;
import es.uvigo.esei.dgss.letta.jsf.pages.RegisterPage;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.jsf.util.mail.TestingMailer;
import es.uvigo.esei.dgss.letta.mail.Email;
import es.uvigo.esei.dgss.letta.mail.MailBox;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

/**
 * {@linkplain RegisterUserControllerTest} is an arquillian test case to test
 * the JSF ManagedBean {@link RegisterUserController}.
 *
 * @author abmiguez
 * @author bcgonzalez3
 *
 */
@RunWith(Arquillian.class)
public class RegisterUserControllerTest {
	private static final Path WEBAPP = Paths.get("src/main/webapp");
	private static final String UUID_PATTERN =
		"[a-zA-Z0-9]{8}(-[a-zA-Z0-9]{4}){3}-[a-zA-Z0-9]{12}";

	@Drone
	private WebDriver browser;
	
	@Page
	private IndexPage indexPage;
	
	@Inject
	private TestingMailer mailer;
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
			.create(WebArchive.class, "test.war")
			.addPackage(JoinEventController.class.getPackage())
			.addPackage(UserEJB.class.getPackage())
			.addPackage(User.class.getPackage())
			.addPackage(LoginDuplicateException.class.getPackage())
			.addPackage(IndexPage.class.getPackage())
			.addPackage(EventMappings.class.getPackage())
			.addPackage(WebDriver.class.getPackage())
			.addPackage(MailBox.class.getPackage())
			.addClasses(Mailer.class, TestingMailer.class)
			.addAsWebResource(WEBAPP.resolve("index.xhtml").toFile(),
					"index.xhtml")
			.addAsWebResource(WEBAPP.resolve("register.xhtml").toFile(),
					"register.xhtml")
			.addAsWebResource(
					WEBAPP.resolve("template/templateLayout.xhtml")
							.toFile(), "template/templateLayout.xhtml")
			.addAsWebResource(
					WEBAPP.resolve("template/templateHeader.xhtml")
							.toFile(), "template/templateHeader.xhtml")
			.addAsWebResource(
					WEBAPP.resolve("template/templateFooter.xhtml")
							.toFile(), "template/templateFooter.xhtml")
			.addAsWebResource(
					WEBAPP.resolve("template/templateContent.xhtml")
							.toFile(), "template/templateContent.xhtml")
			.addAsResource("test-persistence.xml",
					"META-INF/persistence.xml")
			.addAsWebInfResource("jboss-web.xml")
			.addAsWebInfResource("web.xml")
			.addAsWebInfResource("beans-mailer.xml","beans.xml")
			.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces").withoutTransitivity().asSingleFile());
	}
	
	@Before
	public void setUp() {
		if (this.browser != null)
			this.browser.manage().deleteAllCookies();
	}
	
	@Test
	@InSequence(1)
	@UsingDataSet("registrations.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoLoginSuccess() {
	}
	
	@Test
	@InSequence(2)
	@RunAsClient
	public void testDoRegisterSuccess(@InitialPage RegisterPage registerPage) {
		final User newUser = newUser();
		
		registerPage.register(newUser.getLogin(), passwordFor(newUser), newUser.getEmail());
		indexPage.assertOnIt();
	}
	
	@Test
	@InSequence(3)
	@ShouldMatchDataSet(value="registrations-create.xml",excludeColumns="UUID")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoRegisterSuccess() {
		final User newUser = newUser();
		
		assertThat(mailer.getEmails(), is(not(empty())));
		
		final Email email = mailer.getLastEmail();
		assertThat(email.getTo(), is(equalTo(newUser.getEmail())));
		assertThat(email.getMessage(), matches(".*/confirm\\.xhtml\\?uuid=" + UUID_PATTERN + ".*"));
		
		mailer.clear();
	}
	
	@Test
	@InSequence(11)
	@UsingDataSet("registrations.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoRegisterFail() {
	}
	
	@Test
	@InSequence(12)
	@RunAsClient
	public void testDoRegisterFail(@InitialPage RegisterPage registerPage) {
		registerPage.register("anne", "short", "wrongemail");
		registerPage.assertOnRegisterPage();
	}

	@Test
	@InSequence(13)
	@ShouldMatchDataSet("registrations.xml")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoRegisterFail() {
		assertThat(mailer.getEmails(), is(empty()));
		
		mailer.clear();
	}
}
