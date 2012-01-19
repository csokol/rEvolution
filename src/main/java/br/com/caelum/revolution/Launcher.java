package br.com.caelum.revolution;

import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.revolution.analyzers.AnalyzerFactory;
import br.com.caelum.revolution.analyzers.AnalyzerRunner;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;

public class Launcher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("missing config file");

		log.info("rEvolution");
		log.info("starting...");

		AnalyzerRunner analyzerRunner = new AnalyzerFactory()
				.basedOn(config(args));
		analyzerRunner.start();

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

}
