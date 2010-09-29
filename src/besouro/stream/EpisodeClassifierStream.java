package besouro.stream;

import java.util.ArrayList;
import java.util.List;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.model.JavaFileAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestSessionAction;
import besouro.plugin.EpisodeListener;
import besouro.zorro.ZorroEpisodeClassification;
import besouro.zorro.ZorroTDDMeasure;

public class EpisodeClassifierStream implements ActionOutputStream {

	private ZorroEpisodeClassification classifier;
	private ZorroTDDMeasure measure;
	private JavaActionsLinker javaActionsLinker;
	
	List<Action> actions = new ArrayList<Action>();
	List<Episode> episodes = new ArrayList<Episode>();
	
	private EpisodeListener listener;

	public EpisodeClassifierStream() throws Exception {
		classifier = new ZorroEpisodeClassification();
		measure = new ZorroTDDMeasure();
		javaActionsLinker = new JavaActionsLinker();
	}

	public void addAction(Action action) {

		System.out.println("[action] " + action);
		
		if (action instanceof JavaFileAction) {
			javaActionsLinker.linkActions((JavaFileAction) action);
		}
		
		Episode episode = recognizeEpisode(action);

		if (episode != null) {
			
			classifier.classifyEpisode(episode);
			measure.addEpisode(episode);
			episodes.add(episode);
			
			if (listener!=null)
				listener.episodeRecognized(episode);
			
			System.out.println(episode);
			System.out.println("-----------------");
			System.out.println("\t#episodes: " + measure.countEpisodes());
			System.out.println("\t duration: " + measure.getTDDPercentageByDuration());
			System.out.println("\t   number: " + measure.getTDDPercentageByNumber());
			System.out.println("-----------------");

		}
		
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

	/**
	 * for testing purposes
	 */
	public JavaActionsLinker getJavaActionsMeasurer() {
		return javaActionsLinker;
	}

	public void addEpisodeListener(EpisodeListener listener) {
		this.listener = listener;
		
	}

	public Episode[] getEpisodes() {
		return episodes.toArray(new Episode[episodes.size()]);
	}


}
