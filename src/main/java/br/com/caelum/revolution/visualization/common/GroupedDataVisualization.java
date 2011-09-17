package br.com.caelum.revolution.visualization.common;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import br.com.caelum.revolution.visualization.Visualization;

public class GroupedDataVisualization<T extends Number> implements Visualization {

	private Session session;
	private final Chart chart;
	private final String sql;

	public GroupedDataVisualization(Chart chart, String sql) {
		this.chart = chart;
		this.sql = sql;
	}

	protected Query buildQuery() {
		Query query = session
				.createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(GroupedDataTuple.class));
		return query;
	}

	protected Map<Object, Double> convertTo(List<GroupedDataTuple<T>> results) {

		Map<Object, Double> map = new LinkedHashMap<Object, Double>();

		for (GroupedDataTuple<T> tuple : results) {
			map.put(tuple.getName(), new Double(tuple.getQty().doubleValue()));
		}

		return map;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public void exportTo(OutputStream os, int width, int height) {
		Query query = buildQuery();
		List<GroupedDataTuple<T>> results = query.list();

		JFreeChart finalChart = chart.build(convertTo(results));
		
		try {
			ChartUtilities.writeChartAsJPEG(os, finalChart, width, height);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		

	}

}
