package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.Role.USER;

public class UsersFriendshipParameters {
	
	 /**
     * Returns a valid user.
     *
     * @return a valid user.
     */
    public static User anSenderRequest() {
        return new User("john", "johnpass", "john@email.com", USER);
    }

    /**
     * Returns a valid user different from {@link #anReceiverRequest()}.
     *
     * @return a valid user different from {@link #anReceiverRequest()}.
     */
    public static User anReceiverRequest() {
        return new User("anne", "annepass", "anne@email.com", USER);
    }

}
