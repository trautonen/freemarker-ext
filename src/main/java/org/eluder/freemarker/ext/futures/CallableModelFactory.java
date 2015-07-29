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
