package es.uvigo.esei.dgss.letta.service.util.security;

import java.util.function.Supplier;

import javax.ejb.Local;

/**
 * Utility class that allows the invocation of method with a specific role.
 * 
 * @author Miguel Reboiro-Jato
 *
 */
@Local
public interface RoleCaller {
	/**
	 * Calls a function that returns a value.
	 * 
	 * @param supplier the function to be called.
	 * @param <V> the type of the returned value.
	 * @return the value returned by {@code supplier}.
	 */
	public <V> V call(Supplier<V> supplier);

	/**
	 * Calls an runnable that returns no value.
	 * 
	 * @param runnable the runnable to be called.
	 */
	public void run(Runnable runnable);

	/**
	 * Calls a function that returns a value and may throw an exception.
	 * 
	 * @param supplier the function to be called.
	 * @param <V> the type of the returned value.
	 * @param <E> the type of the throwable exception.
	 * @return the value returned by {@code supplier}.
	 * @throws E if the {@code supplier} throws an exception.
	 */
	public <V, E extends Throwable> V throwingCall(
	    ThrowingSupplier<V, E> supplier
	) throws E;

	/**
	 * Calls an runnable that returns no value and may throw an exception.
	 *
	 * @param <E> the type of the throwable exception.
	 * @param runnable the runnable to be called.
	 * @throws E if the {@code runnable} throws an exception.
	 */
	public <E extends Throwable >void throwingRun(
	    ThrowingRunnable<E> runnable
	) throws E;

	/**
	 * A function that can throw an exception.
	 * 
	 * @author Miguel Reboiro Jato
	 * 
	 * @param <V> the type of the returned value.
	 * @param <E> the type of the throwable exception.
	 */
	public interface ThrowingSupplier<V, E extends Throwable> {
		/**
		 * Executes a task that returns a value.
		 *
		 * @return the value returned by the task.
		 * @throws E wildcard exception to allow throwing any exception.
		 */
		public V get() throws E;
	}
	
	/**
	 * A runnable that can throw an exception.
	 * 
	 * @author Miguel Reboiro Jato
	 *
	 * @param <E> the type of the throwable exception.
	 */
	public interface ThrowingRunnable<E extends Throwable> {
		/**
		 * Executes a task that does not return a value.
		 *
		 * @throws E wildcard exception to allow throwing any exception.
		 */
		public void run() throws E;
	}
}
