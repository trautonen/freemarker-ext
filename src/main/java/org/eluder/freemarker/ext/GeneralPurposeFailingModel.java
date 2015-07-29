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
