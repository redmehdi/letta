package es.uvigo.esei.dgss.letta.domain.matchers;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import es.uvigo.esei.dgss.letta.domain.entities.Registration;

/**
 * Matcher that compares two {@link Registration} entities by their attributes.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public class IsEqualToRegistration extends IsEqualToEntity<Registration> {
	private boolean compareUuid;
	
	/**
	 * Constructs a new instance of {@link IsEqualToRegistration}. The UUID of
	 * the registrations will not be compared.
	 *
	 * @param registration the expected registration.
	 */
	public IsEqualToRegistration(Registration registration) {
		this(registration, false);
	}
	
	/**
	 * Constructs a new instance of {@link IsEqualToRegistration}.
	 *
	 * @param registration the expected registration.
	 * @param compareUuid whether or not the UUID of the registrations should be
	 * compared.
	 */
	public IsEqualToRegistration(Registration registration, boolean compareUuid) {
		super(registration);
		this.compareUuid = compareUuid;
	}

	@Override
	protected boolean matchesSafely(Registration actual) {
		this.clearDescribeTo();
		
		if (actual == null) {
			this.addTemplatedDescription("actual", expected.toString());
			return false;
		} else {
			return checkAttribute("login", Registration::getLogin, actual)
				&& checkAttribute("password", Registration::getPassword, actual)
				&& checkAttribute("email", Registration::getEmail, actual)
				&& checkAttribute("role", Registration::getRole, actual)
				&& (!compareUuid || checkAttribute("uuid", Registration::getUuid, actual));
		}
	}

	/**
	 * Factory method that creates a new {@link IsEqualToEntity} matcher with
	 * the provided {@link Registration} as the expected value. The UUID of the
	 * registrations will be included in the comparison.
	 * 
	 * @param registration the expected registration.
	 * @return a new {@link IsEqualToEntity} matcher with the provided
	 * {@link Registration} as the expected value.
	 */
	@Factory
	public static IsEqualToRegistration equalsToRegistration(Registration registration) {
		return new IsEqualToRegistration(registration, true);
	}

	/**
	 * Factory method that creates a new {@link IsEqualToEntity} matcher with
	 * the provided {@link Registration} as the expected value. The UUID of the
	 * registrations will be ignored.
	 * 
	 * @param registration the expected registration.
	 * @return a new {@link IsEqualToEntity} matcher with the provided
	 * {@link Registration} as the expected value.
	 */
	@Factory
	public static IsEqualToRegistration equalsToRegistrationIgnoringUuid(Registration registration) {
		return new IsEqualToRegistration(registration);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value. The UUID
	 * of the registrations will be included in the comparison.
	 * 
	 * @param registrations the registrations to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value.
	 * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends Registration>> containsRegistrationsInAnyOrder(Registration ... registrations) {
		return containsEntityInAnyOrder(IsEqualToRegistration::equalsToRegistration, registrations);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value. The UUID
	 * of the registrations will be ignored.
	 * 
	 * @param registrations the registrations to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value.
	 * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends Registration>> containsRegistrationsInAnyOrderIgnoringUuid(Registration ... registrations) {
		return containsEntityInAnyOrder(IsEqualToRegistration::equalsToRegistrationIgnoringUuid, registrations);
	}
}
