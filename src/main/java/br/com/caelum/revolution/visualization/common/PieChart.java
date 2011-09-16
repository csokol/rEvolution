package br.com.caelum.revolution.visualization.common;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

public class PieChart implements Chart {

	private final String title;
	private final MapToDataSetConverter converter;
	
	public PieChart(String title, MapToDataSetConverter converter) {
		this.title = title;
		this.converter = converter;
	}
	
	public JFreeChart build(Map<Object, Double> data) {
		return ChartFactory.createPieChart(title, converter.toPieDataset(data), false, false, false);
	}

}
