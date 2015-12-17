package es.uvigo.esei.dgss.letta.rest;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.modifiedEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.newEventWithoutCreator;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.nonExistentEvent;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
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

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class UserResourceRestTest {

	private static final String BASE_PATH = "api/private/user/";
	private static final String ID_EVENT_JOIN = "/15";
	private static final String NON_EXISTENT_ID_EVENT_JOIN = "/1000";
	private static final String EXISTENT_LOGIN = "anne";
	private static final String NON_EXISTENT_LOGIN = "annen";
	private static final String EXISTENT_AUTHORIZATION = "Basic YW5uZTphbm5lcGFzcw==";
	private static final String NON_EXISTENT_AUTHORIZATION = "Basic YW5uZW46YW5uZW5wYXM=";

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
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
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
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
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
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
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
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentUserCreated() {
	}

	@Test
	@InSequence(42)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testNonExistentUserCreated(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/created") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(Status.UNAUTHORIZED));
	}

	@Test
	@InSequence(43)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentUserCreated() {
	}

	@Test
	@InSequence(51)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentAuthorizationCreated() {
	}

	@Test
	@InSequence(52)
	@RunAsClient
	@Header(name = "Authorization", value = NON_EXISTENT_AUTHORIZATION)
	public void testNonExistentAuthorizationCreated(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/created") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(Status.UNAUTHORIZED));
	}

	@Test
	@InSequence(53)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentAuthorizationCreated() {
	}

	@Test
	@InSequence(61)
	@UsingDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
	public void beforeTestEventAlreadyJoined() {
	}

	@Test
	@InSequence(62)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testEventAlreadyJoined(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ ID_EVENT_JOIN) final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().post(null);
		assertThat(response, hasHttpStatus(INTERNAL_SERVER_ERROR));

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
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentEventToJoin() {
	}

	@Test
	@InSequence(72)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testNonExistentEventToJoin(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ NON_EXISTENT_ID_EVENT_JOIN) final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().post(null);
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(73)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentEventToJoin() {
	}

	@Test
	@InSequence(81)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() {
	}

	@Test
	@InSequence(82)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testGetJoinedEventsIncorrectStartNumberCorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=-1&count=0") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(83)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() {
	}

	@Test
	@InSequence(91)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(92)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testGetJoinedEventsCorrectStartNumberIncorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=0&count=-1") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(93)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(101)
	@UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void beforeTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(102)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testGetJoinedEventsIncorrectStartNumberIncorrectCountNumber(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/joined?start=-1&count=-1") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(103)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
	public void afterTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() {
	}

	@Test
	@InSequence(111)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentUserJoined() {
	}

	@Test
	@InSequence(112)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testNonExistentUserJoined(@ArquillianResteasyResource(BASE_PATH
			+ NON_EXISTENT_LOGIN + "/joined") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(UNAUTHORIZED));
	}

	@Test
	@InSequence(113)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentUserJoined() {
	}

	@Test
	@InSequence(121)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentAuthorizationJoined() {
	}

	@Test
	@InSequence(122)
	@RunAsClient
	@Header(name = "Authorization", value = NON_EXISTENT_AUTHORIZATION)
	public void testNonExistentAuthorizationJoined(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/joined") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(UNAUTHORIZED));
	}

	@Test
	@InSequence(123)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentAuthorizationJoined() {
	}

	@Test
	@InSequence(131)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestCreateEvent() {
	}

	@Test
	@InSequence(132)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testCreateEvent(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/create") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request()
				.post(json(newEventWithoutCreator()));
		assertThat(response, hasHttpStatus(CREATED));
	}

	@Test
	@InSequence(133)
	@UsingDataSet({ "users.xml", "events.xml", "events-create.xml" })
	public void afterTestCreateEvent() {
	}

	@Test
	@InSequence(141)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentAuthorizationCreateEvent() {
	}

	@Test
	@InSequence(142)
	@RunAsClient
	@Header(name = "Authorization", value = NON_EXISTENT_AUTHORIZATION)
	public void testNonExistentAuthorizationCreateEvent(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/create") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request()
				.post(json(newEventWithoutCreator()));
		assertThat(response, hasHttpStatus(UNAUTHORIZED));
	}

	@Test
	@InSequence(143)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentAuthorizationCreateEvent() {
	}
	
	@Test
	@InSequence(151)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestModifyEvent() {
	}

	@Test
	@InSequence(152)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testModifyEvent(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/modify") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request()
				.put(json(modifiedEvent()));
		assertThat(response, hasHttpStatus(OK));
	}

	@Test
	@InSequence(153)
	@UsingDataSet({ "users.xml", "events-modified.xml" })
	public void afterTestModifyEvent() {
	}

	@Test
	@InSequence(161)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentAuthorizationModifyEvent() {
	}

	@Test
	@InSequence(162)
	@RunAsClient
	@Header(name = "Authorization", value = NON_EXISTENT_AUTHORIZATION)
	public void testNonExistentAuthorizationModifyEvent(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN
					+ "/modify") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request()
				.put(json(modifiedEvent()));
		assertThat(response, hasHttpStatus(UNAUTHORIZED));
	}

	@Test
	@InSequence(163)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentAuthorizationModifyEvent() {
	}
	
	@Test
	@InSequence(171)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestUserNotAuthorizedModifyEvent() {
	}

	@Test
	@InSequence(172)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testUserNotAuthorizedModifyEvent(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/modify") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request()
				.put(json(nonExistentEvent()));
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(173)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestUserNotAuthorizedModifyEvent() {
	}
	
	@Test
	@InSequence(181)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonExistentEventModifyEvent() {
	}

	@Test
	@InSequence(182)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testNonExistentEventModifyEvent(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/modify") final ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request()
				.put(json(nonExistentEvent()));
		assertThat(response, hasHttpStatus(BAD_REQUEST));
	}

	@Test
	@InSequence(183)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonExistentEventModifyEvent() {
	}

	@Test
	@InSequence(191)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void beforeTestNonMatchAuthorizationModifyEvent() {
	}

	@Test
	@InSequence(192)
	@RunAsClient
	@Header(name = "Authorization", value = EXISTENT_AUTHORIZATION)
	public void testNonMatchAuthorizationModifyEvent(
			@ArquillianResteasyResource(BASE_PATH + "jonh"
					+ "/modify") final ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request()
				.put(json(modifiedEvent()));
		assertThat(response, hasHttpStatus(UNAUTHORIZED));
	}

	@Test
	@InSequence(193)
	@UsingDataSet({ "users.xml", "events.xml" })
	public void afterTestNonMatchAuthorizationModifyEvent() {
	}
	
}
