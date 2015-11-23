package es.uvigo.esei.dgss.letta.service;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

/**
 * Class for test searchEJB.
 * 
 * @author apsoto, aalopez
 *
 */
@RunWith(Arquillian.class)
@CleanupUsingScript({ "cleanup.sql" })
public class SearchEJBTest {
	@Inject
	SearchEJB searchEJB;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClass(SearchEJB.class)
				.addPackage(Event.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("test-persistence.xml", "persistence.xml");
	}

	/**
	 * Method for test single result on titles.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchTitleSingleResult() {
		List<Event> events = searchEJB.searchEvent("Example1 literature");
		assertThat(events.size(), is(equalTo(1)));
	}

	/**
	 * Method for test multiple result on titles.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchTitleMultipleResult() {
		List<Event> events = searchEJB.searchEvent("Example");
		assertThat(events.size(), is(equalTo(25)));
	}

	/**
	 * Method for test single result on description.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchDescriptionSingleResult() {
		List<Event> events = searchEJB.searchEvent("This is a description literature 1");
		assertThat(events.size(), is(equalTo(1)));
	}

	/**
	 * Method for test multiple result on description.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchDescriptionMultipleResult() {
		List<Event> events = searchEJB.searchEvent("This is a description");
		assertThat(events.size(), is(equalTo(25)));
	}

	/**
	 * Method for test on title and description.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchOnTitleAndDescription() {
		List<Event> events = searchEJB.searchEvent("literature");
		assertThat(events.size(), is(equalTo(5)));
	}

	/**
	 * Method for test who receives empty list.
	 */
	@Test
	@UsingDataSet("events.xml")
	public void testSearchNoResultException() {
		List<Event> events = searchEJB.searchEvent("aasdfasdfas");
		assertThat(events.size(), is(equalTo(0)));
	}
}
