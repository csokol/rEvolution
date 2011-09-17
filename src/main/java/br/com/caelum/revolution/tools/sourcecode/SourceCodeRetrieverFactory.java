package br.com.caelum.revolution.tools.sourcecode;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="Source Code Retriever", configs={"extensions"})
public class SourceCodeRetrieverFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new SourceCodeRetrieverTool(config.asString("extensions").split(";"));
	}

}
