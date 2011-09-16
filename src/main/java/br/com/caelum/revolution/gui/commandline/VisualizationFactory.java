package br.com.caelum.revolution.gui.commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PrefixedConfig;
import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.VisualizationNotFoundException;

public class VisualizationFactory {
	
	public VisualizationRunner basedOn(Config config) {
		
		List<VisualizationPlusConfigs> all = new ArrayList<VisualizationPlusConfigs>();
		
		int counter = 1;
		while(config.contains(visualizationConfigName(counter))) {
			String visualizationName = config.asString(visualizationConfigName(counter));
			
			PrefixedConfig specificConfig = new PrefixedConfig(config, visualizationConfigName(counter));
			Visualization visualization = getSpecificFactoryFor(visualizationName).build(specificConfig);
			int height = config.asInt("height");
			int width = config.asInt("width");
			File file = new File(config.asString("width"));
			
			
			all.add(new VisualizationPlusConfigs(visualization, file, width, height));
			
			counter++;
		}

		return new VisualizationRunner(all, new HibernatePersistence(config));
	}
	
	private String visualizationConfigName(int counter) {
		return "visualizations." + counter;
	}

	private SpecificVisualizationFactory getSpecificFactoryFor(
			String visualization) {

		try {
			Class<?> factoryClass = (Class<?>) Class.forName(visualization);
			return (SpecificVisualizationFactory) factoryClass.newInstance();
		} catch (Exception e) {
			throw new VisualizationNotFoundException(e);
		}
	}

}
