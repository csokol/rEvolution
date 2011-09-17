package br.com.caelum.revolution.tools.files;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="Number of Files", configs={"extension"})
public class NumberOfFilesFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new NumberOfFilesTool(config.asString("extension"));
	}

}
