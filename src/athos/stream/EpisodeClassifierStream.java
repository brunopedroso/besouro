package athos.stream;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jess.Batch;
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

	public EpisodeClassifierStream() throws Exception {
	    this.engine = new Rete();
	    Batch.batch("athos/model/Actions.clp", this.engine);
	    Batch.batch("athos/model/Episode.clp", this.engine);
	    Batch.batch("athos/model/EpisodeClassifier.clp", this.engine);
	}
	
	
	public void addAction(Action action) {

		//link the list, to calculate the increases
		if (action instanceof JavaFileAction) {
			
			JavaFileAction linkedAction = (JavaFileAction) action;
			
			JavaFileAction previousPerFile = previousEditActionPerFile.get(linkedAction.getFile());
			linkedAction.setPreviousAction(previousPerFile); // 1st time will be null, I know...
			
			previousEditActionPerFile.put(linkedAction.getFile(), linkedAction);
			
		}
		
		actions.add(action);
		
		System.out.println("[action] " + action);
		
		if(action instanceof UnitTestAction) {
			
			UnitTestAction utAction = (UnitTestAction) action;
			
			if (utAction.isSuccessful()) {
				
				try {
					
					engine.reset();
					
					int i = 1;
					for(Action a: actions) {
						a.assertJessFact(i++, engine);
					}
					
//					Iterator it = engine.listFacts();
//					while (it.hasNext()) {
//						System.out.println(it.next());
//					}
					
					engine.run();
					
					QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
					
					if(result.next()) {
						System.out.println("[episode]");
						System.out.println("\t" + result.getString("cat"));
						System.out.println("\t" + result.getString("tp"));
						
					} else {
						System.out.println("[episode] could not be classified.");
						
					}
					
					
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
				
				// start a new classification
				actions.clear();
				
			}
		}
		
	}


	public List<Action> getActions() {
		return actions;
	}

	
	
}
