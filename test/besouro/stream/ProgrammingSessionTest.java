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

import besouro.listeners.ListenersSet;
import besouro.model.Episode;
import besouro.model.FileOpenedAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.plugin.EpisodeListener;

public class ProgrammingSessionTest {

	private File basedir;
	private ProgrammingSession session;

	private boolean notified;
	private ListenersSet listeners;

	@Before
	public void setup() {
		basedir = new File("testDir");
		basedir.mkdir();
		
		listeners = mock(ListenersSet.class);
		session = ProgrammingSession.newSession(basedir, listeners);
	}
	
	@After
	public void teardown() {
		for(File f: basedir.listFiles()) {
			f.delete();
		}
		basedir.delete();
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
	public void shouldRegisterEclipseListeners() {
		verify(listeners).setOutputStream(session);
	}
	
	@Test
	public void shouldRegisterItselfInTheListenerSet() {
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
		session = ProgrammingSession.newSession(basedir, listeners);
		verify(listeners, times(1)).unregisterListenersInEclipse();
		verify(listeners, times(2)).registerListenersInEclipse();
	}

	@Test
	public void shouldStartAFileforEachSession() {
		Assert.assertEquals("should have created files", 2, basedir.list().length);
		session = ProgrammingSession.newSession(basedir, listeners);
		Assert.assertEquals("should create other files", 4, basedir.list().length);
		//its supposed that it will write to the newly created file... but its not being tested...
	}
	
}
