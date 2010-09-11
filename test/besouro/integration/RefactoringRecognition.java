package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class RefactoringRecognition extends IntegrationTestBaseClass {

	@Test 
	public void refactoringCategory1A() throws Exception {
		
		addRefactoring1A_Actions();
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1A", stream.getRecognizedEpisodes().get(0).getSubtype());
	}

	
	@Test 
	public void refactoringCategory1A_2() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
				
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest", Result.ERROR));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(2);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",37));
				
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		// two refactorings - one on each edit (because they precede test-pass)
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1A", stream.getRecognizedEpisodes().get(0).getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1A", stream.getRecognizedEpisodes().get(1).getSubtype());
	}
	
	@Test 
	public void refactoringCategory1B() throws Exception {
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("1B", stream.getRecognizedEpisodes().get(0).getSubtype());
		
		//TODO [rule] why are it recognizing these 3 extra episodes?
		//			  does it influence the metric?
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getRecognizedEpisodes().get(2).getSubtype());
//		
//		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(3).getCategory());
//		Assert.assertEquals("1", stream.getRecognizedEpisodes().get(3).getSubtype());
	}
	
	
	@Test 
	public void refactoringCategory2A() throws Exception {
		
		 // Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
	    
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));

	    // Edit on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));

	    
	    // Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2A", stream.getRecognizedEpisodes().get(0).getSubtype());
	    
	}
	
	@Test 
	public void refactoringCategory2B() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		// rename prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRenameMethodEvent("ProductionFile.java", "ProductionFile", "aMethod", "anotherMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("2B", stream.getRecognizedEpisodes().get(0).getSubtype());

		//TODO [rule] why are it recognizing these 2 extra episodes?
		//			  does it influence the metric?
//		Assert.assertEquals("regression", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2", stream.getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getRecognizedEpisodes().get(2).getSubtype());
		
	}

	@Test 
	public void refactoringCategory3_1() throws Exception {
		
		 // Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		addRefactoring1A_Actions();
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("3", stream.getRecognizedEpisodes().get(0).getSubtype());
		
	}
	
	@Test 
	public void refactoringCategory3_2() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		addRefactoring1A_Actions();
		
		//TODO [rule] we have 2 refactorings here... hongbing considered just one...
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(0).getCategory());
		Assert.assertEquals("3", stream.getRecognizedEpisodes().get(0).getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}
}
