package besouro.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import besouro.model.Episode;
import besouro.stream.EpisodeListener;

public class EpisodeFileStorage implements EpisodeListener {

	private File file;
	private FileWriter writer;
	private TreeMap<Long, Episode> episodes = new TreeMap<Long, Episode>();

	public EpisodeFileStorage(File file) {
		try {
			
			this.file = file;
			this.file.createNewFile();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void episodeRecognized(Episode e) {
		episodes.put(e.getTimestamp(), e);
		save();
	}
	
	public void save() {
		
		try {
			
			// restarts the file
			writer = new FileWriter(this.file);
			
			for (Episode ep: episodes.values())
				saveEpisodeToFile(ep);
			
			writer.close();
			
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	private void saveEpisodeToFile(Episode e) throws IOException {
		
		writer.append("" + e.getTimestamp());
		
		writer.append(" " + e.getCategory());
		writer.append(" " + e.getSubtype());
		writer.append(" " + e.getDuration());
		writer.append(" " + e.isTDD());
		writer.append("the end");
		writer.append("\n");
		
		writer.flush();
		
	}
	
	public static Episode[] loadEpisodes(File file) {
		try {
		
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(file));
		
			List<Episode> list = new ArrayList<Episode>();
			String line = reader.readLine();
			
			while (line != null){
				
				StringTokenizer tok = new StringTokenizer(line, " ");
				
				Episode e = new Episode();
				e.setTimestamp(Long.parseLong(tok.nextToken()));
				e.setClassification(tok.nextToken(), tok.nextToken());
				e.setDuration(Integer.parseInt(tok.nextToken()));
				e.setIsTDD(Boolean.parseBoolean(tok.nextToken()));
				
				list.add(e);
				
				line = reader.readLine();
			}
			
			return list.toArray(new Episode[list.size()]);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
