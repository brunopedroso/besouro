package besouro.plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import besouro.classification.randomHeuristic.RandomHeuristicTDDConformance;
import besouro.classification.zorro.ZorroEpisodeClassifierStream;
import besouro.listeners.BesouroListenerSet;
import besouro.model.Action;
import besouro.model.Episode;
import besouro.persistence.ActionFileStorage;
import besouro.persistence.EpisodeFileStorage;
import besouro.persistence.GitRecorder;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodeListener;

public class ProgrammingSession implements ActionOutputStream {

	private BesouroListenerSet eclipseListenerSet;
	
	private ZorroEpisodeClassifierStream zorroClassifier;
	private ZorroEpisodeClassifierStream randomHeuristicClassifier;
	
	private ActionFileStorage actionStorage;
	private EpisodeFileStorage zorroEpisodesStorage;
	private EpisodeFileStorage randomHeuristicEpisodesStorage;
	private EpisodeFileStorage disagreementsStorage;
	
	private File actionsFile;
	private File zorroEpisodesFile;
	private File randomHeuristicEpisodesFile;
	private File disagreementsFile;

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
		
		zorroEpisodesFile = new File(sessionDir, "zorroEpisodes.txt");
		zorroEpisodesStorage = new EpisodeFileStorage(zorroEpisodesFile);
		zorroClassifier = new ZorroEpisodeClassifierStream();
		zorroClassifier.addEpisodeListener(zorroEpisodesStorage);
		
		randomHeuristicEpisodesFile = new File(sessionDir, "randomHeuristicEpisodes.txt");
		randomHeuristicEpisodesStorage = new EpisodeFileStorage(randomHeuristicEpisodesFile);
		randomHeuristicClassifier = new ZorroEpisodeClassifierStream();
		randomHeuristicClassifier.setConformanceCriterion(new RandomHeuristicTDDConformance());
		randomHeuristicClassifier.addEpisodeListener(randomHeuristicEpisodesStorage);
		
		disagreementsFile = new File(sessionDir, "disagreements.txt");
		disagreementsStorage = new EpisodeFileStorage(disagreementsFile);
		
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
		zorroClassifier.addAction(action);
		randomHeuristicClassifier.addAction(action);
		git.addAction(action);
	}

	/**
	 * Used by EpisodeView to allow the user to disagree from a n Episode classification
	 * @param episode
	 */
	public void disagreeFromEpisode(Episode episode) {
		disagreementsStorage.episodeRecognized(episode);
	}


	public void addEpisodeListeners(EpisodeListener episodeListener) {
		// its the classification that will show in the interface
		randomHeuristicClassifier.addEpisodeListener(episodeListener);
	}

	public void close() {
		eclipseListenerSet.unregisterListenersInEclipse();
		git.close();
	}

	public Episode[] getZorroEpisodes() {
		return zorroClassifier.getEpisodes();
	}

	public File getActionsFile() {
		return actionsFile;
	}

	public File getZorroEpisodesFile() {
		return zorroEpisodesFile;
	}

	public File getRandomheuristicEpisodesFile() {
		return randomHeuristicEpisodesFile;
	}
	
	public File getDisagreementsFile() {
		return disagreementsFile;
	}

	
	/** for testing purposes only */
	public void setGitRecorder(GitRecorder git) {
		this.git = git;
		
	}


}
