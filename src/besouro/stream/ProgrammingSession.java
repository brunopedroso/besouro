package besouro.stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.plugin.EpisodeListener;
import besouro.plugin.ListenersSet;

public class ProgrammingSession implements ActionOutputStream, EpisodeListener {

	private ListenersSet eclipseListenerSet;
	
	private EpisodeClassifierStream classifier;
	
	private FileStorageActionStream actionStorage;
	private EpisodeFileStorage episodesStorage;
	
	private List<EpisodeListener> listeners = new ArrayList<EpisodeListener>();
	
	private static ProgrammingSession currentSession;
	public static ProgrammingSession newSession(File basedir) {
		return newSession(basedir, ListenersSet.getSingleton());
	}
	
	public static ProgrammingSession newSession(File basedir, ListenersSet listeners) {
		if(currentSession!=null){
			currentSession.close();
		}
		currentSession = new ProgrammingSession(basedir, listeners);
		return currentSession;
	}
	

	private ProgrammingSession(File basedir, ListenersSet listeners) {
		
		actionStorage = new FileStorageActionStream(new File(basedir, "actions.txt"));
		
		episodesStorage = new EpisodeFileStorage(new File(basedir, "episodes.txt"));
		this.addEpisodeListeners(episodesStorage);
		
		classifier = new EpisodeClassifierStream();
		classifier.addEpisodeListener(this);
		
		eclipseListenerSet = listeners;
		eclipseListenerSet.registerListenersInEclipse();
		eclipseListenerSet.setOutputStream(this);
		
	}

	public void addAction(Action action) {
		actionStorage.addAction(action);
		classifier.addAction(action);
	}

	public void addEpisodeListeners(EpisodeListener episodeListener) {
		listeners.add(episodeListener);
	}

	public void episodeRecognized(Episode e) {
		for(EpisodeListener lis: listeners){
			lis.episodeRecognized(e);
		}
		
	}

	public void close() {
		eclipseListenerSet.unregisterListenersInEclipse();
	}

	public Episode[] getEpisodes() {
		return classifier.getEpisodes();
	}

}
