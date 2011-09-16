package br.com.caelum.revolution.visualization.evolution;

import java.math.BigDecimal;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.LineChart;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

public class NumberOfFilesOverTimeFactory implements SpecificVisualizationFactory {

	public Visualization build(Config config) {
		return new GroupedDataVisualization<BigDecimal>(
				new LineChart("Number of Files Per Month in Average over Time", "Commit Date", "Number of Files", new MapToDataSetConverter()), 
				"select convert(concat(month(c.date), '/', year(c.date)), char) name, avg(n.qty) qty from numberoffilespercommit n inner join commit c on n.commit_id = c.id group by month(c.date), year(c.date) order by year(c.date), month(c.date)");

	}
}
