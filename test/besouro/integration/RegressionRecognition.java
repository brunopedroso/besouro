package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class RegressionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void regressionCategory1() throws Exception {
		
		addRegressionCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	@Test 
	public void regressionCategory1_2() throws Exception {
		
		addRegressionCategory1_2_events();
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
		Assert.assertEquals("regression", stream.getEpisodes()[1].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[1].getSubtype());
		
	}

	
	@Test 
	public void regressionCategory2() throws Exception {
		
		addRegressionCategory2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
	}
	
	
}
