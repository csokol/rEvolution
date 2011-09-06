package br.com.caelum.revolution.tools.cochanges;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Artifact;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

public class CoChangesTool implements Tool, ToolThatPersists {

	private Session session;

	public void calculate(Commit commit, BuildResult current) throws ToolException {
		
		List<CoChangeCount> coChangesCount = new ArrayList<CoChangeCount>();

		List<Artifact> artifacts = commit.getArtifacts();
		
		generateCoChanges(artifacts.get(0), artifacts, coChangesCount,1);

		for (CoChangeCount coChangeCount : coChangesCount) {
			session.save(coChangeCount);
		}
	}

	private void generateCoChanges(Artifact origin,List<Artifact> destinyArtifats, List<CoChangeCount> coChangesCount, int begin) {
		
		for(int i = begin; i < destinyArtifats.size(); i++) {
		
			Artifact destinyArtifact = destinyArtifats.get(i); 
			CoChangeCount coChangeCount = new CoChangeCount();
			coChangeCount.setCount(1);
			coChangeCount.setOrigin(origin.getName());
			coChangeCount.setDestiny(destinyArtifact.getName());
			
			coChangesCount.add(coChangeCount);
		}
		
		if(destinyArtifats.size() == begin) {
			return;
		}
		
		generateCoChanges(destinyArtifats.get(begin), destinyArtifats, coChangesCount,++begin);
	}

	public String getName() {
		return "CoChangesTool";
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { CoChangeCount.class } ;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
