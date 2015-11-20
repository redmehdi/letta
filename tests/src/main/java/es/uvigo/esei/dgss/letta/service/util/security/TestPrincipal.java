package es.uvigo.esei.dgss.letta.service.util.security;

import java.security.Principal;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

/**
 * A {@link Principal} stub that allows changing the current principal.
 * 
 * @author Miguel Reboiro Jato
 */
@Alternative
@Singleton
public class TestPrincipal implements Principal {
	private String name;
	
	/**
	 * Constructs a new instance of {@link TestPrincipal}.
	 */
	public TestPrincipal() {}

	/**
	 * Constructs a new instance of {@link TestPrincipal} with the provided user
	 * as the current principal.
	 *
	 * @param name the user to be used as the current principal
	 */
	public TestPrincipal(String name) {
		this.name = name;
	}
	
	/**
	 * Changes the current principal.
	 * 
	 * @param name the name of the new principal.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}