package org.eluder.freemarker.ext;

import java.util.List;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

public class GeneralPurposeFailingModel implements TemplateBooleanModel, TemplateScalarModel, TemplateSequenceModel, TemplateHashModelEx, TemplateMethodModelEx {

    private final TemplateModelException exception;

    public GeneralPurposeFailingModel(final Throwable throwable) {
        if (throwable instanceof TemplateModelException) {
            this.exception = (TemplateModelException) throwable;
        } else {
            this.exception = new TemplateModelException(throwable);
        }
    }

    @Override
    public boolean getAsBoolean() throws TemplateModelException {
        throw exception;
    }

    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        throw exception;
    }

    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        throw exception;
    }

    @Override
    public TemplateModel get(final String key) throws TemplateModelException {
        throw exception;
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        throw exception;
    }

    @Override
    public Object exec(final List arguments) throws TemplateModelException {
        throw exception;
    }

    @Override
    public String getAsString() throws TemplateModelException {
        throw exception;
    }

    @Override
    public TemplateModel get(final int index) throws TemplateModelException {
        throw exception;
    }

    @Override
    public int size() throws TemplateModelException {
        throw exception;
    }
}
