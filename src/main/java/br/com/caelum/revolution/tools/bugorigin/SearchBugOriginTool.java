package br.com.caelum.revolution.tools.bugorigin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.domain.Modification;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.ToolThatUsesSCM;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

public class SearchBugOriginTool implements Tool, ToolThatPersists,
		ToolThatUsesSCM {

	private static Logger log = LoggerFactory
			.getLogger(SearchBugOriginTool.class);
	private SCM scm;
	private Session session;
	private Set<String> commitsAlreadyAdded;
	private final String[] keywords;

	public SearchBugOriginTool(String[] keywords) {
		this.keywords = keywords;
	}

	public void setSCM(SCM scm) {
		this.scm = scm;
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { BugOrigin.class };
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void calculate(Commit commit, BuildResult current)
			throws ToolException {
		commitsAlreadyAdded = new HashSet<String>();

		if (!noKeywordsIn(commit))
			return;

		for (Modification modification : commit.getModifications()) {
			List<Integer> lines = findLinesToBeBlamedIn(modification);

			for (Integer buggedLine : lines) {
				
				String hash = scm.blame(modification.getCommit().getPriorCommitId(), modification.getArtifact().getName(), buggedLine);
				log.info("Bugged hash for line " + buggedLine + ": (" + hash + ")");

				if (!commitsAlreadyAdded.contains(hash)) {

					Commit buggedCommit = (Commit) session
							.createCriteria(Commit.class)
							.add(Restrictions.eq("commitId", hash))
							.uniqueResult();

					if(buggedCommit == null) {
						log.error("Commit " + hash + " mentioned, but not existant! WTF!?");
					}
					else {
						BugOrigin origin = new BugOrigin(buggedCommit, modification);
						session.save(origin);
					}

					commitsAlreadyAdded.add(hash);
				}
			}
		}
	}

	private List<Integer> findLinesToBeBlamedIn(Modification modification) {
		String[] lines = modification.getDiff().replace("\r", "").split("\n");
		List<Integer> linesToBeBlamed = new ArrayList<Integer>();

		int currentLine = 0;
		for (int i = 0; i < lines.length; i++) {
			currentLine++;
			
			if (itRepresentsALineNumber(lines[i])) {
				currentLine = getLineNumberItGitDiffNotation(lines[i]);
			}
			if (itRepresentsCodeThatWasRemoved(lines[i]) && isNotEmpty(lines[i])) {
				linesToBeBlamed.add(currentLine);
			}
			if (itRepresentsCodeThatWasAdded(lines[i])) {
				currentLine--;
			}
		}

		return linesToBeBlamed;
	}

	private boolean isNotEmpty(String line) {
		return line.substring(1, line.length()).replace("\t", "").trim().length() > 0;
	}

	private int getLineNumberItGitDiffNotation(String line) {
		return Integer.parseInt(line.substring(4, line.indexOf(","))) - 1;
	}

	private boolean itRepresentsCodeThatWasAdded(String line) {
		return line.startsWith("+");
	}

	private boolean itRepresentsALineNumber(String line) {
		return line.startsWith("@@");
	}

	private boolean noKeywordsIn(Commit commit) {
		for (String keyword : keywords) {
			if (commit.getMessage().contains(keyword)) {
				log.info("Found a commit that fixed a bug because of the word '" + keyword + "'");
				return true;
			}
		}
		return false;
	}

	private boolean itRepresentsCodeThatWasRemoved(String line) {
		return line.startsWith("-");
	}

	public String getName() {
		return "search-bug-origin";
	}

}
