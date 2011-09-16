package br.com.caelum.revolution.visualization;

import java.util.List;

import br.com.caelum.revolution.persistence.HibernatePersistence;

public class VisualizationRunner {

	private final HibernatePersistence persistence;
	private final List<Visualization> visualizations;

	public VisualizationRunner(List<Visualization> visualizations, HibernatePersistence persistence) {
		this.visualizations = visualizations;
		this.persistence = persistence;
	}
	
	public List<Visualization> getVisualizations() {
		return visualizations;
	}

	private void putPersistenceOn(Visualization visualization) {
			visualization.setSession(persistence.getSession());
	}

	
	public void start() {
		persistence.initMechanism();
		persistence.openSession();
		
		for(Visualization visualization : visualizations) {
			putPersistenceOn(visualization);
			// TODO: should provide file, width, and height
			//visualization.exportTo(file, width, height);
		}
		
		persistence.close();
	}
}
