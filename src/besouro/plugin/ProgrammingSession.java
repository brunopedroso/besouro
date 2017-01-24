package besouro.plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;
import besouro.classification.randomHeuristic.RandomHeuristicTDDConformance;
import besouro.classification.zorro.ZorroEpisodeClassifierStream;
import besouro.listeners.BesouroListenerSet;
import besouro.model.Action;
import besouro.model.Episode;
import besouro.model.Jacoco;
import besouro.persistence.ActionFileStorage;
import besouro.persistence.EpisodeFileStorage;
import besouro.persistence.GitRecorder;
import besouro.persistence.JacocoFileStorage;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodeListener;

public class ProgrammingSession implements ActionOutputStream {

	private BesouroListenerSet eclipseListenerSet;
	
	private ZorroEpisodeClassifierStream zorroClassifier;
	private ZorroEpisodeClassifierStream randomHeuristicClassifier;
	private BesouroEpisodeClassifierStream besouroClassifier;
	
	private ActionFileStorage actionStorage;
	private JacocoFileStorage jacocoStorage;
	private EpisodeFileStorage zorroEpisodesStorage;
	private EpisodeFileStorage randomHeuristicEpisodesStorage;
	private EpisodeFileStorage disagreementsStorage;
	private EpisodeFileStorage besouroEpisodesStorage;
	
	private EpisodeFileStorage userCommentsEpisodesStorage;
	
	private File actionsFile;
	private File jacocoFile;
	private File zorroEpisodesFile;
	private File randomHeuristicEpisodesFile;
	private File disagreementsFile;
	private File besouroEpisodeFile;
	private File userCommentsFile;

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
		
		jacocoFile = new File(sessionDir, "jacoco.txt");
		jacocoStorage = new JacocoFileStorage(jacocoFile);
		
		
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
		
		userCommentsFile = new File(sessionDir, "userComments.txt");
		userCommentsEpisodesStorage = new EpisodeFileStorage(userCommentsFile);
		
		besouroEpisodeFile = new File(sessionDir, "besouroEpisodes.txt");
		besouroEpisodesStorage = new EpisodeFileStorage(besouroEpisodeFile);
		besouroClassifier = new BesouroEpisodeClassifierStream();
		besouroClassifier.addEpisodeListener(besouroEpisodesStorage);
		
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
		besouroClassifier.addAction(action);
		Jacoco jacocoAction = new Jacoco();
		jacocoStorage.addAction(jacocoAction);
		git.addAction(action);
		
	}

	/**
	 * Used by EpisodeView to allow the user to disagree from a n Episode classification
	 * @param episode
	 */
	public void disagreeFromEpisode(Episode episode) {
		disagreementsStorage.episodeRecognized(episode);
	}

	/**
	 * Used by EpisodeView to allow the user to make comments on episodes
	 * @param episode
	 */
	public void commentEpisode(Episode episode) {
		userCommentsEpisodesStorage.episodeRecognized(episode);
	}
	

	public void addEpisodeListeners(EpisodeListener episodeListener) {
		// its the classification that will show in the interface
		randomHeuristicClassifier.addEpisodeListener(episodeListener);
	}

	public Episode[] getEpisodes() {
		// its the classification that will show in the interface
		return randomHeuristicClassifier.getEpisodes();
	}
	
	public void close() {
		eclipseListenerSet.unregisterListenersInEclipse();
		git.close();
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
	
	public File getBesouroEpisodesFile() {
		return besouroEpisodeFile;
	}

	
	public File getDisagreementsFile() {
		return disagreementsFile;
	}

	
	/** for testing purposes only */
	public void setGitRecorder(GitRecorder git) {
		this.git = git;
		
	}

}
