package besouro.measure;

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
 * Test two-way heuristic algorithm of TDD conformance evaluation.
 * 
 * @author Hongbing Kou
 */
public class TestTwoWayTDDHeuristicAlgorithm extends TestCase {

	private Rete engine;

	protected void setUp() throws Exception {
		this.engine = new Rete();
		Batch.batch("besouro/measure/EpisodeTDDConformance.clp", this.engine);
		Batch.batch("besouro/measure/TwoWayTDDHeuristicAlgorithm.clp",this.engine);
	}

	public Fact assertConformanceEpisode(int index, String category,String subtype) throws JessException {
		Fact f = new Fact("EpisodeTDDConformance", engine);
		f.setSlotValue("index", new Value(index, RU.INTEGER));
		f.setSlotValue("category", new Value(category, RU.STRING));
		f.setSlotValue("subtype", new Value(category, RU.STRING));
		Fact assertedFact = this.engine.assertFact(f);
		return assertedFact;
	}

	public void testPatternOne() throws Exception {

		engine.reset();

		assertConformanceEpisode(1, "test-first", "1");
		assertConformanceEpisode(2, "refactoring", "1");
		assertConformanceEpisode(3, "test-addition", "1");
		assertConformanceEpisode(4, "test-last", "1");
		assertConformanceEpisode(5, "refactoring", "1");
		assertConformanceEpisode(6, "test-addition", "1");
		assertConformanceEpisode(7, "refactoring", "1");
		assertConformanceEpisode(8, "test-first", "1");
		assertConformanceEpisode(9, "test-addition", "1");
		assertConformanceEpisode(10, "test-addition", "1");
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
		assertEquals("Test episode 4", "False", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(5, RU.INTEGER)));
		assertTrue("Result to episode 5", result.next());
		assertEquals("Test episode 5", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(6, RU.INTEGER)));
		assertTrue("Result to episode 6", result.next());
		assertEquals("Test episode 6", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(7, RU.INTEGER)));
		assertTrue("Result to episode 7", result.next());
		assertEquals("Test episode 7", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(8, RU.INTEGER)));
		assertTrue("Result to episode 8", result.next());
		assertEquals("Test episode 8", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(9, RU.INTEGER)));
		assertTrue("Result to episode 9", result.next());
		assertEquals("Test episode 9", "True", result.getString("isTDD"));

		result = engine.runQueryStar("episode-tdd-conformance-query-by-index",(new ValueVector()).add(new Value(10, RU.INTEGER)));
		assertTrue("Result to episode 10", result.next());
		assertEquals("Test episode 10", "True", result.getString("isTDD"));
	}
}
