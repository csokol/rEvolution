package br.com.caelum.revolution.tools.cc;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Artifact;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.executor.CommandExecutor;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.GoToChangeSet;
import br.com.caelum.revolution.scm.ToolThatUsesSCM;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

@GoToChangeSet
public class JavaNCSSTool implements Tool, ToolThatPersists, ToolThatUsesSCM {

	private Session session;
	private SCM scm;
	private final CommandExecutor exec;
	private final String javancssJarPath;
	private final String javancssLib;
	private final JavaNCSSOutputParse parser;

	public JavaNCSSTool(String javancssLib, String javancssJarPath,
			JavaNCSSOutputParse parser, CommandExecutor exec) {
		this.javancssLib = javancssLib;
		this.javancssJarPath = javancssJarPath;
		this.parser = parser;
		this.exec = exec;
	}

	public void calculate(Commit commit, BuildResult current)
			throws ToolException {

		exec.setEnvironmentVar("classpath", javancssLib);
		String result = exec.execute(
				"javancss -object -xml -recursive " + scm.getSourceCodePath(),
				javancssJarPath);

		ParsedJavaNCSS parsedResult = parser.parse(result);

		for (ParsedJavaNCSSObject object : parsedResult.getObjects()) {
			// TODO: does it work on windows?
			Artifact artifact = (Artifact) session
					.createCriteria(Artifact.class)
					.add(Restrictions.like("name",
							"%"+object.getName().replace(".", "/")+ "%")).uniqueResult();
			
			CyclomaticComplexity cc = new CyclomaticComplexity();
			cc.setArtifact(artifact);
			cc.setCc(object.getNcss());
			cc.setCommit(commit);
			
			session.save(cc);
		}

	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { CyclomaticComplexity.class };
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getName() {
		return "javancss";
	}

	public void setSCM(SCM scm) {
		this.scm = scm;
	}

}
