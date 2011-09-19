package br.com.caelum.revolution.visualization.statistic;

import java.math.BigInteger;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsVisualization;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.BarChart;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

@IsVisualization(name="Bugs Per Hour", configs={})
public class BugsPerHourFactory implements SpecificVisualizationFactory{

	public Visualization build(Config config) {
		return new GroupedDataVisualization<BigInteger>(
				new BarChart("Bugs per Hour", "Hours", "Quantity", new MapToDataSetConverter()),
				"select convert(hour(x.date), char) name, count(1) qty from ( select distinct bo.buggedCommit_id, c.date from bugorigin bo inner join modification m on m.id = bo.modification_id inner join commit c on c.id = bo.buggedCommit_id ) x group by hour(x.date) order by hour(x.date)");
		
	}

}
