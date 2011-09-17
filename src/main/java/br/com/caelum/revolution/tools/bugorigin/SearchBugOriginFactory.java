package br.com.caelum.revolution.tools.bugorigin;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="Track Bug Origin", configs={"keywords"})
public class SearchBugOriginFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new SearchBugOriginTool(config.asString("keywords").split(";"));
	}

}
