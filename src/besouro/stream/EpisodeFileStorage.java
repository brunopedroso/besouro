package besouro.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

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
		storeEpisode(e);
	}
	
	public void storeEpisode(Episode e) {
		try {
			
			FileWriter writer = new FileWriter(this.file);
			writer.append(e.getCategory());
			writer.append(" ");
			writer.append(e.getSubtype());
			writer.append(" " + e.getDuration());
			writer.append(" " + e.isTDD());
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
			String line = reader.readLine();
			StringTokenizer tok = new StringTokenizer(line, " ");
			
			episodes[0] = new Episode();
			episodes[0].setClassification(tok.nextToken(), tok.nextToken());
			episodes[0].setDuration(Integer.parseInt(tok.nextToken()));
			episodes[0].setIsTDD(Boolean.parseBoolean(tok.nextToken()));
			
			return episodes;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
