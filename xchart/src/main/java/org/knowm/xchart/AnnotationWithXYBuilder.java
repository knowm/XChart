package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.internal.chartpart.AnnotationBuilder;

import java.awt.image.BufferedImage;
public abstract class AnnotationWithXYBuilder extends AnnotationBuilder<AnnotationWithXY> {
	
	//required parameters
	protected double x;
    protected double y;
  
    //optional parameters
    private List<String> lines;
    private String text;
    private BufferedImage image;
    
  public AnnotationWithXYBuilder(boolean isValueInScreenSpace, double x, double y){
	  super(isValueInScreenSpace);
	  this.x = x;
	  this.y = y;
  }
  
  public AnnotationWithXYBuilder setLines(String lines) {
      this.lines = Arrays.asList(lines.split("\\n"));
      return this;
  }

  public AnnotationWithXYBuilder setText(String text) {
      this.text = text;
      return this;
  }
  
  public AnnotationWithXYBuilder setImage(BufferedImage image) {
      this.image = image;
      return this;
  }
  
  public double getX() {
	    return this.x;
  }
  
  public double getY() {
	    return this.y;
  }
  
  public List<String> getLines() {
	    return this.lines;
  }
  
  public String getText() {
	    return this.text;
  }
  
  public BufferedImage getImage() {
	    return this.image;
  }
 }
