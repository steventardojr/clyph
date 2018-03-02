package com.clyph.guice;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

public class ClyphResourceConfig extends ResourceConfig {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @com.google.inject.Inject public static Injector INJECTOR;
  @com.google.inject.Inject @Named("ClyphResource") public static Set<Object> BOUND_RESOURCES;

  @Inject
  public ClyphResourceConfig(ServiceLocator serviceLocator) {
    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(INJECTOR);

    for(Object obj : BOUND_RESOURCES){
      logger.info("Registering {}", obj);
      register(obj);
    }
  }
}
