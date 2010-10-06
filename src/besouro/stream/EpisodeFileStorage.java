package besouro.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import besouro.model.Episode;
import besouro.plugin.EpisodeListener;

public class EpisodeFileStorage implements EpisodeListener {

	private File file;
	private FileWriter writer;

	public EpisodeFileStorage(File file) {
		try {
			
			this.file = file;
			this.file.createNewFile();
			writer = new FileWriter(this.file, true);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void episodeRecognized(Episode e) {
		storeEpisode(e);
	}
	
	public void storeEpisode(Episode e) {
		try {
			
			
			long time = -1;
			
			if (e.getLastAction() != null) {
				time = e.getLastAction().getClock().getTime();
				
			} else {
				time = e.getTimestamp();
				
			}
			
			writer.append("" + time);
			
			writer.append(" " + e.getCategory());
			writer.append(" " + e.getSubtype());
			writer.append(" " + e.getDuration());
			writer.append(" " + e.isTDD());
			writer.append("\n");
			
			writer.flush();
			
		} catch (IOException exc) {
			throw new RuntimeException(exc);
		}
		
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
