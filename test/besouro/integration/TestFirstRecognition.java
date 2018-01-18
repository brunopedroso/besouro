package besouro.integration;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
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

		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
}
