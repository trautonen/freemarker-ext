package org.eluder.freemarker.ext;

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

import freemarker.ext.util.ModelFactory;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Version;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomizableObjectWrapper extends DefaultObjectWrapper {

    private final LinkedHashMap<Class<?>, ModelFactory> modelFactories = new LinkedHashMap<Class<?>, ModelFactory>();

    public CustomizableObjectWrapper(final Version version) {
        super(version);
    }

    public CustomizableObjectWrapper registerModelFactory(final Class<?> type, final ModelFactory modelFactory) {
        modelFactories.put(type, modelFactory);
        return this;
    }

    public CustomizableObjectWrapper registerTypedModelFactory(final TypedModelFactory<?> modelFactory) {
        return registerModelFactory(modelFactory.getType(), modelFactory);
    }
    
    @Override
    protected ModelFactory getModelFactory(final Class clazz) {
        for (Map.Entry<Class<?>, ModelFactory> entry : modelFactories.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return entry.getValue();
            }
        }
        return super.getModelFactory(clazz);
    }
}
