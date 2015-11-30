package es.uvigo.esei.dgss.letta.jsf;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.eventWithId;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ejb.EJB;

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
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.jsf.pages.IndexPage;
import es.uvigo.esei.dgss.letta.jsf.pages.LoginPage;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.util.mail.Mailer;

/**
 * {@linkplain JoinEventControllerTest} is an arquillian test case to test the
 * JSF ManagedBean {@link JoinEventController}.
 *
 * @author abmiguez
 * @author apsoto
 *
 */
@RunWith(Arquillian.class)
public class JoinEventControllerTest {
	private static final Path WEBAPP = Paths.get("src/main/webapp");

	@Drone
	private WebDriver browser;
	
	@EJB 
	private EventEJB eventEJB;
	
	@Page
	private IndexPage indexPage;
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
			.create(WebArchive.class, "test.war")
			.addPackages(true, JoinEventController.class.getPackage())
			.addPackage(UserEJB.class.getPackage())
			.addPackage(User.class.getPackage())
			.addPackage(LoginDuplicateException.class.getPackage())
			.addPackage(IndexPage.class.getPackage())
			.addPackage(EventMappings.class.getPackage())
			.addPackage(WebDriver.class.getPackage())
			.addPackage(Mailer.class.getPackage())
			.addAsWebResource(WEBAPP.resolve("index.xhtml").toFile(),
					"index.xhtml")
			.addAsWebResource(WEBAPP.resolve("login.xhtml").toFile(),
					"login.xhtml")
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
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
			.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces").withoutTransitivity().asSingleFile());
	}
	
	@Before
	public void setUp() {
		if (this.browser != null)
			this.browser.manage().deleteAllCookies();
	}
	
	@Test
	@InSequence(1)
	@UsingDataSet("user-joins-event.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoJoinEventNeverJoin() {
	}

	@Test
	@InSequence(2)
	@RunAsClient
	public void testDoJoinEventNeverJoin(@InitialPage LoginPage loginPage) {
		loginPage.login("anne", "annepass");
		
		final Event event = eventWithId(10);
		indexPage.joinEvent(event);
		indexPage.assertOnJoinedTrue();		
	}

	@Test
	@InSequence(3)
	@ShouldMatchDataSet({ "user-joins-event.xml", "anne-joins-event-10.xml" })
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoJoinEventNeverJoin() {
	}
	
	@Test
	@InSequence(11)
	@UsingDataSet("user-joins-event.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoJoinEventAlreadyJoin() {
	}

	@Test
	@InSequence(12)
	@RunAsClient
	public void testDoJoinEventAlreadyJoin(@InitialPage LoginPage loginPage) {
		loginPage.login("anne", "annepass");
		
		final Event event = eventWithId(1);
		
		indexPage.joinEvent(event);
		indexPage.assertOnJoinedFalse();
	}

	@Test
	@InSequence(13)
	@ShouldMatchDataSet("user-joins-event.xml")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoJoinEventAlreadyJoin() {
	}
}
