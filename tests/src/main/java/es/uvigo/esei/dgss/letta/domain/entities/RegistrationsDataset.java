package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.newUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;
import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility test class that contains a set of {@link Registration} entities. This
 * dataset is replicated in the {@code registration*.xml} dataset files.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public class RegistrationsDataset {
	/**
	 * Returns a list of {@link Registration} with the same user data as the
	 * contained in the {@link UsersDataset#users()} users. This registrations
	 * should exist in the database.
	 * 
	 * @return a list of {@link Registration} with the same user data as the
	 * contained in the {@link UsersDataset#users()} users.
	 */
	public static Registration[] registrations() {
		return stream(users())
			.map(user -> new Registration(user, uuidForUser(user.getLogin())))
		.toArray(Registration[]::new);
	}
	
	/**
	 * Returns a new registration with the same user data as the
	 * {@link UsersDataset#newUser()} user.
	 * 
	 * @return a new registration with the same user data as the
	 * {@link UsersDataset#newUser()} user.
	 */
	public static Registration newRegistration() {
		final User newUser = newUser();
		
		return new Registration(newUser, uuidForUser(newUser.getLogin()));
	}
	
	/**
	 * Returns the UUID for a used that should exists in the dataset.
	 * 
	 * @param login the login of a user existent in the registration.
	 * @return the UUID for a user.
	 */
	public static String uuidForUser(String login) {
		final Map<String, String> uuids = new HashMap<>();
		uuids.put("john", "00000000-0000-0000-0000-000000000000");
		uuids.put("anne", "11111111-0000-0000-0000-000000000000");
		uuids.put("mary", "22222222-0000-0000-0000-000000000000");
		uuids.put("joan", "33333333-0000-0000-0000-000000000000");
		uuids.put("mike", "44444444-0000-0000-0000-000000000000");
		uuids.put("bart", "55555555-0000-0000-0000-000000000000");
		
		return uuids.get(login);
	}
	
	/**
	 * Returns a registration that should exist in the database.
	 * 
	 * @return a registration that should exist in the database.
	 */
	public Registration existentRegistration() {
		return registrations()[0];
	}
	
	/**
	 * Returns a registration that should not exist in the database.
	 * 
	 * @return a registration that should not exist in the database.
	 */
	public Registration nonExistentRegistration() {
		return new Registration(nonExistentUser());
	}
}
