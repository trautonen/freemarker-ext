package org.eluder.freemarker.ext.futures;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutingFuture<T> extends DelegatingFuture<T> {
    
    private final long timeoutMillis;

    public TimeoutingFuture(final Future<T> delegate, final long timeoutMillis) {
        super(delegate);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            throw new RuntimeTimeoutException("Future self timeout " + timeoutMillis + " milliseconds exceeded", ex);
        }
    }

    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(timeout, unit);
    }
    
    public static class RuntimeTimeoutException extends RuntimeException {
        public RuntimeTimeoutException(final String message, final TimeoutException cause) {
            super(message, cause);
        }
    }
}
