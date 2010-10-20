package besouro.classification.zorro;

import jess.Batch;
import jess.Fact;
import jess.JessException;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;
import junit.framework.TestCase;

/**
 * Tests one-way TDD heuristic algorithm
 * 
 * @author Hongbing Kou
 */
public class OneWayTDDHeuristicAlgorithmTest extends TestCase {

	private Rete engine;

	protected void setUp() throws Exception {
		this.engine = new Rete();
		Batch.batch("besouro/zorro/EpisodeTDDConformance.clp", this.engine);
		Batch.batch("besouro/zorro/OneWayTDDHeuristicAlgorithm.clp",this.engine);
	}

	public Fact assertConformanceEpisode(int index, String category, String subtype) throws JessException {
		Fact f = new Fact("EpisodeTDDConformance", engine);
		f.setSlotValue("index", new Value(index, RU.INTEGER));
		f.setSlotValue("category", new Value(category, RU.STRING));
		f.setSlotValue("subtype", new Value(subtype, RU.STRING));

		Fact assertedFact = this.engine.assertFact(f);
		return assertedFact;
	}

	public void testPatternOne() throws Exception {
		engine.reset();

		assertConformanceEpisode(1, "test-first", "1");
		assertConformanceEpisode(2, "refactoring", "1");
		assertConformanceEpisode(3, "test-addition", "1");
		assertConformanceEpisode(4, "test-first", "1");
		engine.run();

		QueryResult result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(1, RU.INTEGER)));
		assertTrue("Result to episode 1", result.next());
		assertEquals("Test episode 1", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(2, RU.INTEGER)));
		assertTrue("Result to episode 2", result.next());
		assertEquals("Test episode 2", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(3, RU.INTEGER)));
		assertTrue("Result to episode 3", result.next());
		assertEquals("Test episode 3", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(4, RU.INTEGER)));
		assertTrue("Result to episode 4", result.next());
		assertEquals("Test episode 4", "True", result.getString("isTDD"));
	}
}