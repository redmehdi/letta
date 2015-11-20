package es.uvigo.esei.dgss.letta.http.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * A matcher that compares the HTTP status of a {@link Response} with an
 * expected value. The expected value can be provided as an integer or as a
 * {@link StatusType}.
 * 
 * @author Miguel Reboiro Jato.
 *
 */
public class HasHttpStatus extends TypeSafeMatcher<Response> {
	private StatusType status;
	
	/**
	 * Constructs a new instance of {@link HasHttpStatus}.
	 *
	 * @param status the expected status.
	 */
	public HasHttpStatus(StatusType status) {
		this.status = status;
	}

	/**
	 * Constructs a new instance of {@link HasHttpStatus}.
	 *
	 * @param statusCode the expected status code.
	 */
	public HasHttpStatus(int statusCode) {
		this(Response.Status.fromStatusCode(statusCode));
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue(this.status);
	}

	@Override
	protected boolean matchesSafely(Response item) {
		return this.status.getStatusCode() == item.getStatus();
	}
	
	/**
	 * Factory method that creates a {@link HasHttpStatus} for the provided
	 * status code.
	 * 
	 * @param statusCode the expected status code.
	 * @return a {@link HasHttpStatus} for the provided status code.
	 */
	@Factory
	public static HasHttpStatus hasHttpStatus(int statusCode) {
		return new HasHttpStatus(statusCode);
	}

	/**
	 * Factory method that creates a {@link HasHttpStatus} for the provided
	 * status.
	 * 
	 * @param status the expected status.
	 * @return a {@link HasHttpStatus} for the provided status.
	 */
	@Factory
	public static HasHttpStatus hasHttpStatus(StatusType status) {
		return new HasHttpStatus(status);
	}
}
