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
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void testLastCategory2() throws Exception {
		
		addTestLast2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

}
