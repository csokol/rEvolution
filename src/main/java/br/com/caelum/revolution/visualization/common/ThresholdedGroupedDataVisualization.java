package br.com.caelum.revolution.visualization.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThresholdedGroupedDataVisualization<T extends Number> extends GroupedDataVisualization<T> {

	private final int threshold;

	public ThresholdedGroupedDataVisualization(Chart chart, int threshold, String sql) {
		super(chart, sql);
		this.threshold = threshold;
	}

	protected Map<Object, Double> convertTo(List<GroupedDataTuple<T>> results) {

		Map<Object, Double> map = new LinkedHashMap<Object, Double>();

		for (GroupedDataTuple<T> tuple : getThresholdFrom(results)) {
			map.put(tuple.getName(), new Double(tuple.getQty().doubleValue()));
		}

		map.put("Others", sumOfTheRest(results));
		
		return map;
	}

	private Double sumOfTheRest(List<GroupedDataTuple<T>> results) {
		double sum = 0;
		for(int i = threshold; i < results.size(); i++) {
			sum += results.get(i).getQty().doubleValue();
		}
		
		return sum;
	}

	private List<GroupedDataTuple<T>> getThresholdFrom(List<GroupedDataTuple<T>> results) {
		List<GroupedDataTuple<T>> list = new ArrayList<GroupedDataTuple<T>>();
		
		for(int i = 0; i < threshold && i+1 <= results.size(); i++) {
			list.add(results.get(i));
		}
		
		return list;
	}


}
