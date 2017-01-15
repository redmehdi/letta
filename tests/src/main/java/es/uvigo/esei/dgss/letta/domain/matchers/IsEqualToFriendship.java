package es.uvigo.esei.dgss.letta.domain.matchers;
import static java.util.Objects.isNull;
import static java.util.stream.StreamSupport.stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import es.uvigo.esei.dgss.letta.domain.entities.Friendship;

/**
 * {@linkplain IsEqualToFriendship} implements a {@link TypeSafeMatcher} to check
 * for equality of {@link Friendship} objects.
 *
 * @author Redouane Mehdi
 */
public class IsEqualToFriendship extends IsEqualToEntity<Friendship> {

    private final boolean shouldCompareUser;
    private final boolean shouldCompareFriend;

    private IsEqualToFriendship(final Friendship friendship, final boolean shouldCompareUser, final boolean shouldCompareFriend) {
        super(friendship);

        this.shouldCompareUser = shouldCompareUser;
        this.shouldCompareFriend = shouldCompareFriend;
    }
    
	@Override
	protected boolean matchesSafely(final Friendship friendship) {
		clearDescribeTo();
		
		if (isNull(friendship)) {
			addTemplatedDescription("actual", expected.toString());
			return false;
		}
		
		final boolean userMatches = !shouldCompareUser || checkAttribute("user", Friendship::getUser, friendship);
		final boolean friendMatches = !shouldCompareFriend || checkAttribute("friend", Friendship::getFriend, friendship);
		return userMatches && friendMatches
				&& checkAttribute("friendshipState", Friendship::getFriendshipState, friendship);
	}

	@Factory
	public static IsEqualToFriendship equalToFriendship(final Friendship friendship) {
		return new IsEqualToFriendship(friendship, false, false);
	}

	@Factory
	public static IsEqualToFriendship equalToFriendshipWithUserAndFriend(final Friendship friendship) {
		return new IsEqualToFriendship(friendship, true, true);
	}
	
	@Factory
	public static Matcher<Iterable<? extends Friendship>> containsFriendshipsInAnyOrder(
			final Friendship... friendships) {
		return containsEntityInAnyOrder(IsEqualToFriendship::equalToFriendship, friendships);
	}
	
	@Factory
    public static Matcher<Iterable<? extends Friendship>> containsFriendshipsListInAnyOrder(
        final Iterable<Friendship> friendships
    ) {
        return containsEntityInAnyOrder(
            IsEqualToFriendship::equalToFriendship,
            stream(friendships.spliterator(), false).toArray(Friendship[]::new)
        );
    }
	
	@Factory
    public static Matcher<Iterable<? extends Friendship>> containsFriendshipsInOrder(
        final Friendship ... friendships
    ) {
        return containsEntityInOrder(IsEqualToFriendship::equalToFriendship, friendships);
    }
	
	@Factory
    public static Matcher<Iterable<? extends Friendship>> containsFriendshipsListInOrder(
        final Iterable<Friendship> friendships
    ) {
        return containsEntityInOrder(
            IsEqualToFriendship::equalToFriendship,
            stream(friendships.spliterator(), false).toArray(Friendship[]::new)
        );
    }
	
	@Factory
    public static Matcher<Iterable<? extends Friendship>> containsFriendshipsWithUserInAnyOrder(
        final Friendship ... friendships
    ) {
        return containsEntityInAnyOrder(
            IsEqualToFriendship::equalToFriendshipWithUserAndFriend, friendships
        );
    }

}
