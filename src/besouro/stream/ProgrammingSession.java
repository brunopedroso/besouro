package besouro.stream;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	private File actionsFile;
	
	private static ProgrammingSession currentSession;

	private File episodesFile;
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
		
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
		
		actionsFile = new File(basedir, "actions_" + timestamp + ".txt");
		actionStorage = new FileStorageActionStream(actionsFile);
		
		episodesFile = new File(basedir, "episodes_" + timestamp + ".txt");
		episodesStorage = new EpisodeFileStorage(episodesFile);
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

	public File getActionsFile() {
		return actionsFile;
	}

	public File getEpisodesFile() {
		return episodesFile;
	}

}
