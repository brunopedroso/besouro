package besouro.listeners;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.WindowEventsFactory;
import besouro.model.Action;
import besouro.model.FileOpenedAction;
import besouro.stream.ActionOutputStream;


public class WindowEventsTest {

	
	private WindowListener listener;
	private JavaStatementMeter measurer;
	private ActionOutputStream stream;
	private ArrayList<Action> generatedActions;
	
	@Before
	public void setup() {
		generatedActions = new ArrayList<Action>();
		stream = new FakeActionStream(generatedActions);
		
		listener = new WindowListener(stream);
		measurer = mock(JavaStatementMeter.class);
		when(measurer.measureJavaFile(any(IFile.class))).thenReturn(measurer);
		listener.setMeasurer(measurer);
	}

	@Test
	public void shouldGenerateFileOpenEvent() {
		
		String filename = "aFile.java";
		listener.partOpened(WindowEventsFactory.createTestEditor(filename, 12345));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(filename, fileOpenedAction.getResource().getName());
		
		Assert.assertEquals(12345, fileOpenedAction.getFileSize());
		
		
	}

	
	@Test
	public void shouldGenerateFileOpenEventAtWindowOpen() {
		
		String filename = "aFile.java";
		
		listener.setWorkbench(WindowEventsFactory.getMockWorkbench(filename));
		listener.windowOpened(null);
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(filename, fileOpenedAction.getResource().getName());
		
		
	}
	
	//TODO [test] more unit tests for window events?
	
	
}
