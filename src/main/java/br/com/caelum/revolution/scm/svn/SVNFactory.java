package br.com.caelum.revolution.scm.svn;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsSCM;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.SpecificSCMFactory;

@IsSCM(name="SVN", configs={"path", "tempPath"})
public class SVNFactory implements SpecificSCMFactory {

	public SCM build(Config config) {
		return new SVN(config.asString("path"), config.asString("tempPath"), new SVNDiffParser());
	}

}
