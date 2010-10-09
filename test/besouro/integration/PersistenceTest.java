package besouro.integration;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.stream.EpisodeClassifierStream;
import besouro.stream.EpisodeFileStorage;
import besouro.stream.ActionFileStorage;

public class PersistenceTest extends IntegrationTestBaseClass {
	
	protected File actionsFile;
	protected File episodesFile;
	
	@Before
	public void setup() throws Exception {
		
		actionsFile = new File("test/actions.txt");
		episodesFile = new File("test/episodes.txt");
		
		stream.setActionsFile(actionsFile);
		stream.setEpisodesFile(episodesFile);
		setup(stream);
	}
	
	@After
	public void tearDown() {
		actionsFile.delete();
		episodesFile.delete();
	}

	
	@Test
	public void shouldPersistActions() throws Exception {
		
		addTestFirst1Actions();
		
		Action[] actions = ActionFileStorage.loadFromFile(actionsFile);
		
		// 7 actions in the factory + 2 openFile's and 2 unitTestSession's
		Assert.assertEquals(11, actions.length);
	}
	
	@Test
	public void shouldPersistEpisodes() throws Exception {
		
		addTestFirst1Actions();
		
		Episode[] episodes = EpisodeFileStorage.loadEpisodes(episodesFile);
		
		Assert.assertEquals(1, episodes.length);
	}
	
}
