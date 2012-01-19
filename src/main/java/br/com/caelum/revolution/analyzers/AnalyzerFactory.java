package br.com.caelum.revolution.analyzers;

import java.util.List;

import br.com.caelum.revolution.builds.Build;
import br.com.caelum.revolution.builds.BuildFactory;
import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.changesets.ChangeSetCollectionFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.SCMFactory;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolsFactory;


public class AnalyzerFactory {

	public AnalyzerRunner basedOn(Config config) {
		SCM scm = new SCMFactory().basedOn(config);
		Build build = new BuildFactory().basedOn(config);
		List<Tool> tools = new ToolsFactory().basedOn(config);
		ChangeSetCollection collection = new ChangeSetCollectionFactory(scm).basedOn(config);
		
		Analyzer analyzer = new DefaultAnalyzer(scm, build, tools, new PersistedCommitConverter(), new HibernatePersistence(config));
		
		return new DefaultAnalyzerRunner(analyzer, collection);
	}

}
