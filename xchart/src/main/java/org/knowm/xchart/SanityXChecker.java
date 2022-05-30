package org.knowm.xchart;

import java.util.List;

public class SanityXChecker extends SanityCheckDecorator {

	private List<?> xData;
	
	public SanityXChecker(SanityChecker schecker, List<?> xData) {
		super(schecker);
		this.setxData(xData);
	}
	
	@Override
	public void checkSanity() {
		super.checkSanity();
	    if (getxData() == null) {
	        throw new IllegalArgumentException("X-Axis data cannot be null!!!");
	    }
	    if (getxData().size() == 0) {
	    	throw new IllegalArgumentException("X-Axis data cannot be empty!!!");
	    }
	}

	private List<?> getxData() {
		return xData;
	}

	private void setxData(List<?> xData) {
		this.xData = xData;
	}
}
