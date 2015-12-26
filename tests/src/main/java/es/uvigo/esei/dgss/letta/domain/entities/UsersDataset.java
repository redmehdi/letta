package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Arrays.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Utility test class that contains a set of {@link User} entities. This
 * dataset is replicated in the {@code user*.xml} dataset files.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public final class UsersDataset {
	private UsersDataset() {}
	
	/**
	 * A list of five users that should exists in the database. The users data
	 * is the following:
	 * <table border="1">
	 *   <caption>User's data</caption>
	 *   <tr>
	 *     <th>Login</th> 
	 *     <th>Password</th> 
	 *     <th>Email</th>
	 *   </tr>
	 *   <tr>
	 *     <td>john</td> 
	 *     <td>johnpass</td> 
	 *     <td>john@email.com</td>
	 *   </tr>
	 *   <tr>
	 *     <td>anne</td> 
	 *     <td>annepass</td> 
	 *     <td>anne@email.com</td>
	 *   </tr>
	 *   <tr>
	 *     <td>mary</td> 
	 *     <td>marypass</td> 
	 *     <td>mary@email.com</td>
	 *   </tr>
	 *   <tr>
	 *     <td>joan</td> 
	 *     <td>joanpass</td> 
	 *     <td>joan@email.com</td>
	 *   </tr>
	 *   <tr>
	 *     <td>mike</td> 
	 *     <td>mikepass</td> 
	 *     <td>mike@email.com</td>
	 *   </tr>
	 * </table>
	 * 
	 * @return a list of five users.
	 */
	public static User[] users() {
		return new User[] {
				new User("john", "johnpass", "john@email.com", "john name", "john description",
						"https://www.facebook.com/john", "https://twitter.com/john", "https://johnpersonal.com/", false, null, "Cuenca"),
				new User("anne", "annepass", "anne@email.com", "anne name", "anne description",
						"https://www.facebook.com/anne", "https://twitter.com/anne", "https://annepersonal.com/", false, null,"Alicante"),
				new User("mary", "marypass", "mary@email.com", "mary name", "mary description",
						"https://www.facebook.com/mary", "https://twitter.com/mary", "https://marypersonal.com/", false, null,"Ourense"),
				new User("joan", "joanpass", "joan@email.com", "joan name", "joan description",
						"https://www.facebook.com/joan", "https://twitter.com/joan", "https://joanpersonal.com/", false, null,"Cáceres"),
				new User("mike", "mikepass", "mike@email.com", "mike name", "mike description",
						"https://www.facebook.com/mike", "https://twitter.com/mike", "https://mikepersonal.com/", true, null,"Granada"),
				new User("kurt", "kurtpass", "kurt@email.com", "kurt name", "kurt description",
						"https://www.facebook.com/kurt", "https://twitter.com/kurt", "https://kurtpersonal.com/", false, null,"Gerona")		
		};
	}
	
	/**
	 * Returns the {@link #users()} list of users without the users with the
	 * provided logins.
	 * 
	 * @param logins a list of logins of the users to be excluded from the
	 * returned values.
	 * @return the {@link #users()} list of users without the users with the
	 * provided logins.
	 */
	public static User[] usersWithout(String ... logins) {
		final Predicate<User> isNotInLogins = user -> stream(logins)
			.noneMatch(login -> login.equals(user.getLogin()));
		
		return stream(users())
			.filter(isNotInLogins)
		.toArray(User[]::new);
	}
	
	/**
	 * Returns a user from the {@link #users()} list with the provided login.
	 *  
	 * @param login the login of the user to be returned.
	 * @return a user from the {@link #users()} list with the provided login.
	 * @throws IllegalArgumentException if there is not any user with the
	 * provided login.
	 */
	public static User userWithLogin(String login) {
		return Arrays.stream(users())
			.filter(user -> user.getLogin().equals(login))
			.findAny()
		.orElseThrow(() -> new IllegalArgumentException("There is not any user with login " + login));
	}
	
	/**
	 * Returns the MD5 of a password. Only values are returned for passwords
	 * used in this dataset.
	 * 
	 * @param password a password to be converted into MD5.
	 * @return the MD5 of the provided password if it is used in this class or
	 * {@code null} otherwise.
	 */
	public static String md5OfPassword(String password) {
		final Map<String, String> md5s = new HashMap<>();
		md5s.put("johnpass", "3bffe7a2bc163d273184e8902afe66b7");
		md5s.put("annepass", "1afee8bef2d82a3bef6f52b2614f16ab");
		md5s.put("marypass", "a5446c2cfe2b8a015caa8a7e825bb8af");
		md5s.put("joanpass", "ce8a660555a5701617403c77f6654d65");
		md5s.put("mikepass", "b1b668f82813956ef1fe9688e6c05011");
		md5s.put("bartpass", "f67884174ac391b5a6c409715d16ba60");
		md5s.put("nonepass", "16348ade4c752d71b257924b04b06a94");
		md5s.put("kurtpass", "81252633317d5b5bc1ab9feb1bd3af69");

		
		return md5s.get(password);
	}
	
	/**
	 * Returns the plain password for a given user.
	 * 
	 * @param user the user whose password will be returned. 
	 * @return the plain password of the user.
	 */
	public static String passwordFor(User user) {
		return passwordFor(user.getLogin());
	}
	
	/**
	 * Returns the plain password for a given user's login.
	 * 
	 * @param login the login of the user whose password will be returned. 
	 * @return the plain password of the user.
	 */
	public static String passwordFor(String login) {
		return login + "pass";
	}
	
	/**
	 * Returns the login of a user that should exist in the database. This is
	 * the login of the user returned by {@link #existentUser()}.
	 * 
	 * @return the login of a user that should exist in the database.
	 */
	public static String existentLogin() {
		return existentUser().getLogin();
	}
	
	/**
	 * Returns an user that should exist in the database.
	 * 
	 * @return an user that should exist in the database.
	 */
	public static User existentUser() {
		return users()[0];
	}
	
	/**
	 * Returns an user whose role is ADMIN
	 * @return an user that should exist in the database with the role of an ADMIN
	 */
	public static User adminUser(){
		return users()[5];
	}
	
	/**
	 * Returns an user that should not exist in the database.
	 * 
	 * @return an user that should not exist in the database.
	 */
	public static User nonExistentUser() {
		return new User("none", "nonepass", "none@email.com", "none name", "none description",
				"https://facebook.com/none", "https://twitter.com/none", "https://nonepersonal.com/", false, null,"Bilbao");
	}
	
	/**
	 * Returns an user that should not exist in the database and that may be
	 * used to create a new user.
	 * 
	 * @return an user that should not exist in the database.
	 */
	public static User newUser() {
		return new User("bart", "bartpass", "bart@email.com", "bart name", "bart description",
				"https://www.facebook.com/bart", "https://twitter.com/bart", "https://bartpersonal.com/", false, null,"Badajoz");
	}
	
	/**
	 * Returns an user that should not exist in the database and that may be
	 * used to create a new user.
	 * 
	 * @return an user that should not exist in the database.
	 */
	public static User modifiedUser() {
		return new User("john", "annepass", "johnModified@email.com", "john name Modified", "john description Modified",
				"https://www.facebook.com/johnModified", null, "https://johnpersonal.com/", false, null,"Cuenca");
	}
}
