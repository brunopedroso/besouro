package athos.stream;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import jess.Batch;
import jess.JessException;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;

import athos.model.Action;
import athos.model.UnitTestAction;

public class EpisodeClassifierStream implements ActionOutputStream {

	private Rete engine;

	public EpisodeClassifierStream() throws Exception {
		
	    this.engine = new Rete();
	    Batch.batch("athos/model/Episode.clp", this.engine);
	    Batch.batch("athos/model/Actions.clp", this.engine);
	    Batch.batch("athos/model/EpisodeClassifier.clp", this.engine);

	}
	
	List<Action> actions = new ArrayList<Action>();
	
	public void addAction(Action action) {
		
		actions.add(action);
		
		System.out.println("[action] " + action);
		
		if(action instanceof UnitTestAction) {
			
			UnitTestAction utAction = (UnitTestAction) action;
			
			if (utAction.isSuccessful()) {
				
				try {
					
					engine.reset();
					for(Action a: actions) {
						engine.add(a);
					}
					
					QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
					
					if(result.next()) {
						System.out.println("[episode]");
						System.out.println("\t" + result.getString("cat"));
						System.out.println("\t" + result.getString("tp"));
						
					} else {
						System.out.println("[episode] could not be classified:");
						
					}
					
					
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
				
				// start a new classification
				actions.clear();
				
			}
		}
		
	}

	
	
	
}
