package es.uvigo.esei.dgss.letta.domain.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersFriendshipParameters.anSenderRequest;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersFriendshipParameters.anReceiverRequest;
import static es.uvigo.esei.dgss.letta.domain.entities.FriendshipState.PENDING;

public class FriendshipTest {
	
	@Test
	public void testConstructor() {
		User user = anSenderRequest();
		User friend = anReceiverRequest();
		Friendship friendship = new Friendship();
		friendship.setFriend(friend);
		friendship.setUser(user);
		friendship.setFriendshipState(PENDING);
		
		assertThat(friendship.getFriend().getLogin(), is(equalTo(friend.getLogin())));
		assertThat(friendship.getUser().getLogin(), is(equalTo(user.getLogin())));
		assertThat(friendship.getFriendshipState(), is(equalTo(PENDING)));
		
	}

}
