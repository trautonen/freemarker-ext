package org.eluder.freemarker.ext.futures;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import org.eluder.freemarker.ext.GeneralPurposeFailingModel;
import org.eluder.freemarker.ext.TypedModelFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureModelFactory implements TypedModelFactory<Future> {

    private final Long timeout;
    private final boolean failForTimeout;

    public FutureModelFactory() {
        this(true);
    }

    public FutureModelFactory(final boolean failForTimeout) {
        this(null, failForTimeout);
    }

    public FutureModelFactory(final Long timeout, boolean failForTimeout) {
        this.timeout = timeout;
        this.failForTimeout = failForTimeout;
    }

    @Override
    public Class<Future> getType() {
        return Future.class;
    }

    @Override
    public TemplateModel create(final Object object, final ObjectWrapper wrapper) {
        final Future future = getType().cast(object);
        try {
            return wrapper.wrap(get(future));
        } catch (TimeoutingFuture.RuntimeTimeoutException ex) {
            return timeout(ex.getCause());
        } catch (TimeoutException ex) {
            return timeout(ex);
        } catch (Throwable th) {
            return new GeneralPurposeFailingModel(th);
        }
    }

    private TemplateModel timeout(final Throwable ex) {
        return (failForTimeout ? new GeneralPurposeFailingModel(ex) : TemplateModel.NOTHING);
    }

    private Object get(final Future future) throws Throwable {
        try {
            if (timeout != null) {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } else {
                return future.get();
            }
        } catch (ExecutionException ex) {
            throw ex.getCause();
        }
    }
}
