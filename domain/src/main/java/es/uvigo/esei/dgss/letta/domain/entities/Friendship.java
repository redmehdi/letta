package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import es.uvigo.esei.dgss.letta.domain.util.annotations.VisibleForJPA;

/**
 * {@linkplain} is a JPA entity that represents the friendship of the application.
 * 
 * @author world1mehdi
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Friendship implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * Constructs a new instance of {@link Friendship}. This empty constructor is
     * required by the JPA framework and <strong>should never be used
     * directly</strong>.
     */
	@VisibleForJPA public Friendship() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.STRING)
	private FriendshipState friendshipState;
	
	@XmlTransient
	@ManyToOne
	@PrimaryKeyJoinColumn(name = "USERLOGIN", referencedColumnName = "ID")
	private User user;

	@XmlTransient
	@ManyToOne
	@PrimaryKeyJoinColumn(name = "FRIENDLOGIN", referencedColumnName = "ID")
	private User friend;

	/**
     * Returns the friend as an {@link User}.
     *
     * @return The friend.
     */
	public User getFriend() {
		return friend;
	}
	
	/**
	 * 
	 * @param friendshipState the {@link FriendshipState} friendship's state 
	 * @param user The sender {@link User} friend's request
	 * @param friend The receiver {@link User} friend's request
	 * 
	 */
	public Friendship(FriendshipState friendshipState, User user, User friend) {
		setFriendshipState(friendshipState);
		setUser(user);
		setFriend(friend);
	}
	
	/**
	 * 
	 * @return the state {@link FriendshipState}
	 */
	public FriendshipState getFriendshipState() {
		return friendshipState;
	}

	/**
	 * 
	 * @param state {@link FriendshipState}
	 * @throws NullPointerException should not be null
	 */
	public void setFriendshipState(FriendshipState state) throws NullPointerException{
		this.friendshipState = requireNonNull(state, "friendshipState cannot be null.");
	}

	/**
	 * 
	 * @param friend
	 * @throws NullPointerException
	 */
	public void setFriend(User friend) throws NullPointerException{
		this.friend = requireNonNull(friend, "friend' user cannot be null.");
	}

	/**
	 * 
	 * @return the {@link User}
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @param user the {@link User}
	 * @throws NullPointerException
	 */
	public void setUser(User user) throws NullPointerException{
		this.user = requireNonNull(user, "user cannot be null.");
	}

}
