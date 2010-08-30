package athos.stream;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jess.Batch;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;
import athos.model.Action;
import athos.model.EditAction;
import athos.model.UnitTestAction;

public class EpisodeClassifierStream implements ActionOutputStream {

	private Rete engine;
	List<Action> actions = new ArrayList<Action>();
	
	private Map<File, EditAction> previousEditActionPerFile = new HashMap<File, EditAction>();

	public EpisodeClassifierStream() throws Exception {
	    this.engine = new Rete();
	    Batch.batch("athos/model/Actions.clp", this.engine);
	    Batch.batch("athos/model/Episode.clp", this.engine);
	    Batch.batch("athos/model/EpisodeClassifier.clp", this.engine);
	}
	
	
	public void addAction(Action action) {

		//link the list, to calculate the duration and increases
		if (action instanceof EditAction) {
			
			EditAction edit = (EditAction) action;
			
			EditAction previousPerFile = previousEditActionPerFile.get(edit.getFile());
			edit.setPreviousAction(previousPerFile); // 1st time will be null, I know...
			
			previousEditActionPerFile.put(edit.getFile(), edit);
			
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
