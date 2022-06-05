package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

public class AnnotationTextBuilder extends AnnotationWithXYBuilder{

	
  
  public AnnotationTextBuilder(boolean isValueInScreenSpace, double x, double y) {
		super(isValueInScreenSpace, x, y);
	}

  public AnnotationText build() {
	  return new AnnotationText(this);
  }
}
