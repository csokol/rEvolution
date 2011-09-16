package br.com.caelum.revolution.visualization;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.gui.commandline.VisualizationFactory;

public class VisualizationFactoryTest {

	private Config config;

	@Before
	public void setUp() {
		config = mock(Config.class);
	}
	
	@Test
	public void shouldBuildBasedOnClassName() {
		when(config.asString("visualizations.1")).thenReturn("br.com.caelum.revolution.visualization.FakeVisualizationFactory");
		when(config.contains("visualizations.1")).thenReturn(true);
		assertTrue(new VisualizationFactory().basedOn(config).getVisualizations().get(0).getClass() == FakeVisualization.class);
	}
	
	@Test(expected=VisualizationNotFoundException.class)
	public void shouldWarnIfInvalidBuild() {
		when(config.contains("visualizations.1")).thenReturn(true);
		when(config.asString("visualizations.1")).thenReturn("invalid");
		
		new VisualizationFactory().basedOn(config);		
	}

	
}
