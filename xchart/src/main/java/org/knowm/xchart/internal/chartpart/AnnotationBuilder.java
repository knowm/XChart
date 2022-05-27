package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

public abstract class AnnotationBuilder<Annotations extends Annotation> {
	
	//required parameters
	protected boolean isValueInScreenSpace;
    
  public AnnotationBuilder(boolean isValueInScreenSpace){
	  this.isValueInScreenSpace = isValueInScreenSpace;
  }
  
  public boolean getIsValueInScreenSpace() {
	    return this.isValueInScreenSpace;
  }
  
  public abstract Annotations build();
}
