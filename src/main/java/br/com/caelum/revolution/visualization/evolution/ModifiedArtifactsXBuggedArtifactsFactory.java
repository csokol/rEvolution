package br.com.caelum.revolution.visualization.evolution;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.ScatterPlot;
import br.com.caelum.revolution.visualization.common.XYDataVisualization;

public class ModifiedArtifactsXBuggedArtifactsFactory implements
		SpecificVisualizationFactory {

	public Visualization build(Config config) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select aa.name label, ");
		sql.append("convert(( ");
		sql.append("select count(1) from modification m inner join bugorigin bo on bo.modification_id = m.id ");
		sql.append("where m.artifact_id = aa.id ");
		sql.append("), decimal(10,2)) x, ");
		sql.append("convert (( ");
		sql.append("select count(1) from artifact a inner join modification m on m.artifact_id = a.id ");
		sql.append("where a.id = aa.id ");
		sql.append("), decimal(10,2)) y ");
		sql.append("from artifact aa ");
		sql.append("order by y desc");
		
		// new File(config.asString("file")), 1500, 1500
		return new XYDataVisualization(
				new ScatterPlot("Modifications x Number of Bugs", "Number of Bugs", "Number of modifications"),
				sql.toString(),
				config.asInt("threshold"));
	}
}
