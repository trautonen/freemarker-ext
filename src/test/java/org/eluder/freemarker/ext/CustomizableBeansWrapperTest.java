package org.eluder.freemarker.ext;

import com.google.common.collect.ImmutableMap;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.eluder.freemarker.ext.futures.CallableModelFactory;
import org.eluder.freemarker.ext.futures.DefaultingValueFuture;
import org.eluder.freemarker.ext.futures.FutureModelFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomizableBeansWrapperTest {

    private ExecutorService executor;

    @Before
    public void init() {
        clean();
        executor = Executors.newSingleThreadExecutor();
    }

    @After
    public void clean() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    @Test
    public void unwrapFuture() throws Exception {
        CustomizableBeansWrapper wrapper = new CustomizableBeansWrapper(FreemarkerExtTestUtils.VERSION)
                .registerTypedModelFactory(new FutureModelFactory());
        Configuration configuration = configuration(wrapper);

        Map<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("model", executor.submit(callable(50, 0)))
                .build();

        String content = FreemarkerExtTestUtils.render(configuration, "test.ftl", model);

        assertThat(content).isEqualTo("50");
    }

    @Test
    public void unwrapCallable() throws Exception {
        CustomizableBeansWrapper wrapper = new CustomizableBeansWrapper(FreemarkerExtTestUtils.VERSION)
                .registerTypedModelFactory(new FutureModelFactory())
                .registerTypedModelFactory(new CallableModelFactory(executor));
        Configuration configuration = configuration(wrapper);

        Map<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("model", callable(100, 0))
                .build();

        String content = FreemarkerExtTestUtils.render(configuration, "test.ftl", model);

        assertThat(content).isEqualTo("100");
    }

    @Test
    public void unwrapDefaultingValueFuture() throws Exception {
        CustomizableBeansWrapper wrapper = new CustomizableBeansWrapper(FreemarkerExtTestUtils.VERSION)
                .registerTypedModelFactory(new FutureModelFactory(500L, true));
        Configuration configuration = configuration(wrapper);

        Map<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("model", new DefaultingValueFuture<>(executor.submit(callable(10, 1000)), 20))
                .build();

        String content = FreemarkerExtTestUtils.render(configuration, "test.ftl", model);

        assertThat(content).isEqualTo("20");
    }

    @Test(expected = TemplateModelException.class)
    public void unwrapFailedFuture() throws Exception {
        CustomizableBeansWrapper wrapper = new CustomizableBeansWrapper(FreemarkerExtTestUtils.VERSION)
                .registerTypedModelFactory(new FutureModelFactory(500L, true));
        Configuration configuration = configuration(wrapper);

        Map<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("model", executor.submit(callable(10, 1000)))
                .build();

        String content = FreemarkerExtTestUtils.render(configuration, "test.ftl", model);

        assertThat(content).isEqualTo("20");
    }

    private Configuration configuration(BeansWrapper identity) {
        BeansWrapper wrapper = FreemarkerExtTestUtils.createBeansWrapper(identity);
        Configuration configuration = new Configuration(FreemarkerExtTestUtils.VERSION);
        configuration.setObjectWrapper(wrapper);
        configuration.setClassForTemplateLoading(getClass(), "/");
        return configuration;
    }

    private <V> Callable<V> callable(final V value, final long sleepMillis) {
        return new Callable<V>() {
            @Override
            public V call() throws Exception {
                if (sleepMillis > 0) {
                    Thread.sleep(sleepMillis);
                }
                return value;
            }
        };
    }

}
