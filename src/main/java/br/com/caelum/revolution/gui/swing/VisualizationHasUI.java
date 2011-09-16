package br.com.caelum.revolution.gui.swing;

import javax.swing.JPanel;

import org.hibernate.Session;

import br.com.caelum.revolution.config.Config;

public interface VisualizationHasUI {
	JPanel panel(final Config config, final Session session);
	String tabName();
}
