package com.clyph.imageconversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.inject.Singleton;

@Singleton
public class ImageConversionService {

  private static final Logger logger = LoggerFactory.getLogger(ImageConversionService.class);

  private final AmazonS3Client s3Client;
  
  public ImageConversionService() {
    this.s3Client = new AmazonS3Client(new BasicAWSCredentials("nope", "sorry"));
  }
  
  public String getUrl(final String contentType, final String toFormat, InputStream imageStream) throws Exception {
    BufferedImage image;
    try {
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      throw new Exception("Error getting image from input stream");
    }
    
    if (image == null) {
      throw new Exception("Couldn't get image from input stream");
    }
    
    File file = null;
    try {
      file = File.createTempFile("image",  "." + toFormat);
    } catch (IOException e) {
      throw new Exception("Error creating temporary file");
    }
    
    OutputStream stream;
    try {
      stream = new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new Exception("Error opening output stream");
    }
    
    try {
      ImageIO.write(image, toFormat, stream);
    } catch (Exception e) {
      throw new Exception("Error converting image");
    }
    
    String filename = UUID.randomUUID().toString() + "." + toFormat;
    
    PutObjectResult result = null;
    try {
      result = s3Client.putObject("image-conversion", filename, file);
    } catch (Exception e) {
      throw new Exception("Couldn't put image on S3");
    }
    
    if (result == null || result.getMetadata() == null) {
      throw new Exception("Couldn't put image on S3");
    }
    
    DateTime date = new DateTime().plusSeconds(300);
    URL url = s3Client.generatePresignedUrl("image-conversion", filename, date.toDate());
    
    if (url == null) {
      throw new Exception("Couldn't get URL from S3");
    }
    
    logger.info("Image conversion successful");
    
    return url.toString();
  }

}
