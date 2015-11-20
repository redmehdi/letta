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
	 * @return the value returned by {@code supplier}.
	 * @throws Throwable if the {@code supplier} throws an exception.
	 */
	public <V> V throwingCall(ThrowingSupplier<V> supplier) throws Throwable;

	/**
	 * Calls an runnable that returns no value and may throw an exception.
	 * 
	 * @param runnable the runnable to be called.
	 * @throws Throwable if the {@code runnable} throws an exception.
	 */
	public void throwingRun(ThrowingRunnable runnable) throws Throwable;

	/**
	 * A function that can throw an exception.
	 * 
	 * @author Miguel Reboiro Jato
	 * 
	 * @param <V> the type of the returned value.
	 */
	public interface ThrowingSupplier<V> {
		/**
		 * Executes a task that returns a value.
		 * 
		 * @return the value returned by the task. 
		 * @throws Throwable wildcard exception to allow throwing any exception.
		 */
		public V get() throws Throwable;
	}
	
	/**
	 * A runnable that can throw an exception.
	 * 
	 * @author Miguel Reboiro Jato
	 */
	public interface ThrowingRunnable {
		/**
		 * Executes a task that does not return a value.
		 * 
		 * @throws Throwable wildcard exception to allow throwing any exception. 
		 */
		public void run() throws Throwable;
	}
}
