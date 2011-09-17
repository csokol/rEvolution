package br.com.caelum.revolution.builds.nobuild;

import br.com.caelum.revolution.builds.Build;
import br.com.caelum.revolution.builds.SpecificBuildFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsBuild;

@IsBuild(name="No build", configs={})
public class NoBuildFactory implements SpecificBuildFactory {

	public Build build(Config config) {
		return new NoBuild();
	}
}
