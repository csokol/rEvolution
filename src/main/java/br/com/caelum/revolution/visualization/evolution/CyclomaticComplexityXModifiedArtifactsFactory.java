package br.com.caelum.revolution.visualization.evolution;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.ScatterPlot;
import br.com.caelum.revolution.visualization.common.XYDataVisualization;

public class CyclomaticComplexityXModifiedArtifactsFactory implements
		SpecificVisualizationFactory {

	public Visualization build(Config config) {

		StringBuilder sql = new StringBuilder();
		sql.append("select a.name label, ");
		sql.append("convert( ");
		sql.append("(select cc from cyclomaticcomplexity where artifact_id = aa.id and commit_id=(select id from commit order by date desc limit 0,1)) ");
		sql.append("), decimal(10,2)) x, ");
		sql.append("convert( ");
		sql.append("(select count(1) from artifact a inner join modification m on m.artifact_id = a.id ");
		sql.append("), decimal(10,2)) y ");
		sql.append("from artifact aa ");
		sql.append("order by x desc ");

		//new File(config.asString("file")), 1500, 1500
		return new XYDataVisualization(new ScatterPlot(
				"Cyclomatic Complexity x Number of Modifications",
				"Cyclomatic Complexity", "Number of modifications"), sql.toString(),
				config.asInt("threshold"));

	}
}
