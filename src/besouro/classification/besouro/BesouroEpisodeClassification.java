package besouro.classification.besouro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	
	/**
	 * may classify various episodes, following rules resuts
	 * @param episode
	 * @return
	 */
	public List<Episode> classifyEpisodesWithRedundancy(Episode episode) {
		
		try {

			QueryResult result = queryjessRules(episode);
			
			List<Episode> results = new ArrayList<Episode>();
			int i=0;
			while (result.next()) {
				Episode e = new Episode();
				e.setDuration(episode.getDuration());
				e.setTimestamp(episode.getTimestamp() + (i++)); // i to diferentiate timestamps so they are persisted
				e.setClassification(result.getString("cat"), result.getString("tp"));
				results.add(e);
			}

			return results;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
}
