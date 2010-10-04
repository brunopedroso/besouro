package besouro.stream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Episode;

public class EpisodeFileStorageTest {

	
	private File file;
	private EpisodeFileStorage storage;

	@Before
	public void setup() {
		file = new File("testFile.txt");
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
	public void shouldNotEraseThePreviousContentOfTheFile() {
		
		FileWriter writer;
		try {
			
			writer = new FileWriter(file);
			writer.write("some previous content");
			writer.close();
			
			long length = file.length();
			
			storage = new EpisodeFileStorage(file);
			
			Assert.assertEquals("should preserve the length of file", length ,file.length());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	}

	
	
}
