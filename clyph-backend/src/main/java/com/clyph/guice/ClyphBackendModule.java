package com.clyph.guice;

import com.clyph.properties.AWSModule;
import com.google.inject.AbstractModule;

public class ClyphBackendModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new AWSModule());
  }

}
