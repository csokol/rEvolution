package br.com.caelum.revolution.visualization.common;

import java.util.Map;

import org.jfree.chart.JFreeChart;

public interface Chart {
	JFreeChart build(Map<Object, Double> data);
}
