package athos.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;

import athos.listeners.mock.JUnitEventFactory;
import athos.listeners.mock.ResourceChangeEventFactory;

public class TestLastRecognition extends IntegrationTestBaseClass {
	
	@Test 
	public void testLasCategory1() throws Exception {
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-last 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void testLastCategory2() throws Exception {
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		// TODO [rule] just to make it substantial :-/
		when(meter.getNumOfTestMethods()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-last 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	
}
