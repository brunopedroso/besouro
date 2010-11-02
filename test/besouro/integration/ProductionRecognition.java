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
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory1WithTestBreak() throws Exception {
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("sesionname", "TestFile.java", Result.FAILURE));
		
		// Edit on production code (corrects the error)    
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		// method increase but byte size decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",5));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		// method increase but byte statement decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",15));
		
		// Unit test failue
		
		// TODO [rule]   redundancy between prod/refact 
//		its a strange case without an edit after the test failure :-/
//		we only need this to luckly disambigue prod x refact
		
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest.java", Result.ERROR));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
//		this one was not considered by hingbings test
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2A", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}
	
	@Test 
	public void productionCategory3() throws Exception {
		
		// method increase, and size increase and LARGE byte increase
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		when(meter.getNumOfStatements()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",133));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
	}
	
}
