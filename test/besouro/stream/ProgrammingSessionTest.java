package besouro.stream;

import java.io.File;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.FileOpenedAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;

import junit.framework.Assert;

public class ProgrammingSessionTest {

	
	//TODO  should store episodes
	// 			should register EpisodeStorage in EpisodeClassifier
	
	//TODO  should allow EpisodeListeners and notify them
	//			view should listen
	
	//TODO  should register listener set
	//TODO  should register itself as actionListener of listener set
	//TODO  should unregister itself as actionListener in listenerSet on close
	//TODO  should close previous session on newSession

	private File basedir;
	private File actionsFile;
	private File episodesFile;
	private ProgrammingSession session;

	@Before
	public void setup() {
		basedir = new File("testDir");
		basedir.mkdir();
		
		actionsFile = new File(basedir, "actions.txt");
		actionsFile.delete();
		Assert.assertFalse("actionsFile should not exist upfront", actionsFile.exists());
		
		session = ProgrammingSession.newSession(basedir);
		Assert.assertTrue("should create the file", actionsFile.exists());

		episodesFile = new File(basedir, "episodes.txt");
		episodesFile.delete();
		Assert.assertFalse("episodeFile should not exist upfront", episodesFile.exists());
		
		session = ProgrammingSession.newSession(basedir);
		Assert.assertTrue("should create the file", episodesFile.exists());

	}
	
	@After
	public void teardown() {
		actionsFile.delete();
		episodesFile.delete();
		basedir.delete();
	}
	
	@Test
	public void shouldStoreActions() {
		session.addAction(new FileOpenedAction(new Date(), "afile"));
		Assert.assertEquals("should persist the action", 1, FileStorageActionStream.loadFromFile(actionsFile).length);
	}

	
	@Test
	public void shouldStoreEpisodes() {
		
		session.addAction(new FileOpenedAction(new Date(), "afile"));
		session.addAction(new EditAction(new Date(), "afile"));
		session.addAction(new UnitTestCaseAction(new Date(), "afile", true));
		session.addAction(new UnitTestSessionAction(new Date(), "afile", true));
		
		Assert.assertEquals("should persist the episode", 1, EpisodeFileStorage.loadEpisodes(episodesFile).length);
		
	}
	
}
