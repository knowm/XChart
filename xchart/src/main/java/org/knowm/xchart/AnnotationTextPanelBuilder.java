package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

public class AnnotationTextPanelBuilder extends AnnotationWithXYBuilder {

	
  
  public AnnotationTextPanelBuilder(boolean isValueInScreenSpace, double x, double y) {
		super(isValueInScreenSpace, x, y);
	}

public AnnotationTextPanel build() {
	  return new AnnotationTextPanel(this);
  }
}
