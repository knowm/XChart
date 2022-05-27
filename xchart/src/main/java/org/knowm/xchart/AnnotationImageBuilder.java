package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

public class AnnotationImageBuilder extends AnnotationWithXYBuilder{

	
  
  public AnnotationImageBuilder(boolean isValueInScreenSpace, double x, double y) {
		super(isValueInScreenSpace, x, y);
	}

  public AnnotationImage build() {
	  return new AnnotationImage(this);
  }
}
