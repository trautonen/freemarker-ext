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

import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Map;

public class HtmlTemplateLoaderTest {

    @Test
    public void renderHtmlContent() throws Exception {
        Configuration configuration = new Configuration(FreemarkerExtTestUtils.VERSION);
        configuration.setTemplateLoader(new HtmlTemplateLoader(new ClassTemplateLoader(getClass(), "/")));
        Map<String, String> model = ImmutableMap.of("model", "<a>one&two</a>");
        String content = FreemarkerExtTestUtils.render(configuration, "test.ftl", model);

        Assertions.assertThat(content).isEqualTo("&lt;a&gt;one&amp;two&lt;/a&gt;");
    }

}
