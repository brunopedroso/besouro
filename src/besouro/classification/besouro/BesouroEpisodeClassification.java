package besouro.classification.besouro;

import java.util.Iterator;

import jess.Batch;
import jess.Fact;
import jess.JessException;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;
import besouro.classification.zorro.ZorroEpisodeClassification;
import besouro.classification.zorro.ZorroTDDConformance;
import besouro.model.Action;
import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.RefactoringAction;
import besouro.model.UnitTestCaseAction;

public class BesouroEpisodeClassification extends ZorroEpisodeClassification {

	public BesouroEpisodeClassification() {
		this.engine = new Rete();
		try {
			Batch.batch("besouro/classification/besouro/Actions.clp", this.engine);
			Batch.batch("besouro/classification/besouro/Episode.clp", this.engine);
			Batch.batch("besouro/classification/besouro/EpisodeClassifier.clp", this.engine);
			
		} catch (JessException e) {
			throw new RuntimeException(e);
		}

	}
	
}
