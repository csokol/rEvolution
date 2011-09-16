package br.com.caelum.revolution.gui.commandline;

import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.revolution.analyzers.AnalyzerRunner;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;


public class Launcher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);
	
	public static void main(String[] args) throws Exception {
		if(args.length==0) throw new Exception("missing config file");
		
		InputStream configStream = new FileInputStream(args[0]);
		Config config = new PropertiesConfig(configStream);
		
		log.info("rEvolution");
		log.info("starting...");

		if(onlyVisualizations(args)) {
			AnalyzerRunner analyzerRunner = new AnalyzerFactory().basedOn(config);
			analyzerRunner.start();
		}
		
		VisualizationRunner visualizationRunner = new VisualizationFactory().basedOn(config);
		visualizationRunner.start();
		
		log.info("FINISHED!");
	}

	private static boolean onlyVisualizations(String[] args) {
		return args.length == 1 || (args.length >= 2 && !args[1].equals("--only-visualization"));
	}
}
