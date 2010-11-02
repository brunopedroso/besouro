package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.persistence.ActionFileStorage;


public class TestFirstRecognition extends IntegrationTestBaseClass {

	@Test 
	public void testFirstCategory1() throws Exception {
	
		addTestFirst1Actions();
		
		int size = stream.getEpisodes().length;
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getEpisodes()[size-1].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[size-1].getSubtype());
		
	  }


	@Test 
	public void testFirstCategory2() throws Exception {
	    
		addTestFirt2Events();
		
		int size = stream.getEpisodes().length;
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getEpisodes()[size-1].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[size-1].getSubtype());

	}


	
	@Test 
	public void testFirstCategory3() throws Exception {
		
		addTestFirst3Events();
		
		int size = stream.getEpisodes().length;
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getEpisodes()[size-1].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[size-1].getSubtype());

	}
	
	@Test 
	public void testFirstCategory4() throws Exception {
		
		addTestFirst4();
    
		int size = stream.getEpisodes().length;
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getEpisodes()[size-1].getCategory());
		Assert.assertEquals("4", stream.getEpisodes()[size-1].getSubtype());

	}



	@Test 
	public void testFirstRealCase() throws Exception {
		
		addTestFirstRealCase();
		
		int size = stream.getEpisodes().length;
		Assert.assertTrue(size==1);
		// TODO [rule]  redundancy! Test-first and test-last!! 
		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
		//TODO [rule]  redundancy: 2 test-lasts
		
	}
	
}
