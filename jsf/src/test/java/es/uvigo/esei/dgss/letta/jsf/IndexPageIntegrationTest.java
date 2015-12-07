package es.uvigo.esei.dgss.letta.jsf;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
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
import es.uvigo.esei.dgss.letta.jsf.pages.EventsList;
import es.uvigo.esei.dgss.letta.jsf.pages.EventsList.EventInfo;
import es.uvigo.esei.dgss.letta.jsf.pages.IndexPage;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static java.util.Objects.nonNull;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class IndexPageIntegrationTest {

    @Drone
    private WebDriver browser;

    @Deployment
    public static Archive<WebArchive> deploy() {
        final Path webApp = Paths.get("src/main/webapp");

        return ShrinkWrap.create(WebArchive.class, "test.war")
              .addClasses(IndexPageController.class, IndexPage.class, EventsList.class, EventInfo.class)
              .addPackage(WebDriver.class.getPackage())
              .addPackages(true, EventMappings.class.getPackage())
              .addPackages(true, EventEJB.class.getPackage())
              .addPackages(true, Event.class.getPackage())
              .addAsWebResource(webApp.resolve("index.xhtml").toFile(), "index.xhtml")
              .addAsWebResource(webApp.resolve("template/templateLayout.xhtml").toFile(), "template/templateLayout.xhtml")
              .addAsWebResource(webApp.resolve("template/templateHeader.xhtml").toFile(), "template/templateHeader.xhtml")
              .addAsWebResource(webApp.resolve("template/templateFooter.xhtml").toFile(), "template/templateFooter.xhtml")
              .addAsWebResource(webApp.resolve("template/templateContent.xhtml").toFile(), "template/templateContent.xhtml")
              .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource("jboss-web.xml")
              .addAsWebInfResource("web.xml")
              .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
              .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces").withoutTransitivity().asSingleFile());
    }

    @Before
    public void setUp() {
        if (nonNull(browser)) browser.manage().deleteAllCookies();
    }

    @Test
    @InSequence(10) // test 1, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestHighlightListShouldContainFiveEvents() { }

    @Test
    @RunAsClient
    @InSequence(11) // test 1, sequence 1
    public void testHighlightListShouldContainFiveEvents(
        @InitialPage final IndexPage index
    ) {
        assertThat(index.getHighlights().getEventInfoList(), hasSize(5));
    }

    @Test
    @InSequence(12) // test 1, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestHighlightListShouldContainFiveEvents() { }

    @Test
    @InSequence(20) // test 2, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestEventListShouldContainTwentyEvents() { }

    @Test
    @RunAsClient
    @InSequence(21) // test 2, sequence 1
    public void testEventListShouldContainTwentyEvents(
        @InitialPage final IndexPage index
    ) {
        assertThat(index.getEventsList().getEventInfoList(), hasSize(20));
    }

    @Test
    @InSequence(22) // test 2, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestEventListShouldContainTwentyEvents() { }

}
