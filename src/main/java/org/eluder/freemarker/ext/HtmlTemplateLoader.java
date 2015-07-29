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

import java.io.IOException;
import java.io.Reader;

import com.google.common.io.CharSource;
import freemarker.cache.TemplateLoader;

public class HtmlTemplateLoader implements TemplateLoader {

    private static final String PROLOGUE = "<#escape x as x?html>";
    private static final String EPILOGUE = "</#escape>";

    private final TemplateLoader delegate;

    public HtmlTemplateLoader(final TemplateLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object findTemplateSource(final String name) throws IOException {
        return delegate.findTemplateSource(name);
    }

    @Override
    public long getLastModified(final Object templateSource) {
        return delegate.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(final Object templateSource, final String encoding) throws IOException {
        return CharSource.concat(
                CharSource.wrap(PROLOGUE),
                new CharSource() {
                    @Override
                    public Reader openStream() throws IOException {
                        return delegate.getReader(templateSource, encoding);
                    }
                },
                CharSource.wrap(EPILOGUE)
        ).openBufferedStream();
    }

    @Override
    public void closeTemplateSource(final Object templateSource) throws IOException {
        delegate.closeTemplateSource(templateSource);
    }
}
