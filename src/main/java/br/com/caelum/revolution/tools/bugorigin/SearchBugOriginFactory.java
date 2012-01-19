package br.com.caelum.revolution.tools.bugorigin;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

public class SearchBugOriginFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new SearchBugOriginTool(config.asString("keywords").split(";"));
	}

}
