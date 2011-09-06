package br.com.caelum.revolution.tools.cochanges;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Artifact;
import br.com.caelum.revolution.domain.ArtifactKind;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.tools.ToolException;

public class CoChangesToolTest {

	private CoChangesTool tool;
	private Session session;

	@Before
	public void setUp() {
		tool = new CoChangesTool();
		session = mock(Session.class);
		tool.setSession(session);
	}
	
	@Test
	public void shouldCountOneForTwoFilesCommitted() throws ToolException {
		//Given
		Commit commit = new Commit();
		
		Artifact fileA = new Artifact("fileA.java", ArtifactKind.CODE);
		commit.addArtifact(fileA);
		
		Artifact fileB = new Artifact("fileB.java", ArtifactKind.CODE);
		commit.addArtifact(fileB);
		
		//When
		tool.calculate(commit, new BuildResult("anything"));
		
		//Then
		ArgumentCaptor<CoChangesCount> argument = ArgumentCaptor.forClass(CoChangesCount.class);
		verify(session).save(argument.capture());
		
		CoChangesCount coChangesCount = argument.getValue();
		
		assertEquals(Integer.valueOf(1),coChangesCount.getCount());
		assertEquals("fileA.java",coChangesCount.getOrigin());
		assertEquals("fileB.java",coChangesCount.getDestiny());
		
	}
}
