package besouro.persistence;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.UnitTestCaseAction;
import besouro.persistence.EpisodeFileStorage;

public class EpisodeFileStorageTest {

	private File file;
	private EpisodeFileStorage storage;

	@Before
	public void setup() {
		file = new File("test/testEpisodes.txt");
		Assert.assertFalse(file.exists());
	}

	@After
	public void tearDown() {
		file.delete();
	}
	
	@Test
	public void shouldCreateANewFile() {
		storage = new EpisodeFileStorage(file);
		Assert.assertTrue("should have created the file", file.exists());
	}
	
	@Test
	public void shouldStoreOneEpisode() {
		storage = new EpisodeFileStorage(file);
		
		Episode e = new Episode();
		e.setClassification("test-first", "1");
		e.setDuration(11);
		e.setIsTDD(true);
		
		storage.episodeRecognized(e);
		
		Episode e1 = EpisodeFileStorage.loadEpisodes(file)[0];
		
		Assert.assertEquals("should persist category", e.getCategory(), e1.getCategory());
		Assert.assertEquals("should persist subtype", e.getSubtype(), e1.getSubtype());
		Assert.assertEquals("should persist duration", e.getDuration(), e1.getDuration());
		Assert.assertEquals("should persist test result", e.isTDD(), e1.isTDD());
	}
	
	@Test
	public void shouldStoreThreeEpisodes() {
		storage = new EpisodeFileStorage(file);
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		e1.setDuration(11);
		e1.setIsTDD(true);
		storage.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1A");
		e2.setDuration(22);
		e2.setIsTDD(true);
		storage.episodeRecognized(e2);
		
		Episode e3 = new Episode();
		e3.setClassification("production", "2");
		e3.setDuration(33);
		e3.setIsTDD(false);
		storage.episodeRecognized(e3);
		
		Episode[] es = EpisodeFileStorage.loadEpisodes(file);
		
		Assert.assertEquals("should persist category", e1.getCategory(), es[0].getCategory());
		Assert.assertEquals("should persist subtype", e1.getSubtype(), es[0].getSubtype());
		Assert.assertEquals("should persist duration", e1.getDuration(), es[0].getDuration());
		Assert.assertEquals("should persist test result", e1.isTDD(), es[0].isTDD());
		
		Assert.assertEquals("should persist category", e2.getCategory(), es[1].getCategory());
		Assert.assertEquals("should persist subtype", e2.getSubtype(), es[1].getSubtype());
		Assert.assertEquals("should persist duration", e2.getDuration(), es[1].getDuration());
		Assert.assertEquals("should persist test result", e2.isTDD(), es[1].isTDD());
		
		Assert.assertEquals("should persist category", e3.getCategory(), es[2].getCategory());
		Assert.assertEquals("should persist subtype", e3.getSubtype(), es[2].getSubtype());
		Assert.assertEquals("should persist duration", e3.getDuration(), es[2].getDuration());
		Assert.assertEquals("should persist test result", e3.isTDD(), es[2].isTDD());
		
		
	}
	
	/**
	 * once actions and episodes will be in separate files, timestamps will be used to link them.
	 */
	@Test
	public void shouldStoreTheTimestampOfTheLastAction() {
		
		storage = new EpisodeFileStorage(file);
		
		EditAction firstAction = new EditAction(new Date(), "afile");
		UnitTestCaseAction lastAction = new UnitTestCaseAction(new Date(), "afile");
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(firstAction);
		actions.add(lastAction);
		
		Episode e = new Episode();
		e.addActions(actions);
		
		storage.episodeRecognized(e);
		
		Episode e1 = EpisodeFileStorage.loadEpisodes(file)[0];
		
		Assert.assertEquals("should persist timestamp", lastAction.getClock().getTime(), e1.getTimestamp());
	}
	
	
	@Test
	public void shouldStoreOldModifiedEpisodeAgain() {
		storage = new EpisodeFileStorage(file);
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		e1.setDuration(11);
		e1.setIsTDD(true);
		storage.episodeRecognized(e1);
		
		Episode[] es = EpisodeFileStorage.loadEpisodes(file);
		
		Assert.assertEquals("should persist category", e1.getCategory(), es[0].getCategory());
		Assert.assertEquals("should persist subtype", e1.getSubtype(), es[0].getSubtype());
		Assert.assertEquals("should persist duration", e1.getDuration(), es[0].getDuration());
		Assert.assertEquals("should persist test result", e1.isTDD(), es[0].isTDD());
		
		
		// Now we change the old episode
		e1.setClassification("regression", "1");
		e1.setIsTDD(false);
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1A");
		e2.setDuration(22);
		e2.setIsTDD(true);
		storage.episodeRecognized(e2);
		
		// we save the new episode
		es = EpisodeFileStorage.loadEpisodes(file);
		
		Assert.assertEquals("should have 2 episodes", 2, es.length);
		
		// the firt episode should have been modified in the file
		Assert.assertEquals("should update category", e1.getCategory(), es[0].getCategory());
		Assert.assertEquals("should update subtype", e1.getSubtype(), es[0].getSubtype());
		Assert.assertEquals("should update duration", e1.getDuration(), es[0].getDuration());
		Assert.assertEquals("should update test result", e1.isTDD(), es[0].isTDD());
		
	}
	
}
