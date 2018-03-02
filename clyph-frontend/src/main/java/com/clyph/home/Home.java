package com.clyph.home;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/home")
public class Home {
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String get(@QueryParam("text") final String text) {
    switch(text) {
      case "Don't Click":
        return "WHY DID YOU CLICK?";
      case "WHY DID YOU CLICK?":
        return "Don't click again!";
      case "You Suck":
        return "Don't Click";
      default:
        return "You Suck";
    }
  }
}
