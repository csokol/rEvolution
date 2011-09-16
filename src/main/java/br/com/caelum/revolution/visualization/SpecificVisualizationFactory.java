package br.com.caelum.revolution.visualization;

import br.com.caelum.revolution.config.Config;

public interface SpecificVisualizationFactory {
	Visualization build(Config config);
}
