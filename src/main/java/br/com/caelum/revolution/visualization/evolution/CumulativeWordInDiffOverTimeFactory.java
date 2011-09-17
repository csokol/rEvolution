package br.com.caelum.revolution.visualization.evolution;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsVisualization;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.BarChart;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

@IsVisualization(name = "Cumulative Word in Diff", configs = { "word" })
public class CumulativeWordInDiffOverTimeFactory implements
		SpecificVisualizationFactory {

	public Visualization build(Config config) {
		return new CumulativeWordInDiffOverTimeVisualization(
				config.asString("word"), new BarChart(
						"Cumulative Appearance of " + config.asString("word"),
						"Commits", "Quantity", new MapToDataSetConverter()));
	}

}
