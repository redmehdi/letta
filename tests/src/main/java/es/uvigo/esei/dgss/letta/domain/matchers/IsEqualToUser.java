package es.uvigo.esei.dgss.letta.domain.matchers;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import es.uvigo.esei.dgss.letta.domain.entities.User;

/**
 * Matcher that compares two {@link User} entities by their attributes.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public class IsEqualToUser extends IsEqualToEntity<User> {
	/**
	 * Constructs a new instance of {@link IsEqualToUser}.
	 *
	 * @param user the expected user.
	 */
	public IsEqualToUser(User user) {
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
	 * Factory method that creates a new {@link IsEqualToEntity} matcher with
	 * the provided {@link User} as the expected value.
	 * 
	 * @param user the expected user.
	 * @return a new {@link IsEqualToEntity} matcher with the provided
	 * {@link User} as the expected value.
	 */
	@Factory
	public static IsEqualToUser equalsToUser(User user) {
		return new IsEqualToUser(user);
	}
	
	/**
	 * Factory method that returns a new {@link Matcher} that includes several
	 * {@link IsEqualToUser} matchers, each one using an {@link User} of the
	 * provided ones as the expected value.
	 * 
	 * @param users the users to be used as the expected values.
	 * @return a new {@link Matcher} that includes several
	 * {@link IsEqualToUser} matchers, each one using an {@link User} of the
	 * provided ones as the expected value.
	 * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
	 */
	@Factory
	public static Matcher<Iterable<? extends User>> containsUsersInAnyOrder(User ... users) {
		return containsEntityInAnyOrder(IsEqualToUser::equalsToUser, users);
	}
}
