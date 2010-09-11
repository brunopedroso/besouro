package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class TestAditionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void testAditionCategory1() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("test-addition", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void testAditionCategory2() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "TestFile", Result.ERROR));

		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		// TODO [rule] just to make it substantial :-/
		when(meter.getNumOfTestMethods()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("test-addition", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2", stream.getRecognizedEpisodes().get(0).getSubtype());
		
		// TODO [rule] this second one was not considered by hongbings test
//		Assert.assertEquals("test-addition", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}

	
	
}
