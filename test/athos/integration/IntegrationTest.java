package athos.integration;

import static org.mockito.Mockito.*;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.jdt.core.ElementChangedEvent;
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
import athos.model.CompilationAction;
import athos.model.EditAction;
import athos.model.UnitTestAction;
import athos.model.refactor.RefactorOperator;
import athos.model.refactor.RefactorSubjectType;
import athos.model.refactor.UnaryRefactorAction;
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
	public void testTDDEpisodeCategory1() throws Exception {
	
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
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-first 1", stream.getRecognizedEpisodes().get(0));
		
	  }

	@Test 
	public void testTDDEpisodeCategory2() throws Exception {
	    
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
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-first 2", stream.getRecognizedEpisodes().get(0));
	}
	

}
