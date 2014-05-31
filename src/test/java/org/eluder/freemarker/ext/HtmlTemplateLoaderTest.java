package org.eluder.freemarker.ext;

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
