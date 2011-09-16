package br.com.caelum.revolution.visualization.common;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

public class BarChart implements Chart {

	private final String title;
	private final String yTitle;
	private final String xTitle;
	private final MapToDataSetConverter converter;
	
	public BarChart(String title, String xTitle, String yTitle, MapToDataSetConverter converter) {
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.converter = converter;
	}
	
	public JFreeChart build(Map<Object, Double> data) {
		return ChartFactory.createBarChart(title, xTitle, yTitle, converter.toCategoryDataset(title, data), PlotOrientation.VERTICAL, true, false, false);
	}

}
