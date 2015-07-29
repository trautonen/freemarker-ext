Freemarker extensions
=====================

[![Build Status](https://img.shields.io/travis/trautonen/freemarker-ext.svg?style=flat-square)](https://travis-ci.org/trautonen/freemarker-ext)
[![Coverage Status](https://img.shields.io/coveralls/trautonen/freemarker-ext.svg?style=flat-square)](https://coveralls.io/github/trautonen/freemarker-ext)
[![Dependencies Status](https://img.shields.io/versioneye/d/trautonen/freemarker-ext.svg?style=flat-square)](https://www.versioneye.com/java/org.eluder.freemarker:freemarker-ext)

[Freemarker](http://freemarker.org/) extensions library containing additions such as asynchronous
models and automatic HTML escaping.


### Getting started

Add the dependency to your Maven `pom.xml` or equivalent dependency management configuration.
Release versions are available in [Maven Central](http://search.maven.org/).

```xml
<dependency>
  <groupId>org.eluder.freemarker</groupId>
  <artifactId>freemarker-ext</artifactId>
  <version>0.9-SNAPSHOT</version>
</dependency>
```


### HTML escaping

Freemarker is general purpose template engine, which means it's scope is not only for HTML
templates. Because of this Freemarker **does not** provide automatic HTML escaping, and that can
hit you with XSS vulnerability if you are not aware of this.

The most common solution is to wrap the whole template between `<#escape x as x?html></#escape>`
tag. This library provides `org.eluder.freemarker.ext.HtmlTemplateLoader` that does the wrapping
automatically. Just wrap your own template loader in `HtmlTemplateLoader` and all variables will
be HTML escaped.

```java
Configuration configuration = new Configuration(Configuration.getVersion());
configuration.setTemplateLoader(new HtmlTemplateLoader(new ClassTemplateLoader(getClass(), "/")));
```


### Asynchronous models

Server side templates are usually rendered in synchronous manner. Templates that contain
independent sections that don't have model wide dependencies with each other could be rendered
asynchronously. Such sections could be signed in user information, advertisements, statistics or
some other optional information.

This library provides two ways to render asynchronous models: futures and callables. Futures are
asynchronous computation results and callables are tasks that should be computed asynchronously.
Callables require configuration of an executor service. Biggest caveat with asynchronous models is
that there's not much you can do if the task execution fails. The template is already rendering, so
you can fail the whole rendering process or provide no result at all. No result is the preferred
mode, because then you can still render the template in degraded mode and probably just some non
crucial information won't get rendered.

Configuring support for asynchronous models requires registering a custom `BeansWrapper` and the
desired model factories. `FutureModelFactory` can be used alone, but if callables are needed then
both `FutureModelFactory` and `CallableModelFactory` must be registered. `CallableModelFactory`
transforms tasks into futures and they are then delegated to `FutureModelFactory` for rendering.
Note that you have to take care of properly shutting down the executor when it's not needed
anymore.

```java
// create an executor that will process the callable models
ExecutorService executor = Executors.newFixedThreadPool(10);

// create a beans wrapper that allows registering additional model factories
CustomizableBeansWrapper wrapper = new CustomizableBeansWrapper(Configuration.getVersion())
        .registerTypedModelFactory(new FutureModelFactory())
        .registerTypedModelFactory(new CallableModelFactory(executor));

// create a configuration
Configuration configuration = new Configuration(Configuration.getVersion());
configuration.setObjectWrapper(wrapper);
configuration.setClassForTemplateLoading(getClass(), "/");
```

After this you can use your preferred way of rendering templates with asynchronous models using
the configuration. Just create root model object where normal values are replaced with futures or
callables. Creating the root model will not block execution at all and the execution performance
will increase dramatically the more there is asynchronous variables.

```java
// empty root model
Map<String, Object> model = new HashMap<>();

// set 'futureval' variable as a future from an external service call
model.put("futureval", service.asyncGet());

// set 'callableval' variable as a callable that will be executed with the configured executor
model.put("callableval", () -> { Thread.sleep(500); return 42; });

// render the template with preferred method
render("template.ftl", model);
```


### Continuous integration

Travis CI builds the plugin with Oracle JDK 7 and 8. All successfully built snapshots are deployed
to Sonatype OSS repository. Cobertura is used to gather coverage metrics and the report is
submitted to Coveralls.


### License

The project Freemarker extensions is licensed under the MIT license.
