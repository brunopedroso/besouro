package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class TestLastRecognition extends IntegrationTestBaseClass {
	
	@Test 
	public void testLastCategory1() throws Exception {
		
		addTestLast1Actions();
		
		Assert.assertEquals(1, stream.getTDDMeasure().getRecognizedEpisodes().size());
		Assert.assertEquals("test-last", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void testLastCategory2() throws Exception {
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest", Result.ERROR));

		// Edit on test
		when(meter.isTest()).thenReturn(true);
		// TODO [rule] just to make it substantial :-/
		when(meter.getNumOfTestMethods()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getTDDMeasure().getRecognizedEpisodes().size());
		Assert.assertEquals("test-last", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	
}
