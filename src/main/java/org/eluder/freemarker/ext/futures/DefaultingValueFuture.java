package org.eluder.freemarker.ext.futures;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultingValueFuture<T> extends DelegatingFuture<T> {

    private final T defaultValue;

    public DefaultingValueFuture(final Future<T> delegate, final T defaultValue) {
        super(delegate);
        this.defaultValue = defaultValue;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return super.get();
        } catch (TimeoutingFuture.RuntimeTimeoutException ex) {
            return defaultValue;
        }
    }

    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return super.get(timeout, unit);
        } catch (TimeoutingFuture.RuntimeTimeoutException | TimeoutException ex) {
            return defaultValue;
        }
    }
}
