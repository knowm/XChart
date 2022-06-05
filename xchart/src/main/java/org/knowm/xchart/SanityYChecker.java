package org.knowm.xchart;

import java.util.List;

public class SanityYChecker implements SanityChecker {
	private List<?> yData;
	
	public SanityYChecker(List<?> yData) {
		setyData(yData);
	}
	
	@Override
	public void checkSanity() {	
		if (getyData() == null) {
			throw new IllegalArgumentException("Y-Axis data connot be null !!!");
		}
		if (getyData().size() == 0) {
			throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
		}
		if (getyData().contains(null)) {
			throw new IllegalArgumentException("Y-Axis data cannot contain null !!!");
		}
	}

	private List<?> getyData() {
		return yData;
	}

	private void setyData(List<?> yData) {
		this.yData = yData;
	}

}
