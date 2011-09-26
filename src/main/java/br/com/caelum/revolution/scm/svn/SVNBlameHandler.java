package br.com.caelum.revolution.scm.svn;

import java.io.File;
import java.util.Date;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNAnnotateHandler;

public class SVNBlameHandler implements ISVNAnnotateHandler {

	private String authorName;
	private final int line;
	private String revision;
	
	public SVNBlameHandler(int line) {
		this.line = line;
	}

	public void handleEOF() {

	}

	public void handleLine(Date date, long revision, String author, String line)
			throws SVNException {
		System.out.println("bla");
		handleLine(date, revision, author, line, null, -1, null, null, 0);
	}

	public void handleLine(Date date, long revision, String author,
			String lineCode, Date mergedDate, long mergedRevision,
			String mergedAuthor, String mergedPath, int lineNumber)
			throws SVNException {
		if (lineNumber == line) {
			this.authorName = author;
			this.revision = String.valueOf(revision);
		}

	}

	public boolean handleRevision(Date date, long revision, String author,
			File contents) throws SVNException {
		return false;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getRevision() {
		return revision;
	}

	
	
}
