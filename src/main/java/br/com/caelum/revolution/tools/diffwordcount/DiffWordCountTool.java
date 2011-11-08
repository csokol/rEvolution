package br.com.caelum.revolution.tools.diffwordcount;

import org.hibernate.Session;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.domain.Modification;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

public class DiffWordCountTool implements Tool, ToolThatPersists {

	private Session session;
	private final String[] extensions;
	private final String[] patterns;
	private final String name;

	public DiffWordCountTool(String[] extensions, String[] patterns, String name) {
		this.extensions = extensions;
		this.patterns = patterns;
		this.name = name;
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { DiffWordCount.class };
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void calculate(Commit commit, BuildResult current)
			throws ToolException {

		for (Modification modification : commit.getModifications()) {

			if (matches(modification)) {
				DiffWordCount qty = new DiffWordCount();
				for (String line : modification.getDiff().replace("\r", "")
						.split("\n")) {
					if (line.startsWith("+") && containsAnyPatternIn(line)) {
						qty.setAdded(qty.getAdded() + 1);
					}
					if (line.startsWith("-") && containsAnyPatternIn(line)) {
						qty.setRemoved(qty.getRemoved() + 1);
					}
				}

				qty.setCommit(commit);
				qty.setArtifact(modification.getArtifact());
				qty.setName(name);
				session.save(qty);
			}
		}
	}

	private boolean containsAnyPatternIn(String line) {
		for (String pattern : patterns) {
			if(line.contains(pattern)) return true;
		}
		return false;
	}

	private boolean matches(Modification modification) {
		for (String extension : extensions) {
			if (modification.getArtifact().getName().endsWith(extension))
				return true;
		}
		return false;
	}

	public String getName() {
		return "diff-word-count " + patterns;
	}

}
