package org.eluder.freemarker.ext;

import freemarker.ext.util.ModelFactory;

public interface TypedModelFactory<T> extends ModelFactory {

    Class<T> getType();

}
