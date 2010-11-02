package besouro.integration.besouro;

import org.junit.Before;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;


public class TestFirstRecognition extends besouro.integration.TestFirstRecognition{
	@Before 
	@Override
	public void setup() throws Exception {
		BesouroEpisodeClassifierStream stream = new BesouroEpisodeClassifierStream();
		setup(stream);
	}	

}
