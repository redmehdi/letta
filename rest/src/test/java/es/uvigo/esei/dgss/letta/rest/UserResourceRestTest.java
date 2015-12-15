package es.uvigo.esei.dgss.letta.rest;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.util.adapters.LocalDateTimeAdapter;
import es.uvigo.esei.dgss.letta.domain.util.converters.LocalDateTimeConverter;
import es.uvigo.esei.dgss.letta.rest.util.filters.CORSFilter;
import es.uvigo.esei.dgss.letta.rest.util.mappers.IllegalArgumentExceptionMapper;
import es.uvigo.esei.dgss.letta.rest.util.mappers.SecurityExceptionMapper;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;

import static javax.ws.rs.core.Response.Status.OK;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class UserResourceRestTest {

	private static final String BASE_PATH = "api/private/user/";
	private static final String ID_EVENT_JOIN = "/15";
	private static final String NON_ID_EVENT_JOIN = "/16";
	private static final String EXISTENT_LOGIN = "anne";
	private static final String NON_EXISTENT_LOGIN = "annen";
	private static final String BASIC_AUTHORIZATION = "Basic YW5uZTphbm5lcGFzcw==";
	private static final String NON_BASIC_AUTHORIZATION = "Basic YW5uZW46YW5uZW5wYXM=";

	private static final GenericType<List<Event>> asEventList = new GenericType<List<Event>>() {
	};

	@Deployment
	public static Archive<WebArchive> deploy() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClass(UserResource.class)
				.addClass(UserAuthorizationEJB.class).addClass(UserEJB.class)
				.addPackage(IllegalArgumentExceptionMapper.class.getPackage())
				.addPackage(LocalDateTimeAdapter.class.getPackage())
				.addPackage(LocalDateTimeConverter.class.getPackage())
				.addClasses(CORSFilter.class,
						IllegalArgumentExceptionMapper.class,
						SecurityExceptionMapper.class)
				.addPackages(true, EventEJB.class.getPackage())
				.addPackages(true, Event.class.getPackage())
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("jboss-web.xml")
				.addAsWebInfResource("web.xml");
	}

	@Test
	@InSequence(10)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestGetCreatedEvents() {
	}

	@Test
	@InSequence(11)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	public void testGetCreatedEvents(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/created") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(OK));
		assertThat(response.readEntity(asEventList), hasSize(5));
	}

	@Test
	@InSequence(12)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml" })
	public void afterTestGetCreatedEvents() {
	}

	@Test
	@InSequence(21)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEvents() {
	}

	@Test
	@InSequence(22)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	public void testGetJoinedEvents(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/joined") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(OK));
		assertThat(response.readEntity(asEventList), hasSize(10));
	}

	@Test
	@InSequence(23)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEvents() {
	}

	@Test
	@InSequence(31)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestJoinEvent() {
	}

	@Test
	@InSequence(32)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	public void testJoinEvent(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ ID_EVENT_JOIN) final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().post(null);
		assertThat(response, hasHttpStatus(OK));
	}

	@Test
	@InSequence(33)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml",
			"anne-attends-event-15.xml" })
	public void afterTestJoinEvent() {
	}

	@Test
	@InSequence(41)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(SecurityException.class)
	public void testNonExistentOwner(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/created") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(51)
	@RunAsClient
	@Header(name = "Authorization", value = NON_BASIC_AUTHORIZATION)
	@ShouldThrowException(SecurityException.class)
	public void testNonExistentAuthorization(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/created") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(61)
	@UsingDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
	public void beforeTestEventAlreadyJoined() {
	}

	@Test
	@InSequence(62)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(EventAlredyJoinedException.class)
	public void testEventAlreadyJoined(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ ID_EVENT_JOIN) final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(63)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml",
			"anne-attends-event-15.xml" })
	public void afterTestEventAlreadyJoined() {
	}

	@Test
	@InSequence(71)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(NullPointerException.class)
	public void testNonExistentEventToJoin(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ NON_ID_EVENT_JOIN) final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(81)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectStartNumber() {
	}

	@Test
	@InSequence(82)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(IllegalArgumentException.class)
	public void testGetJoinedEventsIncorrectStartNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=-1") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(83)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectStartNumber() {
	}

	@Test
	@InSequence(91)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectCountNumber() {
	}

	@Test
	@InSequence(92)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(IllegalArgumentException.class)
	public void testGetJoinedEventsIncorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?count=-1") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(93)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectCountNumber() {
	}

	@Test
	@InSequence(101)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() {
	}

	@Test
	@InSequence(102)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(IllegalArgumentException.class)
	public void testGetJoinedEventsIncorrectStartNumberCorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=-1&count=0") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(103)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() {
	}

	@Test
	@InSequence(111)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(112)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(IllegalArgumentException.class)
	public void testGetJoinedEventsCorrectStartNumberIncorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=0&count=-1") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(113)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(121)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(122)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(IllegalArgumentException.class)
	public void beforeTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=0&count=-1") final ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(123)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() {
	}

}
