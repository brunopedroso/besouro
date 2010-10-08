package besouro.stream;

import java.io.File;

import besouro.model.Action;

public class ProgrammingSession implements ActionOutputStream {

	private FileStorageActionStream actionStorage;
	private EpisodeFileStorage episodesStorage;
	private EpisodeClassifierStream classifier;

	public ProgrammingSession(File basedir) {
		actionStorage = new FileStorageActionStream(new File(basedir, "actions.txt"));
		episodesStorage = new EpisodeFileStorage(new File(basedir, "episodes.txt"));
		classifier = new EpisodeClassifierStream();
		classifier.addEpisodeListener(episodesStorage);
	}

	public static ProgrammingSession newSession(File basedir) {
		ProgrammingSession session = new ProgrammingSession(basedir);
		return session;
	}

	public void addAction(Action action) {
		actionStorage.addAction(action);
		classifier.addAction(action);
	}

}
