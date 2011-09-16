package br.com.caelum.revolution.visualization.evolution;

import java.math.BigDecimal;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.LineChart;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

public class LinesAddedPerCommitOverTimeFactory implements SpecificVisualizationFactory {

	public Visualization build(Config config) {
		// new File(config.asString("file")), 3000, 1500, 
		return new GroupedDataVisualization<BigDecimal>(
				new LineChart("Lines Added Per Commit over Time", "Commit", "Number of Added Lines", new MapToDataSetConverter()), 
				"select sum(linesAdded) qty, convert(commit_id, char) name from  lineschangedcount l inner join commit c on l.commit_id = c.id group by commit_id order by commit_id");
	}

}
