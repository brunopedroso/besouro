package besouro.plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.listeners.BesouroListenerSet;
import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.FileOpenedAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.persistence.ActionFileStorage;
import besouro.persistence.EpisodeFileStorage;
import besouro.persistence.GitRecorder;
import besouro.plugin.ProgrammingSession;
import besouro.stream.EpisodeListener;

public class ProgrammingSessionTest {

	private File basedir;
	private ProgrammingSession session;

	private boolean notified;
	private BesouroListenerSet listeners;

	@Before
	public void setup() {
		basedir = new File("testDir");
		basedir.mkdir();
		
		listeners = mock(BesouroListenerSet.class);
		session = ProgrammingSession.newSession(basedir, listeners);
		
		GitRecorder git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		
	}
	
	@After
	public void teardown() {
		deleteFileTree(basedir);
	}

	private void deleteFileTree(File file) {
		if (file.isDirectory()) {
			for(File f: file.listFiles()) {
				deleteFileTree(f);
			}
		}
		file.delete();
	}
	
	@Test
	public void shouldStoreActions() {
		Assert.assertTrue("should create the file", session.getActionsFile().exists());

		session.addAction(new FileOpenedAction(new Date(), "afile"));
		Assert.assertEquals("should persist the action", 1, ActionFileStorage.loadFromFile(session.getActionsFile()).length);
	}

	
	@Test
	public void shouldStoreEpisodes() {
		Assert.assertTrue("should create the file", session.getEpisodesFile().exists());

		addRegressionActions();
		Assert.assertEquals("should persist the episode", 1, EpisodeFileStorage.loadEpisodes(session.getEpisodesFile()).length);
	}

	private void addRegressionActions() {
		session.addAction(new FileOpenedAction(new Date(), "afile"));
		session.addAction(new UnitTestCaseAction(new Date(), "afile", true));
		session.addAction(new UnitTestSessionAction(new Date(), "afile", true));
	}
	
	@Test
	public void shouldNotifyEpisodeListeners() {
		notified = false;
		EpisodeListener listener = new EpisodeListener() {
			public void episodeRecognized(Episode e) {
				notified = true;				
			}
		};
		session.addEpisodeListeners(listener);
		addRegressionActions();
		Assert.assertTrue("should call the listener", notified);
	}
		
	@Test
	public void shouldRegisterItselfInTheListenerSet() {
		verify(listeners).setOutputStream(session);
	}
	
	@Test
	public void shouldRegisterEclipseListeners() {
		session.start();
		verify(listeners).registerListenersInEclipse();
	}
	
	@Test
	public void shouldUnregisterListenersFromEclipse() {
		session.close();
		verify(listeners).unregisterListenersInEclipse();
	}
	
	@Test
	public void shouldUnregisterListenersFromEclipseOnNewSession() {
		
		// another session has already been created in setup...
		session.start();
		
		session = ProgrammingSession.newSession(basedir, listeners);
		session.start();
		verify(listeners, times(1)).unregisterListenersInEclipse();
		verify(listeners, times(2)).registerListenersInEclipse();
	}

	@Test
	public void shouldStartANewDirforEachSession() {
		Assert.assertEquals("should have created one dir", 1, basedir.list().length);
		Assert.assertTrue("should be a dir", basedir.listFiles()[0].isDirectory());
		Assert.assertEquals("should have created the files inside dir", 2, basedir.listFiles()[0].listFiles().length);
		
		session = ProgrammingSession.newSession(basedir, listeners);
		Assert.assertEquals("should have created another dir", 2, basedir.list().length);
		Assert.assertTrue("should be a dir", basedir.listFiles()[0].isDirectory());
		Assert.assertTrue("should be a dir", basedir.listFiles()[1].isDirectory());
		Assert.assertEquals("should have created the files inside dir", 2, basedir.listFiles()[0].listFiles().length);
		Assert.assertEquals("should have created the files inside dir", 2, basedir.listFiles()[1].listFiles().length);
		
	}
	
	@Test
	public void shouldAddActionsToGitRecorderStream() {
		GitRecorder git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		
		Action action = new EditAction(new Date(), "afile");
		session.addAction(action);
		
		verify(git).addAction(action);
	}
	
	@Test
	public void shouldCreateNewGitRepoIfItDoesnotExist() {
		GitRecorder git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		session.start();
		verify(git).createRepoIfNeeded();
	}
	
}
