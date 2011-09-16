package br.com.caelum.revolution.visualization.common;

import java.io.OutputStream;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import br.com.caelum.revolution.visualization.Visualization;

public class XYDataVisualization implements Visualization {

	private Session session;
	private final ScatterPlot chart;
	private final int threshold;
	private final String sql;

	public XYDataVisualization(ScatterPlot chart, String sql, int threshold) {
		this.chart = chart;
		this.sql = sql;
		this.threshold = threshold;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String scalar() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void exportTo(OutputStream os, int width, int height) {

		List<SmallLabeledXYPoint> points = (List<SmallLabeledXYPoint>) session
				.createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(SmallLabeledXYPoint.class))
				.setMaxResults(threshold)
				.list();
		
		JFreeChart finalChart = chart.build(points);
		
		try {
			ChartUtilities.writeChartAsJPEG(os, finalChart, width, height);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
