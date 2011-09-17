package br.com.caelum.revolution.tools.lineschanged;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

@IsTool(name="Lines Changed", configs={})
public class NumberOfLinesChangedFactory implements SpecificToolFactory{

	public Tool build(Config config) {
		return new NumberOfLinesChangedTool();
	}

}
