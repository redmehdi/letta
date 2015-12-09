package es.uvigo.esei.dgss.letta.rest;

import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

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
import es.uvigo.esei.dgss.letta.rest.util.mapper.CORSFilter;
import es.uvigo.esei.dgss.letta.rest.util.mapper.IllegalArgumentExceptionMapper;
import es.uvigo.esei.dgss.letta.rest.util.mapper.SecurityExceptionMapper;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class UserRestTest {

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
				.addClass(UserPrivateRest.class)
				.addClass(UserAuthorizationEJB.class).addClass(UserEJB.class)
				.addPackage(IllegalArgumentExceptionMapper.class.getPackage())
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
	public void beforeTestGetOwnedEvents() {
	}

	@Test
	@InSequence(11)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	public void testGetOwnedEvents(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/created") ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(OK));
		assertThat(response.readEntity(asEventList), hasSize(5));
	}

	@Test
	@InSequence(12)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml" })
	public void afterTestGetOwnedEvents() {
	}

	@Test
	@InSequence(21)
	@UsingDataSet({ "users.xml", "events.xml", "user-joins-event.xml" })
	public void beforeTestGetJoinedEvents() {
	}

	@Test
	@InSequence(22)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	public void testGetJoinedEvents(@ArquillianResteasyResource(BASE_PATH
			+ EXISTENT_LOGIN + "/joined") ResteasyWebTarget webTarget)
					throws Exception {
		final Response response = webTarget.request().get();
		assertThat(response, hasHttpStatus(OK));
		assertThat(response.readEntity(asEventList), hasSize(10));
	}

	@Test
	@InSequence(23)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml", "user-joins-event.xml" })
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
					+ ID_EVENT_JOIN) ResteasyWebTarget webTarget)
							throws Exception {
		final Response response = webTarget.request().post(null);
		assertThat(response, hasHttpStatus(OK));
	}

	@Test
	@InSequence(33)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml",
			"anne-joins-event-15.xml" })
	public void afterTestJoinEvent() {
	}

	@Test
	@InSequence(41)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(SecurityException.class)
	public void testNonExistentOwner(@ArquillianResteasyResource(BASE_PATH
			+ NON_EXISTENT_LOGIN + "/created") ResteasyWebTarget webTarget)
					throws Exception {
	}

	@Test
	@InSequence(51)
	@RunAsClient
	@Header(name = "Authorization", value = NON_BASIC_AUTHORIZATION)
	@ShouldThrowException(SecurityException.class)
	public void testNonExistentAuthorization(
			@ArquillianResteasyResource(BASE_PATH + NON_EXISTENT_LOGIN
					+ "/created") ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(61)
	@UsingDataSet({ "users.xml", "events.xml", "anne-joins-event-15.xml" })
	public void beforeTestEventAlreadyJoined() {
	}

	@Test
	@InSequence(62)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(EventAlredyJoinedException.class)
	public void testEventAlreadyJoined(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ ID_EVENT_JOIN) ResteasyWebTarget webTarget)
							throws Exception {
	}

	@Test
	@InSequence(63)
	@CleanupUsingScript("cleanup.sql")
	@ShouldMatchDataSet({ "users.xml", "events.xml",
			"anne-joins-event-15.xml" })
	public void afterTestEventAlreadyJoined() {
	}

	@Test
	@InSequence(71)
	@RunAsClient
	@Header(name = "Authorization", value = BASIC_AUTHORIZATION)
	@ShouldThrowException(NullPointerException.class)
	public void testNonExistentEventToJoin(
			@ArquillianResteasyResource(BASE_PATH + EXISTENT_LOGIN + "/joined"
					+ NON_ID_EVENT_JOIN) ResteasyWebTarget webTarget)
							throws Exception {
	}

}
