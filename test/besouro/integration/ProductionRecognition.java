package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class ProductionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void productionCategory1() throws Exception {
		
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("production", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		// method increase but byte size decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",5));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("production", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		// method increase but byte statement decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",15));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest.java", Result.ERROR));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("production", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2", stream.getRecognizedEpisodes().get(0).getSubtype());
		
		//TODO [rule] this one was not considered by hingbings test
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
		Assert.assertEquals("2A", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}
	
	@Test 
	public void productionCategory3() throws Exception {
		
		// method increase, and size increase and LARGE byte increase
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		when(meter.getNumOfStatements()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",133));

		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("production", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("3", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
}
