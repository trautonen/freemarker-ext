package org.eluder.freemarker.ext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.util.ModelFactory;
import freemarker.template.Version;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomizableBeansWrapper extends BeansWrapper {

    private final LinkedHashMap<Class<?>, ModelFactory> modelFactories = new LinkedHashMap<>();

    public CustomizableBeansWrapper(final Version version) {
        super(version);
    }

    public CustomizableBeansWrapper registerModelFactory(final Class<?> type, final ModelFactory modelFactory) {
        modelFactories.put(type, modelFactory);
        return this;
    }

    public CustomizableBeansWrapper registerTypedModelFactory(final TypedModelFactory<?> modelFactory) {
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
