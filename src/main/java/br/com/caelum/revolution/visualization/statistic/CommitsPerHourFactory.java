package br.com.caelum.revolution.visualization.statistic;

import java.math.BigInteger;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsVisualization;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.BarChart;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

@IsVisualization(name="Commits per Hour", configs={})
public class CommitsPerHourFactory implements SpecificVisualizationFactory {

	public Visualization build(Config config) {
		return new GroupedDataVisualization<BigInteger>(
				new BarChart("Commits per Hour", "Week Days", "Quantity", new MapToDataSetConverter()),
				"select convert(hour(x.date), char) name, count(1) qty from commit x group by hour(x.date) order by hour(x.date)");

	}

}
