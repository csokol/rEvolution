package br.com.caelum.revolution.visualization.common;

import java.text.NumberFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterPlot {

	private final String yTitle;
	private final String xTitle;
	private final String title;
	
	public ScatterPlot(String title, String xTitle, String yTitle) {
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
	}
	
	public JFreeChart build(List<? extends XYPoint> points) {
		JFreeChart chart = ChartFactory.createScatterPlot(title, xTitle, yTitle, createDataSet(points), PlotOrientation.VERTICAL, true, false, false);
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator =
		    new StandardXYItemLabelGenerator("{0}", format, format);
		chart.getXYPlot().getRenderer().setBaseItemLabelsVisible(true);
		chart.getXYPlot().getRenderer().setBaseItemLabelGenerator(generator);
		
		return chart;

	}

	private XYDataset createDataSet(List<? extends XYPoint> points) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (XYPoint point : points) {
			XYSeries series = new XYSeries(point.getLabel());
			series.add(point.getX(), point.getY());
		
			dataset.addSeries(series);
		}

		return dataset;
	}
}
