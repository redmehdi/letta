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
	
	

//	public Friendship(
//			final FriendshipState friendshipState, 
//			final User user, 
//			final User friend) {
//		this(friendshipState,user,friend);
//	}



	public Friendship(FriendshipState friendshipState, User user, User friend) {
		setFriendshipState(friendshipState);
		setUser(user);
		setFriend(friend);
	}



	/**
	 * Return the state as an {@link Friendship}
	 * 
	 * @return The {@link FriendshipState}
	 */
	public FriendshipState getFriendshipState() {
		return friendshipState;
	}

	/**
	 * changes state of friendship
	 * @param state The new friendshipState as an {@link FriendshipState}. It cannot be null
	 * @throws NullPointerException if a {@code null} state is received
	 */
	public void setFriendshipState(FriendshipState state) throws NullPointerException{
		this.friendshipState = requireNonNull(state, "friendshipState cannot be null.");
	}

	 /**
     * Changes the friend of the user.
     *
     * @param friend The new friend' user as an {@link User}. It cannot be
     *        {@code null}.
     *
     * @throws NullPointerException If a {@code null} friend is received.
     */
	public void setFriend(User friend) throws NullPointerException{
		this.friend = requireNonNull(friend, "friend' user cannot be null.");
	}

	/**
     * Returns the user as an {@link User}.
     *
     * @return The user.
     */
	public User getUser() {
		return user;
	}

	/**
	 * Changes user in friendship
	 * @param user The new user' friend as an {@link User}. It cannot be
     *        {@code null}.
	 * @throws NullPointerException If a {@code null} user is received.
	 */
	public void setUser(User user) throws NullPointerException{
		this.user = requireNonNull(user, "user cannot be null.");
	}

}
