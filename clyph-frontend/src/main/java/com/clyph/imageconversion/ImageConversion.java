package com.clyph.imageconversion;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

@Path("/imageConversion")
public class ImageConversion {
  
  private static final Logger logger = LoggerFactory.getLogger(ImageConversion.class);
  
  private final ImageConversionService imageConversionService;
  
  private final String[] CONTENT_TYPES_ARRAY = new String[]{"image/jpeg","image/png", "image/gif", "image/bmp"};
  private final List<String> CONTENT_TYPES = Arrays.asList(CONTENT_TYPES_ARRAY);
  
  @Inject
  public ImageConversion(final ImageConversionService imageConversionService) {
    this.imageConversionService = imageConversionService;
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  public Response get(@HeaderParam("Content-Type") final String contentType, @QueryParam("toFormat") final String toFormat,
      InputStream imageStream) {
    if (imageStream == null || contentType == null || !CONTENT_TYPES.contains(contentType)
        || toFormat == null || toFormat == "") {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    String url = null;
    try {
      url = imageConversionService.getUrl(contentType, toFormat, imageStream);
    } catch (Exception e) {
      logger.error("Exception in image conversion process", e);
    }

    return Response.ok(url).type(MediaType.TEXT_HTML).build();
  }
}
