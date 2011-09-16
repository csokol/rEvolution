package br.com.caelum.revolution.visualization.common;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

public class LineChart implements Chart {

	private String title;
	private String xTitle;
	private String yTitle;
	private final MapToDataSetConverter converter;

	public LineChart(String title, String xTitle, String yTitle, MapToDataSetConverter converter) {
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.converter = converter;
	}
	
	public JFreeChart build(Map<Object, Double> data) {
		return ChartFactory.createLineChart(title, xTitle, yTitle, converter.toCategoryDataset(title, data), PlotOrientation.VERTICAL, true, false, false);
	}

	
}
