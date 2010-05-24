package edu.usp.ime.revolution.scm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import edu.usp.ime.revolution.scm.ChangeSet;
import edu.usp.ime.revolution.scm.ChangeSetCollection;

public class ChangeSetBuilder {
	public static ChangeSet aChangeSet(final String name) {
		return new ChangeSet() {
			public String getId() {
				return name;
			}

			public Calendar getTime() {
				return GregorianCalendar.getInstance();
			}
		};
	}
	

	public static ChangeSetCollection aCollectionWith(final ChangeSet changeSet) {
		return new ChangeSetCollection() {
			private boolean next = true;
			
			public Iterator<ChangeSet> iterator() {
				return this;
			}
			
			public void remove() {
				
			}
			
			public ChangeSet next() {
				return changeSet;
			}
			
			public boolean hasNext() {
				next = !next;
				return !next;
			}
		};
	}
}
