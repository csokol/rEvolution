package br.com.caelum.revolution.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;


@Entity
public class Commit {
	@Id @GeneratedValue
	private int id;
	
	private String commitId;
	@ManyToOne
	private Author author;
	private Calendar date;
	private String message;
	@Type(type="text")
	private String diff;
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="commits")
	private List<Artifact> artifacts;
	@OneToMany(mappedBy="commit", cascade=CascadeType.ALL)
	private List<Modification> modifications;
	@OneToOne
	private Commit priorCommit;
	
	public Commit(String commitId, Author author, Calendar date,
			String message, String diff, Commit priorCommit) {
		this();
		this.commitId = commitId;
		this.author = author;
		this.date = date;
		this.message = message;
		this.diff = diff;
		this.priorCommit = priorCommit;
	}

	public Commit() {
		this.artifacts = new ArrayList<Artifact>();
		this.modifications = new ArrayList<Modification>();
	}
	
	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public int getId() {
		return id;
	}

	public void addArtifact(Artifact artifact) {
		if(artifacts == null) {
			artifacts = new ArrayList<Artifact>();
		}
		artifacts.add(artifact);
	}

	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public List<Modification> getModifications() {
		return modifications;
	}

	public void addModification(Modification modification) {
		modification.setCommit(this);
		modifications.add(modification);
	}

	public Commit getPriorCommit() {
		return priorCommit;
	}

	public void setPriorCommit(Commit priorCommit) {
		this.priorCommit = priorCommit;
	}


	
	
}
