package besouro.classification.besouro;


import org.junit.Before;

import besouro.classification.zorro.ZorroEpisodeClassifierTest;


/**
 * Applies the same tests from zorro, but in the BesouroEngine
 * @author Bruno Pedroso
 */
public class BesouroEpisodeClassifierTest extends ZorroEpisodeClassifierTest {

  @Before
  public void setUp() throws Exception {

	super.setUp();
	  
	this.zorro = new BesouroEpisodeClassification();
    this.engine = zorro.getEngine();

    engine.reset();
  }
  

}
