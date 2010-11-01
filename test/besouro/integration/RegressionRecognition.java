package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class RegressionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void regressionCategory1() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void regressionCategory1_2() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
		Assert.assertEquals("regression", stream.getEpisodes()[1].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[1].getSubtype());
		
	}
	
	@Test 
	public void regressionCategory2() throws Exception {
		
		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));
		
		// TODO [rule]  regression-2 make no sense... how would tests pass without editing?
		
		// in my experiments, the only case where it occurred was when the edits were not substantial
		// it happens in 4_BankOCR in commit 0f7c14f1, timestamp 1288200266317
		// but I disagred with it about the classification. I saw it as a refactoring because i've changed the indexes...  
		
		// its a strange case without an test edit after the compilation problem :-/
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
		// TODO [rule]  redundancy: 2 regressions.
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}
	

	
}
