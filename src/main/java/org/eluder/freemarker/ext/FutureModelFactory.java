package org.eluder.freemarker.ext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;

public class FutureModelFactory implements ModelFactory {

    private final Long timeout;
    private final boolean failForTimeout;

    public FutureModelFactory() {
        this(true);
    }
    
    public FutureModelFactory(boolean failForTimeout) {
        this(null, failForTimeout);
    }

    public FutureModelFactory(final Long timeout, boolean failForTimeout) {
        this.timeout = timeout;
        this.failForTimeout = failForTimeout;
    }

    @Override
    public TemplateModel create(final Object object, final ObjectWrapper wrapper) {
        final Future<?> future = (Future<?>) object;
        try {
            return wrapper.wrap(get(future));
        } catch (TimeoutException ex) {
            return (failForTimeout ? new GeneralPurposeFailingModel(ex) : TemplateModel.NOTHING);
        } catch (Throwable th) {
            return new GeneralPurposeFailingModel(th);
        }
    }

    private Object get(final Future<?> future) throws Throwable {
        try {
            if (timeout != null) {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } else {
                return future.get();
            }
        } catch (TimeoutingFuture.RuntimeTimeoutException ex) {
            throw ex.getCause();
        } catch (ExecutionException ex) {
            throw ex.getCause();
        }
    }
}
