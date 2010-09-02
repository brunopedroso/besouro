package athos.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

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
		winListener.partOpened(WindowEventsFactory.createTestEditor(new File("TestFile.java")));
		winListener.partOpened(WindowEventsFactory.createTestEditor(new File("ProductionFile.java")));

	}

	@Test 
	public void testFirstCategory1() throws Exception {
	
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
		
		//TODO we have 2 refactorings here... hongbing considered just one...
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(0));
		Assert.assertEquals("[episode] refactoring 3", stream.getRecognizedEpisodes().get(1));
		
	}
	
	@Test 
	public void testLast1() throws Exception {
		
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
	public void testLast2() throws Exception {
		
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
	public void testAdition1() throws Exception {
		
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
	public void testAdition2() throws Exception {
		
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
		// TODO this second one was not considered by hongbings test
		Assert.assertEquals("[episode] test-addition 1", stream.getRecognizedEpisodes().get(1));
		
	}


	
//			(deffacts Regression-1-episode
//			   (UnitTestAction       (index 1) (file TestRate.java))        
//			   (UnitTestAction       (index 2) (file TestTriangle.java))        
//			(printout t (test-classifier "regression" "1") crlf crlf)

	
//			(deffacts Regression-2-episode
//			   (CompilationAction    (index 1) (file TestTriangle.java) (message "Class ZorroStream not found"))        
//			   (UnitTestAction       (index 2) (file TestTriangle.java))
//			)
//			(printout t (test-classifier "regression" "2") crlf crlf)

	
//			;; Production type 1 without method increase
//			(deffacts production-1-episode
//			   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 0) (statementChange 14) (byteChange 210) (duration 200))    
//			   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
//			   (UnitTestAction (index 3) (file TestTriangle.java))        
//			(printout t (test-classifier "production" "1") crlf crlf)

	
//			;; Production type 2 with method increase but byte size decrease
//			(deffacts production-2-episode
//			   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 2) (byteChange -10) (duration 200))    
//			   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
//			   (UnitTestAction (index 3) (file TestTriangle.java))        
//			)
//			(printout t (test-classifier "production" "2") crlf crlf)

	
//			;; Production type 2 with method increase but statement decrease
//			(deffacts production-2-episodeB
//			   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 2) (byteChange 8) (duration 200))    
//			   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
//			   (UnitTestAction (index 3) (file TestTriangle.java))        
//			)
//			(printout t (test-classifier "production" "2") crlf crlf)

	
//			;; Production type 3 with method increase, and size increase and byte increase
//			(deffacts production-3-episode
//			   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 5) (byteChange 125) (duration 200))    
//			   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
//			   (UnitTestAction (index 3) (file TestTriangle.java))        
//			(printout t (test-classifier "production" "3") crlf crlf)
//


	
//			(deffacts test-first-1-episode-real
//			   (BufferTransAction (index 1) (leavingFile Frame.java))
//			   (UnitTestEditAction (index 2) (file TestFrame.java) (duration 0)  
//			                       (testChange 0) (assertionChange 1)) 
//			   (UnitTestAction  (index 3) (file TestFrame.java) (errmsg "Failed to run test"))              
//			   (BufferTransAction (index 4) (leavingFile TestFrame.java))
//			   (ProductionEditAction (index 5) (file Frame.java) (methodChange 0) (statementChange 0) (byteChange 33) (duration 0))    
//			   (BufferTransAction (index 6) (leavingFile Frame.java))
//			   (UnaryRefactorAction (index 7) (file TestFrame.java) 
//			                         (operation "ADD") (type "METHOD") (data "void testGame()"))    
//			   (UnitTestEditAction (index 8) (file TestFrame.java) (duration 34)  
//			                       (testChange 1) (assertionChange 0) (byteChange 131)) 
//			   (CompilationAction  (index 9) (file TestFrame.java) ;;
//			                        (message "BowlingGame cannot be resolved to a type"))
//			   (UnitTestEditAction (index 10) (file TestFrame.java) (duration 0)  
//			                       (testChange 0) (assertionChange 0) (byteChange 4)) 
//			   (UnaryRefactorAction (index 11) (file BowlingGame.java) 
//			                         (operation "ADD") (type "CLASS") (data "BowlingGame.java"))    
//			   (BufferTransAction (index 12) (leavingFile TestFrame.java)) 
//			   (ProductionEditAction (index 13) (file BowlingGame.java) (methodChange 0) (statementChange 0) (byteChange 167) (duration 9))    
//			   (UnaryRefactorAction (index 14) (file BowlingGame.java) 
//			                         (operation "ADD") (type "METHOD") (data "BowlingGame()"))    
//			   (ProductionEditAction (index 15) (file BowlingGame.java) (methodChange 1) (statementChange 0) (byteChange 82) (duration 23))    
//			   (BufferTransAction (index 16) (leavingFile BowlingGame.java)) 
//			   (UnitTestAction (index 17) (file TestFrame.java)) 
//			(printout t (test-classifier "test-first" "1") crlf crlf)
}
