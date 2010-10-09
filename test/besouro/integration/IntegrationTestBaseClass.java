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

import besouro.listeners.JUnitListener;
import besouro.listeners.JavaStatementMeter;
import besouro.listeners.JavaStructureChangeListener;
import besouro.listeners.ResourceChangeListener;
import besouro.listeners.WindowListener;
import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.listeners.mock.WindowEventsFactory;
import besouro.persistence.ActionFileStorage;
import besouro.stream.EpisodeClassifierStream;


public class IntegrationTestBaseClass {

	protected EpisodeClassifierStream stream;
	protected JavaStructureChangeListener javaListener;
	protected ResourceChangeListener resourceListener;
	protected JUnitListener junitListener;
	protected WindowListener winListener;
	protected JavaStatementMeter meter;
	protected JavaStatementMeter measurer;
	
	@Before
	public void setup() throws Exception {
		
		EpisodeClassifierStream stream = new EpisodeClassifierStream();
		setup(stream);
	}
		
	public void setup(EpisodeClassifierStream stream) throws Exception {
		
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
	
	public void setStreamUnderTest(EpisodeClassifierStream stream) {
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
	
	
	protected void addRefactoring1A_Actions() throws Exception {
		// Edit on test
		when(meter.isTest()).thenReturn(true);
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
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
	}

}
