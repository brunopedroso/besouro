package besouro.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import besouro.listeners.BesouroListenerSet;
import besouro.listeners.mock.WindowEventsFactory;
import besouro.measure.JavaStatementMeter;
import besouro.model.Action;
import besouro.stream.ActionOutputStream;

public class ListenerSetTest {
	
	private BesouroListenerSet listenerSet;

	@Before
	public void setup() {
		listenerSet = BesouroListenerSet.getSingleton();
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		when(meter.measureJavaFile(any(IFile.class))).thenReturn(meter);
		
		listenerSet.getWindowListener().setMeasurer(meter);
	}
	

	@Test
	public void shouldNotBreakeWhenOutputStreamIsNull() {
		listenerSet.getWindowListener().partOpened(WindowEventsFactory.createTestEditor("afile", 33));
	}

	@Test
	public void shouldGenerateActionsToTheDefinedStream() {
		
		final List<Action> list = new ArrayList<Action>();
		listenerSet.setOutputStream(new ActionOutputStream() {
			public void addAction(Action action) {
				list.add(action);
			}
		});
		
		listenerSet.getWindowListener().partOpened(WindowEventsFactory.createTestEditor("afile", 33));
		Assert.assertEquals("should notify the listener about the action", 1, list.size());
		
	}
	
}
