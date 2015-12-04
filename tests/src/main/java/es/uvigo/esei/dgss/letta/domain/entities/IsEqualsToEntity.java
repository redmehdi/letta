package es.uvigo.esei.dgss.letta.domain.entities;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

/**
 * An abstract {@link Matcher} that can be used to create new matchers that
 * compare entities by their attributes.
 *
 * @author Miguel Reboiro Jato
 *
 * @param <T> the type of the entities to be matched.
 */
public abstract class IsEqualsToEntity<T> extends TypeSafeMatcher<T> {
	/**
	 * The expected entity.
	 */
	protected final T expected;
	
	private Consumer<Description> describeTo;
	
	/**
	 * Constructs a new instance of {@link IsEqualsToEntity}.
	 *
	 * @param entity the expected tentity.
	 */
	public IsEqualsToEntity(T entity) {
		this.expected = entity;
	}

	@Override
	public void describeTo(Description description) {
		if (this.describeTo != null) {
			this.describeTo.accept(description);
		}
	}
	
	/**
	 * Adds a new description using the template:
	 * <p>
	 * {@code <expected class> entity with value '<expected>' for <attribute>}
	 * </p>
	 * 
	 * @param attribute the name of the attribute compared.
	 * @param expected the expected value.
	 */
	protected void addTemplatedDescription(String attribute, Object expected) {
		this.describeTo = d -> d.appendText(String.format(
			"%s entity with value '%s' for %s",
			this.expected.getClass().getSimpleName(),
			expected, attribute
		));
	}
	
	/**
	 * Adds as the description of this matcher the
	 * {@link Matcher#describeTo(Description)} method of other matcher. 
	 * 
	 * @param matcher the matcher whose description will be used.
	 */
	protected void addMatcherDescription(Matcher<?> matcher) {
		this.describeTo = matcher::describeTo;
	}
	
	/**
	 * Cleans the current description.
	 */
	protected void clearDescribeTo() {
		this.describeTo = null;
	}
	
	/**
	 * Compares the expected and the actual value of an attribute. If the
	 * comparison fails, the description of the error will be updated.
	 * 
	 * @param attribute the name of the attribute compared.
	 * @param getter the getter function of the attribute.
	 * @param actual the actual entity being compared to the expected entity.
	 * @param <R> type of the value returned by the getter.
	 * @return {@code true} if the value of the expected and actual attributes
	 * are equals and {@code false} otherwise. If the result is {@code false},
	 * the current description will be updated.
	 */
	protected <R> boolean checkAttribute(String attribute, Function<T, R> getter, T actual) {
		final R expectedValue = getter.apply(this.expected);
		final R actualValue = getter.apply(actual);
		
		if (!expectedValue.equals(actualValue)) {
			this.addTemplatedDescription(attribute, expectedValue);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Utility method that generates a {@link Matcher} that compares several
	 * entities.
	 * 
	 * @param converter a function to create a matcher for an entity.
	 * @param entities the entities to be used as the expected values.
	 * @param <T> type of the entity.
	 * @return a new {@link Matcher} that compares several entities.
	 */
	@SafeVarargs
	protected static <T> Matcher<Iterable<? extends T>> containsEntityInAnyOrder(
		Function<T, Matcher<? super T>> converter, T ... entities
	) {
		final Collection<Matcher<? super T>> ownerMatchers = stream(entities)
			.map(converter)
		.collect(toList());
		
		return containsInAnyOrder(ownerMatchers);
	}

    /**
     * Utility method that generates a {@link Matcher} that compares several
     * entities in the same received order.
     *
     * @param converter A function to create a matcher for an entity.
     * @param entities The entities to be used as the expected values, in the
     *        order to be compared.
     * @param <T> The type of the entity.
     *
     * @return A new {@link Matcher} that compares several entities in the same
     *         received order.
     */
    @SafeVarargs
    protected static <T> Matcher<Iterable<? extends T>> containsEntityInOrder(
        final Function<T, Matcher<? super T>> converter, final T ... entities
    ) {
        return contains(stream(entities).map(converter).collect(toList()));
    }

}
