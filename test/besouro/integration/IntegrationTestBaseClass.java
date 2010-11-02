package besouro.integration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.classification.zorro.ZorroEpisodeClassifierStream;
import besouro.listeners.JUnitListener;
import besouro.listeners.JavaStructureChangeListener;
import besouro.listeners.ResourceChangeListener;
import besouro.listeners.WindowListener;
import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.listeners.mock.WindowEventsFactory;
import besouro.measure.JavaStatementMeter;
import besouro.persistence.ActionFileStorage;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodesRecognizerActionStream;


public class IntegrationTestBaseClass {

	protected EpisodesRecognizerActionStream stream;
	protected JavaStructureChangeListener javaListener;
	protected ResourceChangeListener resourceListener;
	protected JUnitListener junitListener;
	protected WindowListener winListener;
	protected JavaStatementMeter meter;
	protected JavaStatementMeter measurer;
	
	@Before
	public void setup() throws Exception {
		
		ZorroEpisodeClassifierStream stream = new ZorroEpisodeClassifierStream();
		setup(stream);
	}
		
	public void setup(EpisodesRecognizerActionStream stream) throws Exception {
		
		this.stream = stream;
		
		javaListener = new JavaStructureChangeListener(stream);
		resourceListener = new ResourceChangeListener(stream);
		junitListener = new JUnitListener(stream);
		winListener = new WindowListener(stream);
		
		// its strange yet, i know
		meter = mock(JavaStatementMeter.class);
		measurer = mock(JavaStatementMeter.class);
		when(measurer.measureJavaFile(any(IFile.class))).thenReturn(meter);
		
		resourceListener.setMeasurer(measurer);
		winListener.setMeasurer(measurer);
		
		// Open file (calculates the first file metrics)
		when(meter.getNumOfMethods()).thenReturn(3);
		winListener.partOpened(WindowEventsFactory.createTestEditor("TestFile.java", 10));
		winListener.partOpened(WindowEventsFactory.createTestEditor("ProductionFile.java", 10));

	}
	
	public void setStreamUnderTest(EpisodesRecognizerActionStream stream) {
		this.stream = stream;
	}

	// SHARED FACTORIES
	


	
	protected void addTestFirst1Actions() throws Exception {

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
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.ERROR));
		
		// Edit on prodction code
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java", 37));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}
	
	protected void addTestFirt2Events() throws CoreException, Exception {
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
	}

	protected void addTestFirst3Events() throws CoreException, Exception {
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
	}
	
	protected void addTestFirst4() throws CoreException, Exception {
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
	}

	protected void addTestFirstRealCase() throws CoreException, Exception {
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
		//TODO [rule]   review substancial concept
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
	}
	
	protected void addTestLast1Actions() throws Exception {
		// Edit on production code    
		when(meter.isTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}
	
	protected void addTestLast2Events() throws CoreException, Exception {
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest", Result.ERROR));

		// Edit on test
		when(meter.isTest()).thenReturn(true);
		//TODO [rule]   review substancial concept
		when(meter.getNumOfTestMethods()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}
	
	protected void addRefactoring1A_Actions() throws Exception {
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}
	
	protected void addRefactoringCategory1a_2_events() throws Exception {
		// Edit on test
		when(meter.isTest()).thenReturn(true);
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
	}
	
	
	protected void addRefactoringCategory1B_events() throws Exception {
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}

	protected void addRefactoringCategory2A_events() throws Exception {
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
	}
	
	protected void addRefactoringCategory2B_events() throws Exception {
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		// rename prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRenameMethodEvent("ProductionFile.java", "ProductionFile", "aMethod", "anotherMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}

	protected void addRefactoringCategory3_1_events() throws CoreException, Exception {
		// Edit on production code    
		when(meter.isTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		addRefactoring1A_Actions();
	}

	protected void addRefactoringCategory3_2_events() throws CoreException,
			Exception {
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		addRefactoringCategory3_1_events();
	}
	
	
	protected void addProductionCategory1Events() throws CoreException, Exception {
		// Edit on production code  
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}
	

	protected void addProductionCategory1WithTestBreakEvents() throws Exception {
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("sesionname", "TestFile.java", Result.FAILURE));
		
		// Edit on production code (corrects the error)    
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}

	protected void addProductionCategory2Events() throws CoreException, Exception {
		
		// method increase but byte size decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",5));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}
	
	protected void addProductionCategory2_2_events() throws Exception {
		// method increase but byte statement decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",15));
		
		// Unit test failue
		
		// TODO [rule]   redundancy between prod/refact 
//		its a strange case without an edit after the test failure :-/
//		we only need this to luckly disambigue prod x refact
		
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest.java", Result.ERROR));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}
	
	
	protected void addProductionCategory3Events() throws Exception {
		// method increase, and size increase and LARGE byte increase
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		when(meter.getNumOfStatements()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",133));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}
	
	protected void addRegressionCategory1Events() throws Exception {
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile.java", Result.OK));
	}
	
	protected void addRegressionCategory1_2_events() throws Exception {
		addRegressionCategory1Events();
		addRegressionCategory1Events();
	}

	protected void addRegressionCategory2Events() throws CoreException, Exception {
		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("TestFile.java", "error message"));
		
		// TODO [rule]  regression-2 make no sense... how would tests pass without editing?
		
		// in my experiments, the only case where it occurred was when the edits were not substantial
		// it happens in 4_BankOCR in commit 0f7c14f1, timestamp 1288200266317
		// but I disagred with it about the classification. I saw it as a refactoring because i've changed the indexes...  
		
		addRegressionCategory1Events();
	}

	
	protected void addTestAddCategory1Events() throws CoreException, Exception {
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}
	
	protected void addTestAddCategory2Events() throws CoreException, Exception {
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestAssertions()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "TestFile", Result.ERROR));

		// Edit on test
		when(meter.isTest()).thenReturn(true);
		//TODO [rule]   review substancial concept
		when(meter.getNumOfTestMethods()).thenReturn(3);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}

}
