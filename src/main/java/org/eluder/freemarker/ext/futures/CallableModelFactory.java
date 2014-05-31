package org.eluder.freemarker.ext.futures;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import org.eluder.freemarker.ext.GeneralPurposeFailingModel;
import org.eluder.freemarker.ext.TypedModelFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CallableModelFactory implements TypedModelFactory<Callable> {

    private final ExecutorService executor;

    public CallableModelFactory(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Class<Callable> getType() {
        return Callable.class;
    }

    @Override
    public TemplateModel create(final Object object, final ObjectWrapper wrapper) {
        Callable callable = getType().cast(object);
        try {
            Future future = executor.submit(callable);
            return wrapper.wrap(future);
        } catch (Exception ex) {
            return new GeneralPurposeFailingModel(ex);
        }
    }
}
