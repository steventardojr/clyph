package com.technique.properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AWSModule extends AbstractModule {

  @Override
  protected void configure() {}
  
  @Provides
  @Singleton
  private AmazonS3 getAmazonS3Client(AWSCredentials credentials) {
    return new AmazonS3Client(credentials);
  }
  
  @Provides
  @Singleton
  private AWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials("nope", "sorry");
  }
}
