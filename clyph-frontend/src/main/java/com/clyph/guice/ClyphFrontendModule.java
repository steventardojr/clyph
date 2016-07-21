package com.clyph.guice;

import com.clyph.home.Home;
import com.clyph.imageconversion.ImageConversion;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;


public class ClyphFrontendModule extends AbstractModule {

  @Override
  protected void configure() {
    requestStaticInjection(ClyphResourceConfig.class);

    Multibinder<Object> multibinder = Multibinder.newSetBinder(binder(),Object.class,Names.named("ClyphResource"));

    multibinder.addBinding().to(JacksonJaxbJsonProvider.class);

    multibinder.addBinding().to(Home.class);
    multibinder.addBinding().to(ImageConversion.class);
    
    install(new ClyphBackendModule());
  }

}
