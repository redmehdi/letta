package es.uvigo.esei.dgss.letta.domain.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import es.uvigo.esei.dgss.letta.domain.entities.User;

/**
 * Matcher that compares two {@link User} entities by their attributes.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public class IsEqualsToUser extends IsEqualsToEntity<User> {
	/**
	 * Constructs a new instance of {@link IsEqualsToUser}.
	 *
	 * @param user the expected user.
	 */
	public IsEqualsToUser(User user) {
		super(user);
	}

	@Override
	protected boolean matchesSafely(User actual) {
		this.clearDescribeTo();
		
		if (actual == null) {
			this.addTemplatedDescription("actual", expected.toString());
			return false;
		} else {
			return checkAttribute("login", User::getLogin, actual)
				&& checkAttribute("password", User::getPassword, actual)
				&& checkAttribute("email", User::getEmail, actual)
				&& checkAttribute("role", User::getRole, actual);
		}
	}

	/**
	 * Factory method that creates a new {@link IsEqualsToEntity} matcher with
	 * the provided {@link User} as the expected value.
	 * 
	 * @param user the expected user.
	 * @return a new {@link IsEqualsToEntity} matcher with the provided
	 * {@link User} as the expected value.
	 */
	@Factory
	public static IsEqualsToUser equalsToUser(User user) {
		return new IsEqualsToUser(user);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualsToUser} matchers, each one using an {@link User} of the
	 * provided ones as the expected value.
	 * 
	 * @param users the users to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualsToUser} matchers, each one using an {@link User} of the
	 * provided ones as the expected value.
	 * @see IsEqualsToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends User>> containsUsersInAnyOrder(User ... users) {
		return containsEntityInAnyOrder(IsEqualsToUser::equalsToUser, users);
	}
}
