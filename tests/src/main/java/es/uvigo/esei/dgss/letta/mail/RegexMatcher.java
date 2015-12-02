package es.uvigo.esei.dgss.letta.mail;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * A Hamcrest matcher to check if string values match a regular expression.
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public class RegexMatcher extends TypeSafeMatcher<String> {
	private final String regex;
	
	/**
	 * Constructs a new instance of {@link RegexMatcher}.
	 *
	 * @param regex the regular expression to match. This regular expression
	 * should follow the rules described in the {@link java.util.regex.Pattern}
	 * class.
	 */
	public RegexMatcher(String regex) {
		this.regex = regex;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue(this.regex);
	}

	@Override
	protected boolean matchesSafely(String item) {
		return item.matches(this.regex);
	}

	/**
	 * Factory method that creates a new instance of {@link RegexMatcher}.
	 * 
	 * @param regex the regular expression to match. This regular expression
	 * should follow the rules described in the {@link java.util.regex.Pattern}
	 * class.
	 * @return a new instance of {@link RegexMatcher}.
	 */
	@Factory
	public static RegexMatcher matches(String regex) {
		return new RegexMatcher(regex);
	}
}
