package edu.usp.ime.revolution.scm.changesets;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.usp.ime.revolution.config.Config;
import edu.usp.ime.revolution.scm.ChangeSetCollection;
import edu.usp.ime.revolution.scm.SCM;
import edu.usp.ime.revolution.scm.SpecificChangeSetFactory;

public class ChangeSetsInPeriodFactory implements SpecificChangeSetFactory {

	private final SimpleDateFormat sdf;

	public ChangeSetsInPeriodFactory() {
		sdf = new SimpleDateFormat("MM/dd/yyyy");
	}
	
	public ChangeSetCollection build(SCM scm, Config config) {
		try {
			Calendar startPeriod = Calendar.getInstance();
			startPeriod.setTime(sdf.parse(config.get("changesets.all.startPeriod")));
			Calendar endPeriod = Calendar.getInstance();
			endPeriod.setTime(sdf.parse(config.get("changesets.all.endPeriod")));
			
			return new ChangeSetsInPeriod(scm, startPeriod, endPeriod);
		}
		catch(Exception e) {
			throw new ChangeSetNotFoundException(e);
		}
	}

}
