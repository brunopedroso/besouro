package besouro.plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
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
import besouro.stream.EpisodeListener;

public class ProgrammingSessionTest {

	private File basedir;
	private ProgrammingSession session;

	private boolean notified;
	private BesouroListenerSet listeners;
	private GitRecorder git;

	@Before
	public void setup() {
		basedir = new File("testDir");
		basedir.mkdir();
		
		listeners = mock(BesouroListenerSet.class);
		
		session = ProgrammingSession.newSession(basedir, listeners);
		
		git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		
	}
	
	@After
	public void teardown() {
		deleteFileTree(basedir);
		session.close();
	}

	private void deleteFileTree(File file) {
		if (file.isDirectory()) {
			for(File f: file.listFiles()) {
				deleteFileTree(f);
			}
		}
		file.delete();
	}
	
	private void addRegressionActions(long timestamp) {
		session.addAction(new FileOpenedAction(new Date(timestamp), "afile"));
		session.addAction(new UnitTestCaseAction(new Date(timestamp), "afile", true));
		session.addAction(new UnitTestSessionAction(new Date(timestamp), "afile", true));
	}
	
	@Test
	public void shouldStoreActions() {
		Assert.assertTrue("should create the file", session.getActionsFile().exists());

		session.addAction(new FileOpenedAction(new Date(), "afile"));
		Assert.assertEquals("should persist the action", 1, ActionFileStorage.loadFromFile(session.getActionsFile()).length);
	}

	
	@Test
	public void shouldStoreZorroEpisodes() {
		Assert.assertTrue("should create the file", session.getZorroEpisodesFile().exists());

		for (int i = 0; i < 10; i++) {
			addRegressionActions(i*1000);
		}
		
		session.close();
		
		Assert.assertEquals("should persist the episode", 10, EpisodeFileStorage.loadEpisodes(session.getZorroEpisodesFile()).length);
	}
	
	@Test
	public void shouldStoreRandomHeuristicEpisodes() {
		Assert.assertTrue("should create the file", session.getRandomheuristicEpisodesFile().exists());
		
		for (int i = 0; i < 10; i++) {
			addRegressionActions(i*1000);
		}
		
		Episode[] episodes = EpisodeFileStorage.loadEpisodes(session.getRandomheuristicEpisodesFile());
		Assert.assertEquals("should persist the episode", 10, episodes.length);
		
		int conf = 0;
		int nonconf = 0;
		
		for (int i = 0; i < 10; i++) {
			if (episodes[i].isTDD())
				conf++;
			else
				nonconf++;
		}
		
		Assert.assertTrue(conf > 0);
		Assert.assertTrue(nonconf > 0);
	}

	
	@Test
	public void shouldStoreBesouroClassificationEpisodes() {
		
		Assert.assertTrue("should create the file", session.getBesouroEpisodesFile().exists());

		for (int i = 0; i < 10; i++) {
			addRegressionActions(i*1000);
		}
		
		session.close();
		
		Assert.assertEquals("should persist the episode", 10, EpisodeFileStorage.loadEpisodes(session.getBesouroEpisodesFile()).length);
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
		addRegressionActions(System.currentTimeMillis());
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
		session.setGitRecorder(git);
		
		session.start();
		verify(listeners, times(1)).unregisterListenersInEclipse();
		verify(listeners, times(2)).registerListenersInEclipse();
	}

	@Test
	public void shouldStartANewDirforEachSession() {
		File besouroDir = new File(basedir, ".besouro");
		Assert.assertEquals("should have created one dir", 1, besouroDir.list().length);
		Assert.assertTrue("should be a dir", besouroDir.listFiles()[0].isDirectory());
		Assert.assertEquals("should have created the files inside dir", 6, besouroDir.listFiles()[0].listFiles().length);
		
		session = ProgrammingSession.newSession(basedir, listeners);
		session.setGitRecorder(git);
		
		Assert.assertEquals("should have created another dir", 2, besouroDir.list().length);
		Assert.assertTrue("should be a dir", besouroDir.listFiles()[0].isDirectory());
		Assert.assertTrue("should be a dir", besouroDir.listFiles()[1].isDirectory());
		Assert.assertEquals("should have created the files inside dir", 6, besouroDir.listFiles()[0].listFiles().length);
		Assert.assertEquals("should have created the files inside dir", 6, besouroDir.listFiles()[1].listFiles().length);
		
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
	public void shouldCloseGitRecorderOnSessionCloseToo() {
		GitRecorder git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		session.close();
		verify(git).close();
	}

	
	@Test
	public void shouldCreateNewGitRepoIfItDoesnotExist() {
		GitRecorder git = mock(GitRecorder.class);
		session.setGitRecorder(git);
		session.start();
		verify(git).createRepoIfNeeded();
	}
	
	@Test
	public void shouldCreateDisagreementsFile() {
		Assert.assertTrue("should create the file", session.getDisagreementsFile().exists());
	}
	
	@Test
	public void shouldNotRegisterDisagreementAlone() {
		addRegressionActions(System.currentTimeMillis());
		Assert.assertEquals("should have recognized the episode", 1, session.getEpisodes().length);
		Assert.assertEquals("should not have persisted the episode yet", 0, EpisodeFileStorage.loadEpisodes(session.getDisagreementsFile()).length);
	}
	
	@Test
	public void shouldRegisterDisagreementWhenWeCall() {
		addRegressionActions(System.currentTimeMillis());
		session.disagreeFromEpisode(session.getEpisodes()[0]);
		Assert.assertEquals("should persist the episode", 1, EpisodeFileStorage.loadEpisodes(session.getDisagreementsFile()).length);
	}
	
	@Test
	public void shouldRegisterDisagreementOnlyOnce() {
		
		addRegressionActions(System.currentTimeMillis());
		
		session.disagreeFromEpisode(session.getEpisodes()[0]);
		session.disagreeFromEpisode(session.getEpisodes()[0]);
		
		Assert.assertEquals("should persist the episode", 1, EpisodeFileStorage.loadEpisodes(session.getDisagreementsFile()).length);
		
	}
	
	
}
