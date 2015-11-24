package es.uvigo.esei.dgss.letta.jsf;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.jsf.pages.ConfirmPage;
import es.uvigo.esei.dgss.letta.jsf.pages.MainPage;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.exceptions.LoginDuplicateException;
import es.uvigo.esei.dgss.letta.service.mail.Mailer;

@RunWith(Arquillian.class)
public class ConfirmUserControllerTest {
	private static final Path WEBAPP = Paths.get("src/main/webapp");

	@Drone
	private WebDriver browser;
	
	@Page
	private MainPage mainPage;
	
	@Page
	private ConfirmPage confirmPage;
	
    @ArquillianResource
    private URL baseURL;
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
			.addPackage(ConfirmUserController.class.getPackage())
			.addPackage(UserEJB.class.getPackage())
			.addPackage(User.class.getPackage())
            .addPackage(LoginDuplicateException.class.getPackage())
            .addPackage(MainPage.class.getPackage())
            .addPackage(WebDriver.class.getPackage())
            .addPackage(Mailer.class.getPackage())
			.addAsWebResource(WEBAPP.resolve("index.xhtml").toFile(), "index.xhtml")
            .addAsWebResource(WEBAPP.resolve("confirm.xhtml").toFile(), "confirm.xhtml")
			.addAsWebResource(WEBAPP.resolve("template/templateLayout.xhtml").toFile(), "template/templateLayout.xhtml")
			.addAsWebResource(WEBAPP.resolve("template/templateHeader.xhtml").toFile(), "template/templateHeader.xhtml")
			.addAsWebResource(WEBAPP.resolve("template/templateFooter.xhtml").toFile(), "template/templateFooter.xhtml")
			.addAsWebResource(WEBAPP.resolve("template/templateContent.xhtml").toFile(), "template/templateContent.xhtml")
			.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
			.addAsWebInfResource("jboss-web.xml")
			.addAsWebInfResource("web.xml")
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Before
	public void setUp() {
		if (this.browser != null)
			this.browser.manage().deleteAllCookies();
	}
	
	@Test @InSequence(1)
	@UsingDataSet("registrations.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoConfirmEmptyUuid() {}
	
	@Test @InSequence(2)
	@RunAsClient
	public void testDoConfirmEmptyUuid() {
		this.browser.get(baseURL + "/faces/confirm.xhtml?uuid=");		
		this.mainPage.waitForMainPage();
		this.mainPage.assertOnMainPage();
	}
	
	@Test @InSequence(3)
	@ShouldMatchDataSet("registrations.xml")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoConfirmEmptyUuid() {}
	
	
	@Test @InSequence(11)
	@UsingDataSet("registrations.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoConfirmBadUuid() {}
	
	@Test @InSequence(12)
	@RunAsClient
	public void testDoConfirmBadUuid() {
		this.browser.get(baseURL + "/faces/confirm.xhtml?uuid=00000000-0000-0000-0000-000000000001");		
		this.mainPage.waitForMainPage();
		this.mainPage.assertOnMainPage();
	}
	
	@Test @InSequence(13)
	@ShouldMatchDataSet("registrations.xml")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoConfirmBadUuid() {}
	
	@Test @InSequence(21)
	@UsingDataSet("registrations.xml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void beforeDoConfirmGoodUuid() {}
	
	@Test @InSequence(22)
	@RunAsClient
	public void testDoConfirmGoodUuid() {
		this.browser.get(baseURL + "/faces/confirm.xhtml?uuid=00000000-0000-0000-0000-000000000000");		
		this.mainPage.waitForMainPage();
		this.mainPage.assertOnMainPage();
	}
	
	@Test @InSequence(23)
	@ShouldMatchDataSet("registrations-register-user.xml")
	@CleanupUsingScript({ "cleanup.sql" })
	public void afterDoConfirmGoodUuid() {}

}