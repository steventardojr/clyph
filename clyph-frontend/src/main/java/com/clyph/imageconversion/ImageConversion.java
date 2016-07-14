package com.clyph.imageconversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;

@Path("/imageConversion")
public class ImageConversion {
  
  private static final Logger logger = LoggerFactory.getLogger(ImageConversion.class);
  
  private final AmazonS3Client s3Client;
  
  private final String[] CONTENT_TYPES_ARRAY = new String[]{"image/jpeg","image/png", "image/gif", "image/bmp"};
  private final List<String> CONTENT_TYPES = Arrays.asList(CONTENT_TYPES_ARRAY);
  
  public ImageConversion() {
    this.s3Client = new AmazonS3Client(new BasicAWSCredentials("nope", "nope"));
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  public Response get(@HeaderParam("Content-Type") final String contentType, @QueryParam("toFormat") final String toFormat,
      InputStream imageStream) {
    if (imageStream == null || contentType == null || !CONTENT_TYPES.contains(contentType)
        || toFormat == null || toFormat == "") {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    BufferedImage image;
    try {
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      logger.error("Error getting image from input stream", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    if (image == null) {
      logger.error("Couldn't get image from input stream");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    File file = null;
    try {
      file = File.createTempFile("image",  "." + toFormat);
    } catch (IOException e) {
      logger.error("Error creating temporary file", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    OutputStream stream;
    try {
      stream = new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      logger.error("Error opening output stream", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    try {
      ImageIO.write(image, toFormat, stream);
    } catch (Exception e) {
      logger.error("Error converting image", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    String filename = UUID.randomUUID().toString() + "." + toFormat;
    
    PutObjectResult result = null;
    try {
      result = s3Client.putObject("image-conversion", filename, file);
    } catch (Exception e) {
      logger.error("Couldn't put image on S3");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    if (result == null || result.getMetadata() == null) {
      logger.error("Couldn't put image on S3");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    DateTime date = new DateTime().plusSeconds(30);
    URL url = s3Client.generatePresignedUrl("image-conversion", filename, date.toDate());
    
    if (url == null) {
      logger.error("Couldn't get URL from S3");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return Response.ok(url.toString()).type(MediaType.TEXT_HTML).build();
  }
}
