package org.eluder.freemarker.ext;

import java.util.LinkedHashMap;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.util.ModelFactory;

public class CustomizableBeansWrapper extends BeansWrapper {

    private final LinkedHashMap<Class<?>, ModelFactory> modelFactories = new LinkedHashMap<Class<?>, ModelFactory>();
    
    public CustomizableBeansWrapper registerModelFactory(final Class<?> type, final ModelFactory modelFactory) {
        modelFactories.put(type, modelFactory);
        return this;
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
