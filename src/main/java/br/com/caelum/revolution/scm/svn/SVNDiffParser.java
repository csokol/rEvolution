package br.com.caelum.revolution.scm.svn;


public class SVNDiffParser {

	public String parse(String diff) {
		return removeFirst4LinesFrom(diff);
	}

	private String removeFirst4LinesFrom(String diff) {
		String[] lines = diff.replace("\r", "").split("\n");
		
		StringBuilder finalDiff = new StringBuilder();
		for(int i = 4; i < lines.length; i++){
			finalDiff.append(lines[i]);
			finalDiff.append("\n");
		}
		
		return finalDiff.toString();
	}

}
