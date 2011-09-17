package br.com.caelum.revolution.tools.diffwordcount;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="Word Count", configs={"patterns", "extensions", "name"})
public class DiffWordCountFactory implements SpecificToolFactory{

	public Tool build(Config config) {
		return new DiffWordCountTool(extensions(config), patterns(config), config.asString("name"));
	}

	private String[] patterns(Config config) {
		return config.asString("patterns").split(";");
	}
	
	private String[] extensions(Config config) {
		return config.asString("extensions").split(";");
	}

}
