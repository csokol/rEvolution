package br.com.caelum.revolution.visualization.statistic;

import java.math.BigInteger;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.BarChart;
import br.com.caelum.revolution.visualization.common.GroupedDataVisualization;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;

public class BugsPerHourInADayOfWeekFactory implements
		SpecificVisualizationFactory {

	public Visualization build(Config config) {

		StringBuilder sql = new StringBuilder();
		sql.append("select convert(hour(x.date), char) name, count(1) qty ");
		sql.append("from ( select distinct bo.buggedCommit_id, c.date from bugorigin bo inner join modification m on m.id = bo.modification_id inner join commit c on c.id = bo.buggedCommit_id ) x ");
		sql.append("where dayname(x.date) = '" + config.asString("weekday") + "' ");
		sql.append("group by hour(x.date) ");
		sql.append("order by hour(x.date) ");

		return new GroupedDataVisualization<BigInteger>(new BarChart(
				"Bugs per Hour in " + config.asString("weekday"), "Hours", "Quantity",
				new MapToDataSetConverter()), sql.toString());
	}

}
