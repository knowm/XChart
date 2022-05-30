package org.knowm.xchart;

public class SanityCheckDecorator implements SanityChecker {
	private SanityChecker checker;
	
	public SanityCheckDecorator(final SanityChecker schecker) {
		this.checker = schecker;
	}
	
	@Override
	public void checkSanity() {
		checker.checkSanity();
	}

}
