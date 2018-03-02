package com.clyph.guice;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.servlet.ServletContainer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ClyphGuiceServletConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    Injector injector = Guice.createInjector(
        new ServletModule(){
          @Override
          protected void configureServlets() {
            Map<String,String> initParams = new HashMap<String,String>();
            initParams.put("javax.ws.rs.Application", ClyphResourceConfig.class.getName());

            bind(ServletContainer.class).in(Singleton.class);

            serve("/api/*").with(ServletContainer.class, initParams);
          }

        },
        new ClyphFrontendModule()
        );

    return injector;
  }
}
