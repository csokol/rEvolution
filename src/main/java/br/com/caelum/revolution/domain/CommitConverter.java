package br.com.caelum.revolution.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.DiffData;

public class CommitConverter {

	public Commit toDomain(CommitData data, Session session) throws ParseException {
		Commit commit = new Commit(data.getCommitId(), data.getAuthor(),
				data.getEmail(), convertDate(data), data.getMessage(), data.getDiff());
		session.save(commit);
		
		for (DiffData diff : data.getDiffs()) {
			Artifact artifact = searchForPreviouslySaved(diff.getName(), session);

			if (itDoesNotExist(artifact)) {
				artifact = new Artifact(diff.getName(), diff.getArtifactKind());
				session.save(artifact);
			}
			
			Modification modification = new Modification(diff.getDiff(), commit, artifact, diff.getModificationKind());
			artifact.addModification(modification);
			commit.addModification(modification);
			session.save(modification);
		}

		return commit;
	}

	private boolean itDoesNotExist(Artifact artifact) {
		return artifact == null;
	}

	private Artifact searchForPreviouslySaved(String name, Session session) {
		Artifact artifact = (Artifact) session
				.createCriteria(Artifact.class)
				.add(Restrictions.eq("name", name))
				.uniqueResult();
		return artifact;
	}

	private Calendar convertDate(CommitData data) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(data
				.getDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
}
