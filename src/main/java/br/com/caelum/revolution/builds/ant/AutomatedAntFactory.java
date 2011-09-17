package br.com.caelum.revolution.builds.ant;

import br.com.caelum.revolution.builds.Build;
import br.com.caelum.revolution.builds.SpecificBuildFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsBuild;

@IsBuild(name="Ant with no build file", configs={"ant.srcDir", "ant.destDir", "ant.classpath"})
public class AutomatedAntFactory implements SpecificBuildFactory{

	public Build build(Config config) {
		return new AutomatedAnt(config.asString("ant.srcDir"), config.asString("ant.destDir"), config.asString("ant.classpath").split(";"));
	}

}
