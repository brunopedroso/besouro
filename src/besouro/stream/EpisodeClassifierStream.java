package besouro.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jess.Batch;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;
import besouro.measure.TDDMeasure;
import besouro.model.Action;
import besouro.model.Episode;
import besouro.model.JavaFileAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestSessionAction;

public class EpisodeClassifierStream implements ActionOutputStream {

	private Rete engine;
	List<Action> actions = new ArrayList<Action>();

	private Map<String, JavaFileAction> previousEditActionPerFile = new HashMap<String, JavaFileAction>();
	private List<Episode> episodes = new ArrayList<Episode>();
	private TDDMeasure measure;

	public EpisodeClassifierStream() throws Exception {
		this.engine = new Rete();
		measure = new TDDMeasure();
		Batch.batch("besouro/model/Actions.clp", this.engine);
		Batch.batch("besouro/model/Episode.clp", this.engine);
		Batch.batch("besouro/model/EpisodeClassifier.clp", this.engine);
	}

	public void addAction(Action action) {

		linkActions(action);
		actions.add(action);
		
		System.out.println("[action] " + action);
		
		if (action instanceof UnitTestSessionAction) {

			UnitTestAction utAction = (UnitTestAction) action;

			if (utAction.isSuccessful()) {
				
				try {

					engine.reset();

					int i = 1;
					for (Action a : actions) {
						a.assertJessFact(i++, engine);
					}

					engine.run();
					
					//debugFacts();

					QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());

					// TODO [rule] recognizing various episodes. Shouldnt it be a while?
					if (result.next()) {
						
						Episode episode = new Episode();
						episode.addActions(actions);
						episode.setClassification(result.getString("cat"), result.getString("tp"));
						
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
						
//					} else {
//						System.out.println("[episode] could not be classified.");

					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				// start a new classification
				actions.clear();

			}
		}

	}

	private void debugFacts() {
		Iterator it = engine.listFacts();
		while (it.hasNext()) {
			System.out.println(it.next());
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

	public TDDMeasure getTDDMeasure() {
		return measure;
	}

}
