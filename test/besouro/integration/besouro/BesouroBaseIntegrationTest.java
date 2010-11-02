package besouro.integration.besouro;

import org.junit.Before;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;
import besouro.integration.IntegrationTestBaseClass;

public class BesouroBaseIntegrationTest extends IntegrationTestBaseClass {

	@Before 
	@Override
	public void setup() throws Exception {
		BesouroEpisodeClassifierStream stream = new BesouroEpisodeClassifierStream();
		setup(stream);
	}
	
}
