package es.uvigo.esei.dgss.letta.domain.entities;

/**
 * Constants that represent the friendship state in the system.
 * @author world1mehdi
 *
 */

public enum FriendshipState {
	/**
	 * The FriendshipState can be a regular pending, accepted, rejected or cancelled.
	 */
	PENDING("PENDING"),ACCEPTED("ACCEPTED"),REJECTED("REJECTED"),CANCELLED("CANCELLED");
	
	private String state;

	private FriendshipState(String state) {
		this.state = state;
	}
	
	public String toString() {
		return this.state;
	}
	
	
	
}
