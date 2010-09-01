package athos.integration;

import static org.mockito.Mockito.*;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.jdt.core.ElementChangedEvent;
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

	@Test 
	public void testTDDEpisodeCategory1() throws Exception {
	
		
		EpisodeClassifierStream stream = new EpisodeClassifierStream();
		
		JavaStructureChangeListener javaListener = new JavaStructureChangeListener(stream);
		ResourceChangeListener resourceListener = new ResourceChangeListener(stream);
		JUnitListener junitListener = new JUnitListener(stream);
		WindowListener winListener = new WindowListener(stream);
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		resourceListener.setTestCounter(meter);
		winListener.setJavaMeter(meter);

		
		// Open file (calculates the first file metrics)
		winListener.partOpened(WindowEventsFactory.createTestEditor(new File("TestFile.java")));
		winListener.partOpened(WindowEventsFactory.createTestEditor(new File("ProductionFile.java")));
		
		
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
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(3);
		when(meter.getNumOfTestAssertions()).thenReturn(0);
		when(meter.getNumOfTestMethods()).thenReturn(0);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",33));
		
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession());
		

		// Edit on prodction code
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java", 33));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession());
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] test-first 1", stream.getRecognizedEpisodes().get(0));
		
	  }
	
}
