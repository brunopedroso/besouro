package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class ProductionRecognition extends IntegrationTestBaseClass {

	@Test 
	public void productionCategory1() throws Exception {
		
		addProductionCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	
	@Test 
	public void productionCategory1WithTestBreak() throws Exception {
		
		addProductionCategory1WithTestBreakEvents();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		addProductionCategory2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		addProductionCategory2_2_events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
//		this one was not considered by hingbings test
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2A", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}


	@Test 
	public void productionCategory3() throws Exception {
		
		addProductionCategory3Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
	}
	
}
