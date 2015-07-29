package org.eluder.freemarker.ext.futures;

/*
 * #[license]
 * Freemarker Extensions
 * %%
 * Copyright (C) 2014 - 2015 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

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

    public FutureModelFactory(final Long timeout, final boolean failForTimeout) {
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
