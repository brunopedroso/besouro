package athos.listeners;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Test;
import org.mockito.Mockito;

import athos.listeners.mock.FakeActionStream;
import athos.model.Action;
import athos.model.FileOpenedAction;
import athos.stream.ActionOutputStream;

public class WindowEventsTest {

	
	@Test
	public void shouldGenerateFileOpenEvent() {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		
		File file = new File("aFile.java");
		IPath ipath = mock(IPath.class);
		when(ipath.toFile()).thenReturn(file);
		
		IFile ifile = mock(IFile.class);
		when(ifile.getFullPath()).thenReturn(ipath);
		
		ITextEditor part = createTestEditor(ifile);
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		when(meter.getNumOfMethods()).thenReturn(11);
		when(meter.getNumOfStatements()).thenReturn(22);
		when(meter.getNumOfTestAssertions()).thenReturn(33);
		when(meter.getNumOfTestMethods()).thenReturn(44);
		
		WindowListener listener = new WindowListener(stream);
		listener.setJavaMeter(meter);
		
		listener.partOpened(part);
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(file, fileOpenedAction.getFile());
		Assert.assertEquals(11, fileOpenedAction.getNumOfMethods());
		Assert.assertEquals(22, fileOpenedAction.getNumOfStatements());
		Assert.assertEquals(33, fileOpenedAction.getNumOfTestAssertions());
		Assert.assertEquals(44, fileOpenedAction.getNumOfTestMethods());
		
		verify(meter).measureJavaFile(ifile);
		
	}

	private ITextEditor createTestEditor(IFile ifile) {
		

		IFileEditorInput fileInput = mock(IFileEditorInput.class);
		when(fileInput.getFile()).thenReturn(ifile);
		
		ITextEditor part = mock(ITextEditor.class);
		when(part.getEditorInput()).thenReturn(fileInput);
		
		return part;
		
	}
	
	
	//TODO  is there more win events to tests
	
}
