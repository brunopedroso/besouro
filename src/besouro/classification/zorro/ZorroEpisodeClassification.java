package besouro.classification.zorro;

import java.util.Iterator;

import jess.Batch;
import jess.Fact;
import jess.JessException;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;
import besouro.model.Action;
import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.RefactoringAction;
import besouro.model.UnitTestCaseAction;

public class ZorroEpisodeClassification {

	protected Rete engine;
	
	public ZorroEpisodeClassification() {
		this.engine = new Rete();
		try {
			Batch.batch("besouro/classification/zorro/Actions.clp", this.engine);
			Batch.batch("besouro/classification/zorro/Episode.clp", this.engine);
			Batch.batch("besouro/classification/zorro/EpisodeClassifier.clp", this.engine);
			
		} catch (JessException e) {
			throw new RuntimeException(e);
		}

	}
	
	public void classifyEpisode(Episode episode) {
		
		try {

			QueryResult result = queryjessRules(episode);

			//  redundancy. It should be a while!
			if (result.next()) {
				episode.setClassification(result.getString("cat"), result.getString("tp"));
			}
			
			while (result.next()) {
				System.out.println("loosing classification " + result.getString("cat") + result.getString("tp"));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected QueryResult queryjessRules(Episode episode) {
		
		try {

			engine.reset();
			
			int i = 1;
			for (Action a : episode.getActions()) {
				assertJessFact(i++, a);
			}
			
			engine.run();
			
			//debugFacts();
			
			QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
			return result;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		
	}

	private void debugFacts() {
		Iterator it = engine.listFacts();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void assertJessFact(int index, Action action){
		
		try {
			
			Fact f = null;
			
			if (action instanceof CompilationAction) {
				CompilationAction ca = (CompilationAction) action;
				f = new Fact("CompilationAction", engine);
				f.setSlotValue("index", new Value(index, RU.INTEGER));
				f.setSlotValue("file",  new Value(ca.getResource(), RU.STRING));
				f.setSlotValue("message", new Value(ca.getErrorMessage(), RU.STRING));
				f = engine.assertFact(f);
				
			} else if (action instanceof EditAction) {
				
				EditAction ca = (EditAction) action;
				
				if (ca.isSubstantial()) {
					
					if (ca.isTestEdit()) {
						f = new Fact("UnitTestEditAction", engine);
						f.setSlotValue("testChange", new Value(ca.getTestMethodIncrease(), RU.INTEGER));
						f.setSlotValue("assertionChange", new Value(ca.getTestAssertionIncrease(), RU.INTEGER));
						
					} else {
						f = new Fact("ProductionEditAction", engine);
						f.setSlotValue("methodChange", new Value(ca.getMethodIncrease(), RU.INTEGER));
						f.setSlotValue("statementChange", new Value(ca.getStatementIncrease(), RU.INTEGER));
					}
					
					f.setSlotValue("index", new Value(index, RU.INTEGER));
					f.setSlotValue("file", new Value(ca.getResource(), RU.STRING));
					f.setSlotValue("byteChange", new Value(ca.getFileSizeIncrease(), RU.INTEGER));
					
					f = engine.assertFact(f);
				}
				
			} else if (action instanceof RefactoringAction) {
				
				RefactoringAction ca = (RefactoringAction) action;
				
				f = new Fact("UnaryRefactorAction", engine);
				
				f.setSlotValue("index", new Value(index, RU.INTEGER));
				f.setSlotValue("file",  new Value(ca.getResource(), RU.STRING));
				
				f.setSlotValue("operation", new Value(ca.getOperator(),RU.STRING));
				f.setSlotValue("type", new Value(ca.getSubjectType(),RU.STRING));
				f.setSlotValue("data", new Value(ca.getSubjectName(), RU.STRING));
				
				f = engine.assertFact(f);
				
			} else if (action instanceof UnitTestCaseAction) {
				
				UnitTestCaseAction ca = (UnitTestCaseAction) action;
				
				f = new Fact("UnitTestAction", engine);
				f.setSlotValue("index", new Value(index, RU.INTEGER));
				f.setSlotValue("file",new Value(ca.getResource(), RU.STRING));
				
				if (!ca.isSuccessful()) {
					f.setSlotValue("errmsg", new Value(ca.isSuccessful() ? "true" : "failure", RU.STRING));
				}
				
				f = engine.assertFact(f);
			}
			
		} catch (JessException e) {
			throw new RuntimeException(e);
		}
		
		
	}

	public Rete getEngine() {
		return engine;
	}
}
