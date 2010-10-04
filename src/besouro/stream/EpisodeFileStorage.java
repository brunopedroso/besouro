package besouro.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import besouro.model.Episode;
import besouro.plugin.EpisodeListener;

public class EpisodeFileStorage implements EpisodeListener {

	private File file;

	public EpisodeFileStorage(File file) {
		try {
			
			this.file = file;
			this.file.createNewFile();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void episodeRecognized(Episode e) {
		try {
			
			FileWriter writer = new FileWriter(this.file);
			writer.append(e.getCategory());
			writer.flush();
			
		} catch (IOException exc) {
			throw new RuntimeException(exc);
		}
	}

	public static Episode[] loadEpisodes(File file) {
		try {
		
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(file));
		
			Episode[] episodes = new Episode[1];
			episodes[0] = new Episode();
			episodes[0].setClassification(reader.readLine(), null);
			
			return episodes;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
