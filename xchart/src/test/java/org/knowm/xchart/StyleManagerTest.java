package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StyleManagerTest {

	private StyleManager styleManager;
	
	@Before
	public void setUp() {
		styleManager = new StyleManager();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridLinesVisibleTestWithFalse(){
		styleManager.setPlotGridLinesVisible(false);
		assertThat(styleManager.isPlotGridLinesVisible()).isFalse();
		assertThat(styleManager.isPlotGridHorizontalLinesVisible()).isFalse();
		assertThat(styleManager.isPlotGridVerticalLinesVisible()).isFalse();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridLinesVisibleTestWithTrue(){
		styleManager.setPlotGridHorizontalLinesVisible(true);
		assertThat(styleManager.isPlotGridLinesVisible()).isTrue();
		assertThat(styleManager.isPlotGridHorizontalLinesVisible()).isTrue();
		assertThat(styleManager.isPlotGridVerticalLinesVisible()).isTrue();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridHorizontalLinesVisibleTestWithFalse(){
		styleManager.setPlotGridHorizontalLinesVisible(false);
		assertThat(styleManager.isPlotGridHorizontalLinesVisible()).isFalse();
		assertThat(styleManager.isPlotGridLinesVisible()).isFalse();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridHorizontalLinesVisibleTestWithTrue(){
		styleManager.setPlotGridLinesVisible(true);
		assertThat(styleManager.isPlotGridHorizontalLinesVisible()).isTrue();
		assertThat(styleManager.isPlotGridLinesVisible()).isTrue();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridVerticalLinesVisibleTestWithFalse(){
		styleManager.setPlotGridVerticalLinesVisible(false);
		assertThat(styleManager.isPlotGridVerticalLinesVisible()).isFalse();
		assertThat(styleManager.isPlotGridLinesVisible()).isFalse();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void setPlotGridVerticalLinesVisibleTestWithTrue(){
		styleManager.setPlotGridVerticalLinesVisible(true);
		assertThat(styleManager.isPlotGridVerticalLinesVisible()).isTrue();
		assertThat(styleManager.isPlotGridLinesVisible()).isTrue();
	}
	
}
