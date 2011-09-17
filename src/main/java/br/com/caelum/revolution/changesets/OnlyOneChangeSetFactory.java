package br.com.caelum.revolution.changesets;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsChangeSet;
import br.com.caelum.revolution.scm.SCM;

@IsChangeSet(name="Only One Changeset", configs={"changesets.one"})
public class OnlyOneChangeSetFactory implements SpecificChangeSetFactory{

	public ChangeSetCollection build(SCM scm, Config config) {
		return new OnlyOneChangeSet(config.asString("changesets.one"));
	}
}
