package es.uvigo.esei.dgss.letta.service.util.security;

import java.util.function.Supplier;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@PermitAll
@RunAs("USER")
@Stateless(name = "user-caller")
public class UserRoleCaller implements RoleCaller {

    @Override
    public <V> V call(final Supplier<V> supplier) {
        return supplier.get();
    }

    @Override
    public void run(final Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V, E extends Throwable> V throwingCall(
        final ThrowingSupplier<V, E> supplier
    ) throws E {
        return supplier.get();
    }

    @Override
    public <E extends Throwable> void throwingRun(
        final ThrowingRunnable<E> runnable
    ) throws E {
        runnable.run();
    }

}
