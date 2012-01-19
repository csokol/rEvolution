package br.com.caelum.revolution.builds.ant;

import br.com.caelum.revolution.builds.Build;
import br.com.caelum.revolution.builds.SpecificBuildFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.executor.SimpleCommandExecutor;

public class AntFactory implements SpecificBuildFactory {

	public Build build(Config config) {
		return new Ant(
				new SimpleCommandExecutor(), 
				config.asString("ant.task"),
				config.asString("ant.buildPath"));
	}

}
