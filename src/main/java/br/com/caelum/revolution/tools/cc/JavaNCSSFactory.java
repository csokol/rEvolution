package br.com.caelum.revolution.tools.cc;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.executor.SimpleCommandExecutor;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

public class JavaNCSSFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new JavaNCSSTool(config.asString("javancsslib"),
				config.asString("javancsspath"), new JavaNCSSOutputParse(),
				new SimpleCommandExecutor());
	}

}
