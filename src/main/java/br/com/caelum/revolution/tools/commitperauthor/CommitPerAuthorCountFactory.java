package br.com.caelum.revolution.tools.commitperauthor;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.tools.SpecificToolFactory;
import br.com.caelum.revolution.tools.Tool;

public class CommitPerAuthorCountFactory implements SpecificToolFactory {

	public Tool build(Config config) {
		return new CommitPerAuthorCountTool();
	}

}
