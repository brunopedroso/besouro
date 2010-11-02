package besouro.integration.besouro;

import org.junit.Before;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;

public class RefactoringRecognition extends besouro.integration.RefactoringRecognition {
	@Before 
	@Override
	public void setup() throws Exception {
		BesouroEpisodeClassifierStream stream = new BesouroEpisodeClassifierStream();
		setup(stream);
	}	

}
