package br.com.caelum.revolution.tools.lineschanged;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

public class NumberOfLinesChangedFactory implements SpecificToolFactory{

	public Tool build(Config config) {
		return new NumberOfLinesChangedTool();
	}

}
