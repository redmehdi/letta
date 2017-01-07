package es.uvigo.esei.dgss.letta.domain.entities;

public class FriendshipDataset {
	
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
	
	
	public static Friendship newFriendShip() {
    	return new Friendship(
    			FriendshipState.PENDING, 
    			users()[0], 
    			users()[2]
    					);
    }

}
