package br.com.caelum.revolution.builds.nobuild;

import br.com.caelum.revolution.builds.Build;
import br.com.caelum.revolution.builds.SpecificBuildFactory;
import br.com.caelum.revolution.config.Config;

public class NoBuildFactory implements SpecificBuildFactory {

	public Build build(Config config) {
		return new NoBuild();
	}
}
