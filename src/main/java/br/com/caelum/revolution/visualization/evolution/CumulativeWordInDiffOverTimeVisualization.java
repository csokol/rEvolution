package br.com.caelum.revolution.visualization.evolution;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.Chart;

public class CumulativeWordInDiffOverTimeVisualization implements
		Visualization {

	private Session session;
	private final Chart chart;
	private final String name;

	public CumulativeWordInDiffOverTimeVisualization(String name, Chart chart) {
		this.name = name;
		this.chart = chart;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public void exportTo(OutputStream os, int width, int height) {
		List<WordDiffInCommit> diffs = (List<WordDiffInCommit>) session
				.createSQLQuery(
						"select convert(sum(added), signed integer) added, convert(sum(removed), signed integer) removed, c.id from diffwordcount d inner join commit c on c.id = d.commit_id where d.name = '"
								+ name + "' group by c.id order by c.id")
				.setResultTransformer(
						Transformers.aliasToBean(WordDiffInCommit.class))
				.list();

		Map<Object, Double> data = new LinkedHashMap<Object, Double>();

		double current = 0;
		for (WordDiffInCommit diff : diffs) {
			current += (diff.getAdded().subtract(diff.getRemoved()).intValue());
			data.put(diff.getId(), current);
		}

		JFreeChart finalChart = chart.build(data);
		
		try {
			ChartUtilities.writeChartAsJPEG(os, finalChart, width, height);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
