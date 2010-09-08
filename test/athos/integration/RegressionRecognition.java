package athos.integration;

import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import athos.listeners.mock.JUnitEventFactory;
import athos.listeners.mock.ResourceChangeEventFactory;

public class RegressionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void regressionCategory1() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void regressionCategory1_2() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(0).getSubtype());
		
		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(1).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}
	
	@Test 
	public void regressionCategory2() throws Exception {
		
		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));

		// TODO [rule] its a strange case without an test edit after the compilation problem :-/
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2", stream.getRecognizedEpisodes().get(0).getSubtype());
		// TODO [rule] this second one was not considered by hongbings test
		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(1).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}
	

	
}
