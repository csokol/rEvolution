package br.com.caelum.revolution.visualization.statistic;

import java.math.BigInteger;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.BarChart;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

public class CommitsPerWeekDayFactory implements SpecificVisualizationFactory {

	public Visualization build(Config config) {

		//  new File(config.asString("file")), 1500, 1500, 
		return new GroupedDataVisualization<BigInteger>(
				new BarChart("Commits per Week Day", "Week Days", "Quantity", new MapToDataSetConverter()),
				"select dayname(c.date) name, count(1) qty from commit c group by dayname(c.date) order by dayofweek(c.date)");

	}

}
