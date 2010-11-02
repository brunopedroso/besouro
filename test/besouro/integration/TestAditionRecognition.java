package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class TestAditionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void testAditionCategory1() throws Exception {
		
		addTestAddCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-addition", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	
	@Test 
	public void testAditionCategory2() throws Exception {
		
		addTestAddCategory2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-addition", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
	}

}
