package br.com.caelum.revolution.tools.cochanges;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

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
	public void shouldCountOneForOneCommitWithTwoFiles() throws ToolException {
		//Given
		Commit commit = new Commit();
		
		Artifact fileA = new Artifact("A", ArtifactKind.CODE);
		commit.addArtifact(fileA);
		
		Artifact fileB = new Artifact("B", ArtifactKind.CODE);
		commit.addArtifact(fileB);
		
		//When
		tool.calculate(commit, new BuildResult("anything"));
		
		//Then
		ArgumentCaptor<CoChangeCount> argument = ArgumentCaptor.forClass(CoChangeCount.class);
		verify(session).save(argument.capture());
		
		CoChangeCount coChangesCount = argument.getValue();
		
		assertEquals(Integer.valueOf(1),coChangesCount.getCount());
		assertEquals("A",coChangesCount.getOrigin());
		assertEquals("B",coChangesCount.getDestiny());
	}
	
	@Test
	public void shouldCountOneForEachFileAtTheCommit() throws ToolException {
		//Given
		Commit commit = new Commit();
		
		Artifact fileA = new Artifact("A", ArtifactKind.CODE);
		commit.addArtifact(fileA);
		
		Artifact fileB = new Artifact("B", ArtifactKind.CODE);
		commit.addArtifact(fileB);
		
		Artifact fileC = new Artifact("C", ArtifactKind.CODE);
		commit.addArtifact(fileC);
		
		//When
		tool.calculate(commit, new BuildResult("anything"));
		
		//Then
		ArgumentCaptor<CoChangeCount> argument = ArgumentCaptor.forClass(CoChangeCount.class);
		verify(session, times(3)).save(argument.capture());
		
		List<CoChangeCount> coChangesCounts = argument.getAllValues();
		
		assertEquals(3,coChangesCounts.size());
		
		assertEquals(Integer.valueOf(1),coChangesCounts.get(0).getCount());
		assertEquals("A",coChangesCounts.get(0).getOrigin());
		assertEquals("B",coChangesCounts.get(0).getDestiny());
		
		assertEquals(Integer.valueOf(1),coChangesCounts.get(1).getCount());
		assertEquals("A",coChangesCounts.get(1).getOrigin());
		assertEquals("C",coChangesCounts.get(1).getDestiny());
		
		assertEquals(Integer.valueOf(1),coChangesCounts.get(2).getCount());
		assertEquals("B",coChangesCounts.get(2).getOrigin());
		assertEquals("C",coChangesCounts.get(2).getDestiny());
	}
	
	
}
