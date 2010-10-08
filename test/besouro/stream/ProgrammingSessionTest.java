package besouro.stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Episode;
import besouro.model.FileOpenedAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.plugin.EpisodeListener;
import besouro.plugin.ListenersSet;

public class ProgrammingSessionTest {

	private File basedir;
	private File actionsFile;
	private File episodesFile;
	private ProgrammingSession session;

	private boolean notified;
	private ListenersSet listeners;

	@Before
	public void setup() {
		basedir = new File("testDir");
		basedir.mkdir();
		
		actionsFile = new File(basedir, "actions.txt");
		actionsFile.delete();
		Assert.assertFalse("actionsFile should not exist upfront", actionsFile.exists());
		
		episodesFile = new File(basedir, "episodes.txt");
		episodesFile.delete();
		Assert.assertFalse("episodeFile should not exist upfront", episodesFile.exists());

		listeners = mock(ListenersSet.class);
	}
	
	@After
	public void teardown() {
		actionsFile.delete();
		episodesFile.delete();
		basedir.delete();
	}
	
	@Test
	public void shouldStoreActions() {
		session = ProgrammingSession.newSession(basedir, listeners);
		Assert.assertTrue("should create the file", actionsFile.exists());

		session.addAction(new FileOpenedAction(new Date(), "afile"));
		Assert.assertEquals("should persist the action", 1, FileStorageActionStream.loadFromFile(actionsFile).length);
	}

	
	@Test
	public void shouldStoreEpisodes() {
		session = ProgrammingSession.newSession(basedir, listeners);
		Assert.assertTrue("should create the file", episodesFile.exists());

		addRegressionActions();
		Assert.assertEquals("should persist the episode", 1, EpisodeFileStorage.loadEpisodes(episodesFile).length);
	}

	private void addRegressionActions() {
		session.addAction(new FileOpenedAction(new Date(), "afile"));
		session.addAction(new UnitTestCaseAction(new Date(), "afile", true));
		session.addAction(new UnitTestSessionAction(new Date(), "afile", true));
	}
	
	@Test
	public void shouldNotifyEpisodeListeners() {
		session = ProgrammingSession.newSession(basedir, listeners);

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
	public void shouldRegisterEclipseListeners() {
		session = ProgrammingSession.newSession(basedir, listeners);
		verify(listeners).setOutputStream(session);
	}
	
	@Test
	public void shouldRegisterItselfInTheListenerSet() {
		session = ProgrammingSession.newSession(basedir, listeners);
		verify(listeners).registerListenersInEclipse();
	}
	
	@Test
	public void shouldUnregisterListenersFromEclipse() {
		session = ProgrammingSession.newSession(basedir, listeners);
		session.close();
		verify(listeners).unregisterListenersInEclipse();
	}
	
	@Test
	public void shouldUnregisterListenersFromEclipseOnNewSession() {
		session = ProgrammingSession.newSession(basedir, listeners);
		session = ProgrammingSession.newSession(basedir, listeners);
		verify(listeners, times(1)).unregisterListenersInEclipse();
		verify(listeners, times(2)).registerListenersInEclipse();
		
	}
	
}
