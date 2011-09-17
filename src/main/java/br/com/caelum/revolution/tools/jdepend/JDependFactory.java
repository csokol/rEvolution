package br.com.caelum.revolution.tools.jdepend;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.executor.SimpleCommandExecutor;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="JDepend", configs={"jDependPath"})
public class JDependFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new JDependTool(
				new SimpleCommandExecutor(),  
				new DefaultJDependXMLInterpreter(),
				config.asString("jDependPath"));
	}

}
