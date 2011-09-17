package br.com.caelum.revolution.changesets;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsChangeSet;
import br.com.caelum.revolution.scm.SCM;

@IsChangeSet(name="All Changesets", configs={})
public class AllChangeSetsFactory implements SpecificChangeSetFactory {

	public ChangeSetCollection build(SCM scm, Config config) {
		return new AllChangeSets(scm);
	}

}
