package com.clyph.properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AWSModule extends AbstractModule {
  @Override
  protected void configure() {}

  @Provides
  @Singleton
  private AWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials("nope", "sorry");
  }
}
