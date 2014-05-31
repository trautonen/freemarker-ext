package org.eluder.freemarker.ext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.StringWriter;

public class FreemarkerExtTestUtils {

    public static final Version VERSION = Configuration.getVersion();

    public static BeansWrapper createBeansWrapper(BeansWrapper identity) {
        BeansWrapperBuilder builder = new BeansWrapperBuilder(VERSION);
        builder.setOuterIdentity(identity);
        return builder.build();
    }

    public static String render(Configuration configuration, String templateName, Object templateModel) throws Exception {
        Template template = configuration.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(templateModel, writer);
        return writer.toString();
    }

    private FreemarkerExtTestUtils() {
    }
}
