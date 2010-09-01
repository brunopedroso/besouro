package athos.stream;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jess.Batch;
import jess.JessException;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;
import athos.model.Action;
import athos.model.JavaFileAction;
import athos.model.UnitTestAction;

public class EpisodeClassifierStream implements ActionOutputStream {

	private Rete engine;
	List<Action> actions = new ArrayList<Action>();

	private Map<File, JavaFileAction> previousEditActionPerFile = new HashMap<File, JavaFileAction>();
	private List<String> episodes = new ArrayList<String>();

	public EpisodeClassifierStream() throws Exception {
		this.engine = new Rete();
		Batch.batch("athos/model/Actions.clp", this.engine);
		Batch.batch("athos/model/Episode.clp", this.engine);
		Batch.batch("athos/model/EpisodeClassifier.clp", this.engine);
	}

	public void addAction(Action action) {


		actions.add(action);

		System.out.println("[action] " + action);
		
		linkActions(action);

		if (action instanceof UnitTestAction) {

			UnitTestAction utAction = (UnitTestAction) action;

			if (utAction.isSuccessful()) {

				try {

					engine.reset();

					int i = 1;
					for (Action a : actions) {
						a.assertJessFact(i++, engine);
					}

					// Iterator it = engine.listFacts();
					// while (it.hasNext()) {
					// System.out.println(it.next());
					// }

					engine.run();

					QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());

					if (result.next()) {
						String episode = "[episode]"+" " + result.getString("cat") + " " + result.getString("tp");
						episodes.add(episode);
						System.out.println(episode);

					} else {
						System.out
								.println("[episode] could not be classified.");

					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				// start a new classification
				actions.clear();

			}
		}

	}
	
//	public void printRulesOut() {
//		try {
//			
//			Iterator it = engine.listActivations();
//			Object rule = null;
//			for (;it.hasNext(); rule = it.next()) {
//				System.out.println(rule.toString());
//			}
//			
//		} catch (JessException e) {
//			throw new RuntimeException(e);
//		}
//	}

	private void linkActions(Action action) {
		
		// link the list, to calculate the increases
		if (action instanceof JavaFileAction) {

			JavaFileAction linkedAction = (JavaFileAction) action;
			
			JavaFileAction previousPerFile = previousEditActionPerFile.get(linkedAction.getFile());
			
//			System.out.println("  ==>" + (previousPerFile==null?"null":"Achei") + " " + linkedAction.getFile() + ".");

			linkedAction.setPreviousAction(previousPerFile); // 1st time will be null, I know...

			previousEditActionPerFile.put(linkedAction.getFile(), linkedAction);

		}
	}

	public List<Action> getActions() {
		return actions;
	}

	public List<String> getRecognizedEpisodes() {
		return episodes;
	}

}
