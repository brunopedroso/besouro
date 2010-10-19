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
import besouro.persistence.GitRecorder;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodeListener;
import besouro.zorro.ZorroEpisodeClassifierStream;

public class ProgrammingSession implements ActionOutputStream {

	private BesouroListenerSet eclipseListenerSet;
	
	private ZorroEpisodeClassifierStream classifier;
	
	private ActionFileStorage actionStorage;
	private EpisodeFileStorage episodesStorage;
	
	private File actionsFile;
	private File episodesFile;

	private GitRecorder git;
	
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
		
		File besouroDir = new File(basedir, ".besouro");
		besouroDir.mkdir();
		
		File sessionDir = new File(besouroDir, timestamp);
		sessionDir.mkdir();
		
		actionsFile = new File(sessionDir, "actions.txt");
		actionStorage = new ActionFileStorage(actionsFile);
		
		episodesFile = new File(sessionDir, "episodes.txt");
		episodesStorage = new EpisodeFileStorage(episodesFile);
		
		classifier = new ZorroEpisodeClassifierStream();
		classifier.addEpisodeListener(episodesStorage);
		
		eclipseListenerSet = listeners;
		eclipseListenerSet.setOutputStream(this);
		
		git = new GitRecorder(basedir);
		
	}
	
	public void start() {
		eclipseListenerSet.registerListenersInEclipse();
		git.createRepoIfNeeded();
	}


	public void addAction(Action action) {
		actionStorage.addAction(action);
		classifier.addAction(action);
		git.addAction(action);
	}

	public void addEpisodeListeners(EpisodeListener episodeListener) {
		classifier.addEpisodeListener(episodeListener);
	}

	public void close() {
		eclipseListenerSet.unregisterListenersInEclipse();
		git.close();
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

	/** for testing purposes only */
	public void setGitRecorder(GitRecorder git) {
		this.git = git;
		
	}



}
