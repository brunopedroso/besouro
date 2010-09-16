package besouro.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import besouro.listeners.JavaStatementMeter;
import besouro.model.Action;
import besouro.model.Episode;
import besouro.model.JavaFileAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestSessionAction;
import besouro.zorro.ZorroEpisodeClassification;
import besouro.zorro.ZorroTDDMeasure;

public class EpisodeClassifierStream implements ActionOutputStream {

	private ZorroEpisodeClassification classifier;
	private ZorroTDDMeasure measure;
	
	List<Action> actions = new ArrayList<Action>();

	private Map<String, JavaFileAction> previousEditActionPerFile = new HashMap<String, JavaFileAction>();
	private JavaStatementMeter javaFileMeasurer;
	private Episode previsousEpisode;

	/**
	 * for testing purposes
	 * @param javaFileMeasurer
	 */
	public void setJavaFileMeasurer(JavaStatementMeter javaFileMeasurer) {
		this.javaFileMeasurer = javaFileMeasurer;
	}

	public EpisodeClassifierStream() throws Exception {
		classifier = new ZorroEpisodeClassification();
		measure = new ZorroTDDMeasure();
		javaFileMeasurer = new JavaStatementMeter();
	}

	public void addAction(Action action) {

		System.out.println("[action] " + action);
		
		linkActions(action);
		measureJavaActions(action);
		
		Episode episode = recognizeEpisode(action);

		if (episode != null) {
			
			linkEpisodes(episode);
			classifier.classifyEpisode(episode);
			measure.addEpisode(episode);
			
			System.out.println(episode);
			System.out.println("-----------------");
			System.out.println("\t#episodes: " + measure.countEpisodes());
			System.out.println("\t duration: " + measure.getTDDPercentageByDuration());
			System.out.println("\t   number: " + measure.getTDDPercentageByNumber());
			System.out.println("-----------------");

		}
		
	}

	private void linkEpisodes(Episode episode) {
		if (previsousEpisode!=null)
			episode.setPreviousEpisode(previsousEpisode);
		previsousEpisode = episode;
	}

	private Episode recognizeEpisode(Action action) {
		
		actions.add(action);
		if (action instanceof UnitTestSessionAction) {

			if (((UnitTestAction) action).isSuccessful()) {
				
				Episode episode = new Episode();
				episode.addActions(actions);
				actions.clear();
				return episode;
				
			}
		}
		
		return null;
	}

	private void measureJavaActions(Action action) {
		if (action instanceof JavaFileAction) {
			
			JavaFileAction javaAction = (JavaFileAction) action;
			JavaStatementMeter metrics = javaFileMeasurer.measureJavaFile((IFile) javaAction.getResource());
			
			javaAction.setFileSize((int) javaAction.getResource().getLocation().toFile().length());
			javaAction.setIsTestEdit(metrics.isTest());
			javaAction.setMethodsCount(metrics.getNumOfMethods());
			javaAction.setStatementsCount(metrics.getNumOfStatements());
			javaAction.setTestMethodsCount(metrics.getNumOfTestMethods());
			javaAction.setTestAssertionsCount(metrics.getNumOfTestAssertions());
			
		}
	}

	private void linkActions(Action action) {
		
		// link the list, to calculate the increases
		if (action instanceof JavaFileAction) {

			JavaFileAction linkedAction = (JavaFileAction) action;
			String path = linkedAction.getResource().getName();
			
			linkedAction.setPreviousAction(previousEditActionPerFile.get(path)); // 1st time will be null, I know...

			previousEditActionPerFile.put(path, linkedAction);

		}
	}

	public List<Action> getActions() {
		return actions;
	}

	public ZorroTDDMeasure getTDDMeasure() {
		return measure;
	}

}
