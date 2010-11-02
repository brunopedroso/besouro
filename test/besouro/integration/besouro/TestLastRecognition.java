package besouro.integration.besouro;

import org.junit.Before;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;


public class TestLastRecognition extends besouro.integration.TestLastRecognition{

	@Before 
	@Override
	public void setup() throws Exception {
		BesouroEpisodeClassifierStream stream = new BesouroEpisodeClassifierStream();
		setup(stream);
	}	

}
