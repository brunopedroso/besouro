package besouro.stream;

import besouro.model.Episode;

public interface EpisodesRecognizerActionStream extends ActionOutputStream{
	
	public Episode[] getEpisodes();

}
