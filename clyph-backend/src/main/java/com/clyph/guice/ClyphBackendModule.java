package com.clyph.guice;

import com.google.inject.AbstractModule;
import com.technique.properties.AWSModule;

public class ClyphBackendModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new AWSModule());
  }

}
