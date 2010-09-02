package athos.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import athos.listeners.JUnitListener;
import athos.listeners.JavaStatementMeter;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.WindowListener;
import athos.listeners.mock.JUnitEventFactory;
import athos.listeners.mock.JavaStructureChangeEventFactory;
import athos.listeners.mock.ResourceChangeEventFactory;
import athos.listeners.mock.WindowEventsFactory;
import athos.stream.EpisodeClassifierStream;

//TODO   break and rename integraton tests

public class IntegrationTest {

	private EpisodeClassifierStream stream;
	private JavaStructureChangeListener javaListener;
	private ResourceChangeListener resourceListener;
	private JUnitListener junitListener;
	private WindowListener winListener;
	private JavaStatementMeter meter;

	@Before
	public void setup() throws Exception {
		stream = new EpisodeClassifierStream();
		
		javaListener = new JavaStructureChangeListener(stream);
		resourceListener = new ResourceChangeListener(stream);
		junitListener = new JUnitListener(stream);
		winListener = new WindowListener(stream);
		
		meter = mock(JavaStatementMeter.class);
		resourceListener.setTestCounter(meter);
		winListener.setJavaMeter(meter);
		
		// Open file (calculates the first file metrics)
		when(meter.getNumOfMethods()).thenReturn(3);
		winListener.partOpened(WindowEventsFactory.createTestEditor("TestFile.java", 10));
		winListener.partOpened(WindowEventsFactory.createTestEditor("ProductionFile.java", 10));

	}

	@Test 
	public void testFirstCategory1() throws Exception {
	
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction("TestFile.java", "TestFile", "aTestMethod"));

		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		when(meter.getNumOfTestMethods()).thenReturn(10);
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
		
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Edit on prodction code
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java", 37));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		int size = stream.getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("[episode] test-first 1", stream.getRecognizedEpisodes().get(size-1));
		
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
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		int size = stream.getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("[episode] test-first 2", stream.getRecognizedEpisodes().get(size-1));

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
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

		// Work on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",37));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		int size = stream.getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("[episode] test-first 3", stream.getRecognizedEpisodes().get(size-1));

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
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
    
		int size = stream.getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("[episode] test-first 4", stream.getRecognizedEpisodes().get(size-1));

		
	}
	
	@Test 
	public void refactoringCategory1A() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
//		//TODO [rule] its a little strange... I dont count test methods change in test-edits, but i consider it to be substancial

		
//		// Edit on test
//		when(meter.hasTest()).thenReturn(true);
//		when(meter.getNumOfTestMethods()).thenReturn(2);
//		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",35));
//		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		// Edit on test
//		when(meter.hasTest()).thenReturn(true);
//		when(meter.getNumOfTestAssertions()).thenReturn(3);
//		when(meter.getNumOfTestMethods()).thenReturn(5);
//		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",37));

		//TODO [rule] hongbing's test is kinda strange...
		//	more actions that was needed
		//	a overwrite in the index 2 action
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 1A", stream.getRecognizedEpisodes().get(0));
	}
	
	@Test 
	public void refactoringCategory1A_2() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
				
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(2);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",37));
				
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		// two refactorings - one on each edit (because they precede test-pass)
		Assert.assertEquals("[episode] refactoring 1A", stream.getRecognizedEpisodes().get(0));
		Assert.assertEquals("[episode] refactoring 1A", stream.getRecognizedEpisodes().get(1));
	}
	
	@Test 
	public void refactoringCategory1B() throws Exception {
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(4, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 1B", stream.getRecognizedEpisodes().get(0));
		
		//TODO [rule] why are it recognizing these 3 extra episodes?
		//			  does it influence the metric?
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(1));
		Assert.assertEquals("[episode] refactoring 2B", stream.getRecognizedEpisodes().get(2));
		Assert.assertEquals("[episode] regression 1", stream.getRecognizedEpisodes().get(3));
	}
	
	
	@Test 
	public void refactoringCategory2A() throws Exception {
		
		 // Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
	    
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

	    // Edit on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));

	    
	    // Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 2A", stream.getRecognizedEpisodes().get(0));
	    
	}
	
	@Test 
	public void refactoringCategory2B() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// rename prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRenameMethodEvent("ProductionFile.java", "ProductionFile", "aMethod", "anotherMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(3, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 2B", stream.getRecognizedEpisodes().get(0));

		//TODO [rule] why are it recognizing these 2 extra episodes?
		//			  does it influence the metric?
		Assert.assertEquals("[episode] regression 2", stream.getRecognizedEpisodes().get(1));
		Assert.assertEquals("[episode] refactoring 2B", stream.getRecognizedEpisodes().get(2));
		
	}

	@Test 
	public void refactoringCategory3_1() throws Exception {
		
		 // Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void refactoringCategory3_2() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		//TODO [rule] we have 2 refactorings here... hongbing considered just one...
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(0));
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(1));
		
	}
	
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
	
	@Test 
	public void testAditionCategory1() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-addition 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void testAditionCategory2() throws Exception {
		
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
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-addition 2", stream.getRecognizedEpisodes().get(0));
		// TODO [rule] this second one was not considered by hongbings test
		Assert.assertEquals("[episode] test-addition 1", stream.getRecognizedEpisodes().get(1));
		
	}

	@Test 
	public void regressionCategory1() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile1.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] regression 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void regressionCategory1_2() throws Exception {
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile1.java"));
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile2.java"));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] regression 1", stream.getRecognizedEpisodes().get(0));
		Assert.assertEquals("[episode] regression 1", stream.getRecognizedEpisodes().get(1));
		
	}
	
	@Test 
	public void regressionCategory2() throws Exception {
		
		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));

		// TODO [rule] its a strange case without an test edit after the compilation problem :-/
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] regression 2", stream.getRecognizedEpisodes().get(0));
		// TODO [rule] this second one was not considered by hongbings test
		Assert.assertEquals("[episode] regression 1", stream.getRecognizedEpisodes().get(1));
		
	}
	
	@Test 
	public void productionCategory1() throws Exception {
		
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		// method increase but byte size decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",5));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 2", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		// method increase but byte statement decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",15));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 2", stream.getRecognizedEpisodes().get(0));
		//TODO [rule] this one was not considered by hingbings test
		Assert.assertEquals("[episode] refactoring 2A", stream.getRecognizedEpisodes().get(1));
		
	}
	
	@Test 
	public void productionCategory3() throws Exception {
		
		// method increase, and size increase and LARGE byte increase
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		when(meter.getNumOfStatements()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",133));

		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 3", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void testFirstRealCase() throws Exception {
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));

		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

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
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		int size = stream.getRecognizedEpisodes().size();
		Assert.assertTrue(size>0);
		Assert.assertEquals("[episode] test-first 1", stream.getRecognizedEpisodes().get(size-1));
		
		//TODO [rule] we have 7 actions here!
		
	}
	
}
