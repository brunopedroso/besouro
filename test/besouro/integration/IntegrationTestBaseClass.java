package besouro.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

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
import besouro.stream.EpisodeClassifierStream;


public class IntegrationTestBaseClass {

	protected EpisodeClassifierStream stream;
	protected JavaStructureChangeListener javaListener;
	protected ResourceChangeListener resourceListener;
	protected JUnitListener junitListener;
	protected WindowListener winListener;
	protected JavaStatementMeter meter;

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

}
