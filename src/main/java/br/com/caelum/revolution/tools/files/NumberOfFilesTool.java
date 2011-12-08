package br.com.caelum.revolution.tools.files;

import java.io.File;

import org.hibernate.Session;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.scm.GoToChangeSet;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.ToolThatUsesSCM;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

@GoToChangeSet
public class NumberOfFilesTool implements Tool, ToolThatPersists, ToolThatUsesSCM {

	private String extension;
	private Session session;
	private SCM scm;

	public NumberOfFilesTool(String extension) {
		this.extension = extension;
	}

	public void calculate(Commit commit, BuildResult current) throws ToolException {
		try {
			NumberOfFilesPerCommit result = new NumberOfFilesPerCommit();
			result.setCommit(commit);
			result.setQty(countFiles(scm.getSourceCodePath()));
			
			session.save(result);
		}
		catch(Exception e) {
			throw new ToolException(e);
		}
	}

	public String getName() {
		return "number-of-files";
	}
	
	private int countFiles(String directory) {
		int qty = 0;
		
		File[] files = new File(directory).listFiles();
		
		for(File file : files) {
			if(file.getName().endsWith(extension)) qty++;
			if(file.isDirectory()) qty += countFiles(file.getPath());
		}
		
		return qty;
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { NumberOfFilesPerCommit.class };
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setSCM(SCM scm) {
		this.scm = scm;
	}
}
