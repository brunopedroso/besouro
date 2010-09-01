package athos.integration;

import static org.mockito.Mockito.mock;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.junit.Test;

import athos.listeners.JUnitListener;
import athos.listeners.JavaStatementMeter;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.mock.JUnitEventFactory;
import athos.listeners.mock.JavaStructureChangeEventFactory;
import athos.listeners.mock.ResourceChangeEventFactory;
import athos.stream.EpisodeClassifierStream;

public class IntegrationTest {

	@Test 
	public void testTDDEpisodeCategory1() throws Exception {
	
		
		EpisodeClassifierStream stream = new EpisodeClassifierStream();
		
		JavaStructureChangeListener javaListener = new JavaStructureChangeListener(stream);
		ResourceChangeListener resourceListener = new ResourceChangeListener(stream);
		JUnitListener junitListener = new JUnitListener(stream);
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		resourceListener.setTestCounter(meter);
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createAddMethodAction());

		// Edit on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createTestEditAction());
		
		// Compile error on test
		resourceListener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent("afile", "error message"));
		
		// Work on production code
		resourceListener.resourceChanged(ResourceChangeEventFactory.createProductionEditAction());
		
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession());
		

		// Edit on prodction code
		resourceListener.resourceChanged(ResourceChangeEventFactory.createProductionEditAction());

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession());

		  
	  }
	
}
