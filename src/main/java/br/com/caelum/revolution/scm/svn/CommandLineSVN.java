package br.com.caelum.revolution.scm.svn;

import java.util.List;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.executor.CommandExecutor;
import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.SCM;

public class CommandLineSVN implements SCM {

	private final CommandExecutor exec;
	private final String path;

	public CommandLineSVN(String path, CommandExecutor exec) {
		this.path = path;
		this.exec = exec;
	}
	
	public List<ChangeSet> getChangeSets() {
		String output = exec.execute("svn log --xml", getSourceCodePath());
		return null;
	}

	public String goTo(String id) {
		exec.execute("svn checkout -r " + id + " " + path, getSourceCodePath());
		return null;
	}

	public CommitData detail(String id) {
		// svn diff -r REV ANTERIOR:REV
		// svn log -r REV
		
		// TODO Auto-generated method stub
		return null;
	}

	public String sourceOf(String hash, String fileName) {
		exec.execute("svn checkout -r " + hash + " " + path + fileName, getSourceCodePath());
		return null;
	}

	public String getSourceCodePath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String blame(String commitId, String file, int line) {
		exec.execute("svn blame -r " + commitId + " " + path + file, getSourceCodePath());
		// TODO Auto-generated method stub
		return null;
	}

}
