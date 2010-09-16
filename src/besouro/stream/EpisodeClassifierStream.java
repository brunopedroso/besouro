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
	private JavaActionsMeasurer javaActionsMeasurer;
	
	List<Action> actions = new ArrayList<Action>();
	private Episode previsousEpisode;


	/**
	 * for testing purposes
	 */
	public JavaActionsMeasurer getJavaActionsMeasurer() {
		return javaActionsMeasurer;
	}

	public EpisodeClassifierStream() throws Exception {
		classifier = new ZorroEpisodeClassification();
		measure = new ZorroTDDMeasure();
		javaActionsMeasurer = new JavaActionsMeasurer();
	}

	public void addAction(Action action) {

		System.out.println("[action] " + action);
		
		javaActionsMeasurer.measureJavaActions(action);
		
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
		if (previsousEpisode!=null) episode.setPreviousEpisode(previsousEpisode);
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


	public ZorroTDDMeasure getTDDMeasure() {
		return measure;
	}

}
