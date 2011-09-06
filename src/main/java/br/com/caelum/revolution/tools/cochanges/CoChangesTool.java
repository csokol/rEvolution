package br.com.caelum.revolution.tools.cochanges;

import org.hibernate.Session;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

public class CoChangesTool implements Tool, ToolThatPersists {

	private Session session;

	public void calculate(Commit commit, BuildResult current) throws ToolException {
		CoChangesCount coChangesCount = new CoChangesCount();
		
		coChangesCount.setOrigin(commit.getArtifacts().get(0).getName());
		coChangesCount.setDestiny(commit.getArtifacts().get(1).getName());
		coChangesCount.setCount(1);
		
		session.save(coChangesCount);
	}

	public String getName() {
		return "CoChangesTool";
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { CoChangesCount.class } ;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
