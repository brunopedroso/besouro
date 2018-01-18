package besouro.persistence;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Date;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.EditAction;
import besouro.model.UnitTestCaseAction;

public class GitRecorderTest {
	
	private GitRecorder gitRec;
	private Git git;
	private AddCommand add;
	private CommitCommand commit;
	private File basedir;


	@Before
	public void setup() {
		basedir = new File("test/basedir");
		
		gitRec = new GitRecorder(basedir);
		
		git = mock(Git.class);
		add = mock(AddCommand.class);
		
		gitRec.setGit(git);
		
		when(git.add()).thenReturn(add);
		when(add.addFilepattern(anyString())).thenReturn(add);
		
		commit = mock(CommitCommand.class);
		when(git.commit()).thenReturn(commit);
		when(commit.setAll(true)).thenReturn(commit);
		when(commit.setCommitter(anyString(), anyString())).thenReturn(commit);
		when(commit.setMessage(anyString())).thenReturn(commit);
	}
	
	private static void deleteFileTree(File file) {
		if (file.isDirectory()) {
			for(File f: file.listFiles()) {
				deleteFileTree(f);
			}
		}
		file.delete();
	}

	@After
	public void teardown() {
		deleteFileTree(basedir);
	}
	
	
	@Test
	public void shouldAddAllAndCommitOnEachEditAction() throws Exception {

		EditAction action = new EditAction(new Date(), "anyFile");
		gitRec.addAction(action);
		
		verify(git).add();
		verify(add).addFilepattern(".");
		verify(add).call();
		
		verify(git).commit();
		verify(commit).setAll(true);
		verify(commit).setCommitter(anyString(), anyString());
		verify(commit).setMessage(anyString());
		verify(commit).call();
		
	}
	
	@Test
	public void shouldAddAllAndCommitOnClose() throws Exception {
		
		gitRec.close();
		
		verify(git).add();
		verify(add).addFilepattern(".");
		verify(add).call();
		
		verify(git).commit();
		verify(commit).setAll(true);
		verify(commit).setCommitter(anyString(), anyString());
		verify(commit).setMessage(anyString());
		verify(commit).call();
		
	}
	
	@Test
	public void shouldDoNothingForOtherActionTypes() throws Exception {

		UnitTestCaseAction action = new UnitTestCaseAction(new Date(), "anyFile");
		gitRec.addAction(action);
		
		verify(git,never()).add();
		verify(git, never()).commit();
		
	}
	
	@Test
	public void shouldCreateRepoIfItDoesntExist() throws Exception {
		
		Repository repo = mock(Repository.class);
		when(git.getRepository()).thenReturn(repo);
		
		gitRec.createRepoIfNeeded();
		
		verify(repo).create();
		
	}
	
	@Test
	public void shouldNotCreateRepoIfItExist() throws Exception {
		
		Repository repo = mock(Repository.class);
		when(git.getRepository()).thenReturn(repo);
		
		this.basedir.mkdir();
		new File(this.basedir, ".git").mkdir();
		
		gitRec.createRepoIfNeeded();
		
		verify(repo, never()).create();
		
	}
	
}
