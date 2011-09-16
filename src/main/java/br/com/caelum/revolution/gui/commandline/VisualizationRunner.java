package br.com.caelum.revolution.gui.commandline;

import java.io.FileOutputStream;
import java.util.List;

import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.visualization.Visualization;

public class VisualizationRunner {

	private final HibernatePersistence persistence;
	private final List<VisualizationPlusConfigs> visualizations;

	public VisualizationRunner(List<VisualizationPlusConfigs> visualizations,
			HibernatePersistence persistence) {
		this.visualizations = visualizations;
		this.persistence = persistence;
	}

	public List<VisualizationPlusConfigs> getVisualizations() {
		return visualizations;
	}

	private void putPersistenceOn(Visualization visualization) {
		visualization.setSession(persistence.getSession());
	}

	public void start() {
		persistence.initMechanism();
		persistence.openSession();

		for (VisualizationPlusConfigs config : visualizations) {
			putPersistenceOn(config.getVisualization());
			config.getVisualization().exportTo(file(config), config.getWidth(), config.getHeight());
		}

		persistence.close();
	}

	private FileOutputStream file(VisualizationPlusConfigs config) {
		try {
			return new FileOutputStream(config.getFile());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
