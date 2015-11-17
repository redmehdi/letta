package es.uvigo.esei.dgss.letta.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.Event;

@RunWith(Arquillian.class)
@CleanupUsingScript({ "cleanup.sql" })
public class EventEJBTest {
	@Inject
	private EventEJB facade;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClass(EventEJB.class)
				.addPackage(Event.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("test-persistence.xml",
						"persistence.xml");
	}

	@Test
	public void testGetFrontPageEmpty() {
		final List<Event> frontPage = facade.getFrontPage();
		assertThat(frontPage, is(empty()));
	}

	@Test
	@UsingDataSet("events.xml")
	@ShouldMatchDataSet("events.xml")
	public void testGetFrontPageEquals20() {
		final List<Event> frontPage = facade.getFrontPage();
		assertThat(frontPage.size(), is(equalTo(20)));
	}

	@Test
	@UsingDataSet("events-less-than-twenty.xml")
	@ShouldMatchDataSet("events-less-than-twenty.xml")
	public void testGetFrontPageLessThan20() {
		final List<Event> frontPage = facade.getFrontPage();
		assertThat(frontPage.size(), is(lessThan(20)));
	}

	@Test
	public void testGetFrontPageHighlightsEmpty() {
		final List<Event> frontPage = facade.getFrontPageHighlights();
		assertThat(frontPage, is(empty()));
	}

	@Test
	@UsingDataSet("events.xml")
	@ShouldMatchDataSet("events.xml")
	public void testGetFrontPageHighlightsEquals5() {
		final List<Event> frontPage = facade.getFrontPageHighlights();
		assertThat(frontPage.size(), is(equalTo(5)));
	}

	@Test
	@UsingDataSet("events-less-than-five.xml")
	@ShouldMatchDataSet("events-less-than-five.xml")
	public void testGetFrontPageHighlightsLessThan5() {
		final List<Event> frontPage = facade.getFrontPageHighlights();
		assertThat(frontPage.size(), is(lessThan(5)));
	}

}
