package br.com.caelum.revolution.gui.commandline;

import java.io.File;

import br.com.caelum.revolution.visualization.Visualization;

public class VisualizationPlusConfigs {

	private final int height;
	private final int width;
	private final File file;
	private final Visualization visualization;

	public VisualizationPlusConfigs(Visualization visualization, File file, int width, int height) {
		this.visualization = visualization;
		this.file = file;
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public File getFile() {
		return file;
	}

	public Visualization getVisualization() {
		return visualization;
	}
	
	
}
