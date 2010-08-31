package athos.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.model.Action;
import athos.model.FileOpenedAction;
import athos.stream.ActionOutputStream;

public class WindowEventsTest {

	
	@Test
	public void shouldGenerateFileOpenEvent() {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		WindowListener listener = new WindowListener(stream);
		listener.setJavaMeter(createStubJavaMeter());
		
		File file = mock(File.class);
		when(file.getName()).thenReturn("aFile.java");
		
		when(file.length()).thenReturn((long) 12345);
		
		listener.partOpened(createTestEditor(file));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(file, fileOpenedAction.getFile());
		
		Assert.assertEquals(12345, fileOpenedAction.getFileSize());
		
		Assert.assertEquals(11, fileOpenedAction.getMethodsCount());
		Assert.assertEquals(22, fileOpenedAction.getStatementsCount());
		Assert.assertEquals(33, fileOpenedAction.getTestAssertionsCount());
		Assert.assertEquals(44, fileOpenedAction.getTestMethodsCount());
		
	}
	
	@Test
	public void shouldGenerateFileOpenEventAtWindowOpen() {
		
		File file = mock(File.class);
		when(file.getName()).thenReturn("aFile.java");
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		WindowListener listener = new WindowListener(stream);
		
		listener.setJavaMeter(createStubJavaMeter());
		listener.setWorkbench(getMockWorkbench(file));
		
		listener.windowOpened(null);
		
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(file, fileOpenedAction.getFile());
		Assert.assertEquals(11, fileOpenedAction.getMethodsCount());
		Assert.assertEquals(22, fileOpenedAction.getStatementsCount());
		Assert.assertEquals(33, fileOpenedAction.getTestAssertionsCount());
		Assert.assertEquals(44, fileOpenedAction.getTestMethodsCount());
		
	}
	
	//TODO [data]  is there more window events to test?
	
	
	// TODO   move to a factory

	private IWorkbench getMockWorkbench(File file) {
		
		IDocument doc = mock(IDocument.class);
		
		IDocumentProvider docProvider = mock(IDocumentProvider.class);
		when(docProvider.getDocument(any())).thenReturn(doc);
		
		IPath ipath = mock(IPath.class);
		when(ipath.toFile()).thenReturn(file);
		
		IFile inputFile = mock(IFile.class);
		
		when(inputFile.getLocation()).thenReturn(ipath);
		
		IFileEditorInput editorInput = mock(IFileEditorInput.class);
		when(editorInput.getFile()).thenReturn(inputFile);
		
		ITextEditor editor = mock(ITextEditor.class);
		when(editor.getEditorInput()).thenReturn(editorInput);
		when(editor.getDocumentProvider()).thenReturn(docProvider);
		
		IWorkbenchPage page = mock(IWorkbenchPage.class);
		when(page.getActiveEditor()).thenReturn(editor);
		
		IWorkbenchWindow window = mock(IWorkbenchWindow.class);
		when(window.getActivePage()).thenReturn(page);
		
		IWorkbench workbench = mock(IWorkbench.class);
		when(workbench.getWorkbenchWindows()).thenReturn(new IWorkbenchWindow[]{window});
		return workbench;
	}
	
	private JavaStatementMeter createStubJavaMeter() {
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		when(meter.getNumOfMethods()).thenReturn(11);
		when(meter.getNumOfStatements()).thenReturn(22);
		when(meter.getNumOfTestAssertions()).thenReturn(33);
		when(meter.getNumOfTestMethods()).thenReturn(44);
		return meter;
	}

	private ITextEditor createTestEditor(File file) {
		IPath ipath = mock(IPath.class);
		when(ipath.toFile()).thenReturn(file);
		
		IFile ifile = mock(IFile.class);
		when(ifile.getLocation()).thenReturn(ipath);
		
		ITextEditor part = createTestEditor(ifile);
		return part;
	}

	private ITextEditor createTestEditor(IFile ifile) {
		

		IFileEditorInput fileInput = mock(IFileEditorInput.class);
		when(fileInput.getFile()).thenReturn(ifile);
		
		ITextEditor part = mock(ITextEditor.class);
		when(part.getEditorInput()).thenReturn(fileInput);
		
		return part;
		
	}
	
	
}
