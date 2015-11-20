package es.uvigo.esei.dgss.letta.domain.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * Matcher that compares two {@link Registration} entities by their attributes.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public class IsEqualsToRegistration extends IsEqualsToEntity<Registration> {
	private boolean compareUuid;
	
	/**
	 * Constructs a new instance of {@link IsEqualsToRegistration}. The UUID of
	 * the registrations will not be compared.
	 *
	 * @param registration the expected registration.
	 */
	public IsEqualsToRegistration(Registration registration) {
		this(registration, false);
	}
	
	/**
	 * Constructs a new instance of {@link IsEqualsToRegistration}.
	 *
	 * @param registration the expected registration.
	 * @param compareUuid whether or not the UUID of the registrations should be
	 * compared.
	 */
	public IsEqualsToRegistration(Registration registration, boolean compareUuid) {
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
	 * Factory method that creates a new {@link IsEqualsToEntity} matcher with
	 * the provided {@link Registration} as the expected value. The UUID of the
	 * registrations will be included in the comparison.
	 * 
	 * @param registration the expected registration.
	 * @return a new {@link IsEqualsToEntity} matcher with the provided
	 * {@link Registration} as the expected value.
	 */
	@Factory
	public static IsEqualsToRegistration equalsToRegistration(Registration registration) {
		return new IsEqualsToRegistration(registration, true);
	}

	/**
	 * Factory method that creates a new {@link IsEqualsToEntity} matcher with
	 * the provided {@link Registration} as the expected value. The UUID of the
	 * registrations will be ignored.
	 * 
	 * @param registration the expected registration.
	 * @return a new {@link IsEqualsToEntity} matcher with the provided
	 * {@link Registration} as the expected value.
	 */
	@Factory
	public static IsEqualsToRegistration equalsToRegistrationIgnoringUuid(Registration registration) {
		return new IsEqualsToRegistration(registration);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualsToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value. The UUID
	 * of the registrations will be included in the comparison.
	 * 
	 * @param registrations the registrations to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualsToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value.
	 * @see IsEqualsToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends Registration>> containsRegistrationsInAnyOrder(Registration ... registrations) {
		return containsEntityInAnyOrder(IsEqualsToRegistration::equalsToRegistration, registrations);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualsToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value. The UUID
	 * of the registrations will be ignored.
	 * 
	 * @param registrations the registrations to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualsToRegistration} matchers, each one using an
	 * {@link Registration} of the provided ones as the expected value.
	 * @see IsEqualsToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends Registration>> containsRegistrationsInAnyOrderIgnoringUuid(Registration ... registrations) {
		return containsEntityInAnyOrder(IsEqualsToRegistration::equalsToRegistrationIgnoringUuid, registrations);
	}
}
