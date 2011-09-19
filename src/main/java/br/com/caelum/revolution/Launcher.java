package br.com.caelum.revolution;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.revolution.analyzers.AnalyzerRunner;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;
import br.com.caelum.revolution.gui.commandline.AnalyzerFactory;
import br.com.caelum.revolution.gui.commandline.VisualizationFactory;
import br.com.caelum.revolution.gui.commandline.VisualizationRunner;
import br.com.caelum.revolution.gui.swing.MainUI;

public class Launcher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("missing config file");

		log.info("rEvolution");
		log.info("starting...");

		if (swingGui(args)) {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			new MainUI().setVisible(true);
		} else if (onlyVisualizations(args)) {
			VisualizationRunner visualizationRunner = new VisualizationFactory().basedOn(config(args));
			visualizationRunner.start();
		} else {
			AnalyzerRunner analyzerRunner = new AnalyzerFactory().basedOn(config(args));
			analyzerRunner.start();
		}

		log.info("FINISHED!");
	}

	private static Config config(String[] args) {
		try {
			InputStream configStream = new FileInputStream(args[0]);
			return new PropertiesConfig(configStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean swingGui(String[] args) {
		return args.length == 1 && args[0].equals("--gui");
	}

	private static boolean onlyVisualizations(String[] args) {
		return args.length >= 2 && args[1].equals("--only-visualization");
	}
}
