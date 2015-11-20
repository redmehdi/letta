package es.uvigo.esei.dgss.letta.domain.entities;

import static org.apache.commons.lang3.StringUtils.repeat;

import es.uvigo.esei.dgss.letta.domain.entities.User;

/**
 * Test utility class with a set of parameters to be used when testing user
 * entities.
 * 
 * @author Miguel Reboiro Jato
 *
 */
public final class UserParameters {
	private UserParameters() {}
	
	/**
	 * Returns a valid user login.
	 * 
	 * @return a valid user login.
	 */
	public final static String aLogin() {
		return "john";
	}
	
	/**
	 * Returns a valid user login different from {@link #aLogin()}.
	 * 
	 * @return a valid user login different from {@link #aLogin()}.
	 */
	public final static String newLogin() {
		return "johnny";
	}
	
	/**
	 * Returns a valid login with the shortest valid length.
	 * 
	 * @return a valid login with the shortest valid length.
	 */
	public final static String shortestLogin() {
		return "A";
	}
	
	/**
	 * Returns a valid login with the maximum valid length.
	 * 
	 * @return a valid login with the maximum valid length.
	 */
	public final static String longestLogin() {
		return repeat('A', 100);
	}
	
	/**
	 * Returns an empty login (i.e. a empty string).
	 * 
	 * @return an empty login (i.e. a empty string).
	 */
	public final static String emptyLogin() {
		return "";
	}
	
	/**
	 * Returns an invalid login longer than the maximum valid length.
	 * 
	 * @return an invalid login longer than the maximum valid length.
	 */
	public final static String longLogin() {
		return repeat('A', 101);
	}
	
	/**
	 * Returns a valid password.
	 * 
	 * @return a valid password.
	 */
	public final static String aPassword() {
		return "my-password!1234";
	}
	
	/**
	 * Returns the MD5 for the {@link #aPassword()} password.
	 * 
	 * @return the MD5 for the {@link #aPassword()} password.
	 */
	public final static String aPasswordMD5() {
		return "a390173ae9d17e32660c024619557415";
	}
	
	/**
	 * Returns a valid password different from {@link #aPassword()}.
	 * 
	 * @return a valid password different from {@link #aPassword()}.
	 */
	public final static String newPassword() {
		return "my-new-password.5678";
	}
	
	/**
	 * Returns the MD5 for the {@link #newPassword()} password.
	 * 
	 * @return the MD5 for the {@link #newPassword()} password.
	 */
	public final static String newPasswordMD5() {
		return "d370724402aa18338be2b40f3822d40b";
	}
	
	/**
	 * Returns a valid password with the minimum valid length.
	 * 
	 * @return a valid password with the minimum valid length.
	 */
	public final static String shortestPassword() {
		return "AbCdEf";
	}
	
	/**
	 * Returns the MD5 for the {@link #shortestPassword()} password.
	 * 
	 * @return the MD5 for the {@link #shortestPassword()} password.
	 */
	public final static String shortestPasswordMD5() {
		return "90469eb9b9ccaa40fa9c7d0c593a7201";
	}
	
	/**
	 * Returns an invalid password shortest than the minimum valid length.
	 * 
	 * @return an invalid password shortest than the minimum valid length.
	 */
	public final static String shortPassword() {
		return repeat('A', 5);
	}
	
	/**
	 * Returns a valid email.
	 * 
	 * @return a valid email.
	 */
	public final static String anEmail() {
		return "my.test_email%with.all+The-valid.ch4r4ct3rs@MY-email.1234.com";
	}
	
	/**
	 * Returns a valid email different from {@link #newEmail()}.
	 * 
	 * @return a valid email different from {@link #newEmail()}.
	 */
	public final static String newEmail() {
		return "new-email@email.com";
	}
	
	/**
	 * Returns an invalid email.
	 * 
	 * @return an invalid email.
	 */
	public final static String anEmailWithBadFormat() {
		return "this_is_not_an{At}email{Dot}Com";
	}

	/**
	 * Returns a valid user with the {@link #aLogin()}, {@link #aPassword()} and
	 * {@link #anEmail()} parameters.
	 * 
	 * @return a valid user with the {@link #aLogin()}, {@link #aPassword()} and
	 * {@link #anEmail()} parameters.
	 */
	public static User validUser() {
		return new User(aLogin(), aPassword(), anEmail());
	}
}
