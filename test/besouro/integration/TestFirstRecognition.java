package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.stream.FileStorageActionStream;


public class TestFirstRecognition extends IntegrationTestBaseClass {

	@Test 
	public void testFirstCategory1() throws Exception {
	
		addTestFirst1Actions();
		
		int size = stream.getTDDMeasure().getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getCategory());
		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getSubtype());
		
	  }


	@Test 
	public void testFirstCategory2() throws Exception {
	    
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		when(meter.getNumOfTestMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));

		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));

		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(0);
		when(meter.getNumOfMethods()).thenReturn(0);
		when(meter.getNumOfTestAssertions()).thenReturn(0);
		when(meter.getNumOfTestMethods()).thenReturn(0);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		int size = stream.getTDDMeasure().getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getCategory());
		Assert.assertEquals("2", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getSubtype());

	}
	
	@Test 
	public void testFirstCategory3() throws Exception {
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		when(meter.getNumOfTestMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfTestAssertions()).thenReturn(0);
		when(meter.getNumOfTestMethods()).thenReturn(0);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));
		
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "TestFile", Result.ERROR));

		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",37));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		int size = stream.getTDDMeasure().getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getCategory());
		Assert.assertEquals("3", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getSubtype());

	}
	
	@Test 
	public void testFirstCategory4() throws Exception {
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		when(meter.getNumOfTestMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
   
		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",37));

		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",39));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
    
		int size = stream.getTDDMeasure().getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("test-first", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getCategory());
		Assert.assertEquals("4", stream.getTDDMeasure().getRecognizedEpisodes().get(size-1).getSubtype());

	}

	@Test 
	public void testFirstRealCase() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));

		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "TestFile", Result.ERROR));

		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",33));

		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction("TestFile.java", "TestFile", "aTestMethod"));		
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",133));

		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));

		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		//TODO [rule] just to be substancial :-/
		when(meter.getNumOfMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",135));
		
		// Add prod method
		// in the original test, it was an ADD CLASS
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));

		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));

		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));

		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",38));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		int size = stream.getTDDMeasure().getRecognizedEpisodes().size();
		Assert.assertTrue(size==1);
		// TODO [rule] THIS SHOULD BE A TEST-FIRST! its that problem of recognizing various episodes... 
		Assert.assertEquals("test-last", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(0).getSubtype());
		
		//TODO [rule] we have 7 actions here!
		
	}

}
