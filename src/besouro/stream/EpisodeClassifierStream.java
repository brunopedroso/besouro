package besouro.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.model.JavaFileAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestSessionAction;
import besouro.zorro.ZorroEpisodeClassification;
import besouro.zorro.ZorroTDDMeasure;

public class EpisodeClassifierStream implements ActionOutputStream {

	private ZorroEpisodeClassification classifier;
	List<Action> actions = new ArrayList<Action>();

	private Map<String, JavaFileAction> previousEditActionPerFile = new HashMap<String, JavaFileAction>();
	private List<Episode> episodes = new ArrayList<Episode>();
	private ZorroTDDMeasure measure;

	public EpisodeClassifierStream() throws Exception {
		classifier = new ZorroEpisodeClassification();
		measure = new ZorroTDDMeasure();
	}

	public void addAction(Action action) {

		linkActions(action);
		actions.add(action);
		
		System.out.println("[action] " + action);
		
		if (action instanceof UnitTestSessionAction) {

			UnitTestAction utAction = (UnitTestAction) action;

			if (utAction.isSuccessful()) {
				
				Episode episode = new Episode();
				episode.addActions(actions);
				
				classifier.classifyEpisode(episode);
				
				if (episodes.size()>0)
					episode.setPreviousEpisode(episodes.get(episodes.size()-1));
				
				episodes.add(episode);
				measure.addEpisode(episode);
				
				System.out.println(episode);
				System.out.println("-----------------");
				System.out.println("\t#episodes: " + episodes.size());
				System.out.println("\t duration: " + measure.getTDDPercentageByDuration());
				System.out.println("\t   number: " + measure.getTDDPercentageByNumber());
				System.out.println("-----------------");
				
				// start a new episode
				actions.clear();

			}
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

	public List<Episode> getRecognizedEpisodes() {
		return episodes;
	}

	public ZorroTDDMeasure getTDDMeasure() {
		return measure;
	}

}
