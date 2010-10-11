package besouro.plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import besouro.listeners.BesouroListenerSet;
import besouro.model.Action;
import besouro.model.Episode;
import besouro.persistence.ActionFileStorage;
import besouro.persistence.EpisodeFileStorage;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodeListener;
import besouro.zorro.ZorroEpisodeClassifierStream;

public class ProgrammingSession implements ActionOutputStream, EpisodeListener {

	private BesouroListenerSet eclipseListenerSet;
	
	private ZorroEpisodeClassifierStream classifier;
	
	private ActionFileStorage actionStorage;
	private EpisodeFileStorage episodesStorage;
	
	private List<EpisodeListener> listeners = new ArrayList<EpisodeListener>();

	private File actionsFile;
	private File episodesFile;
	
	private static ProgrammingSession currentSession;

	public static ProgrammingSession newSession(File basedir) {
		return newSession(basedir, BesouroListenerSet.getSingleton());
	}
	
	public static ProgrammingSession newSession(File basedir, BesouroListenerSet listeners) {
		if(currentSession!=null){
			currentSession.close();
		}
		currentSession = new ProgrammingSession(basedir, listeners);
		return currentSession;
	}
	

	private ProgrammingSession(File basedir, BesouroListenerSet listeners) {
		
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
		
		File sessionDir = new File(basedir, timestamp);
		sessionDir.mkdir();
		
		actionsFile = new File(sessionDir, "actions.txt");
		actionStorage = new ActionFileStorage(actionsFile);
		
		episodesFile = new File(sessionDir, "episodes.txt");
		episodesStorage = new EpisodeFileStorage(episodesFile);
		this.addEpisodeListeners(episodesStorage);
		
		classifier = new ZorroEpisodeClassifierStream();
		classifier.addEpisodeListener(this);
		
		eclipseListenerSet = listeners;
		eclipseListenerSet.setOutputStream(this);
		
	}
	
	public void start() {
		eclipseListenerSet.registerListenersInEclipse();
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
