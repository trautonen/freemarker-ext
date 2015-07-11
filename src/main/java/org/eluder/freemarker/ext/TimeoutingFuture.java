package org.eluder.freemarker.ext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutingFuture<T> implements Future<T> {
    
    private final Future<T> delegate;
    private final long timeoutMillis;

    public TimeoutingFuture(final Future<T> delegate, final long timeoutMillis) {
        this.delegate = delegate;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException, RuntimeTimeoutException {
        try {
            return get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            throw new RuntimeTimeoutException("Future self timeout " + timeoutMillis + " milliseconds exceeded", ex);
        }
    }

    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }
    
    public static class RuntimeTimeoutException extends RuntimeException {
        public RuntimeTimeoutException(final String message, final TimeoutException cause) {
            super(message, cause);
        }
    }
}
