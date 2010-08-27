package athos.stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jess.Batch;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;
import athos.model.Action;
import athos.model.UnitTestAction;

public class EpisodeClassifierStream implements ActionOutputStream {

	private Rete engine;
	List<Action> actions = new ArrayList<Action>();
	private long previousActionTimestamp;

	public EpisodeClassifierStream() throws Exception {
	    this.engine = new Rete();
	    Batch.batch("athos/model/Actions.clp", this.engine);
	    Batch.batch("athos/model/Episode.clp", this.engine);
	    Batch.batch("athos/model/EpisodeClassifier.clp", this.engine);

	    // starts to count the duration of the first action when created
	    previousActionTimestamp = new Date().getTime();
	    
	}
	
	
	public void addAction(Action action) {
		
		calculateDuration(action);
		
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

	private void calculateDuration(Action action) {
		long thisActionTimestamp = action.getClock().getDate().getTime();
		long duration = (thisActionTimestamp - previousActionTimestamp)/1000;
		
		action.setDuration((int) duration);
		previousActionTimestamp = thisActionTimestamp;
	}

	public List<Action> getActions() {
		return actions;
	}

	
	
}
