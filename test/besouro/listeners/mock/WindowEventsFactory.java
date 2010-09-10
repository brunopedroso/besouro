package besouro.listeners.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import besouro.listeners.JavaStatementMeter;


public class WindowEventsFactory {

	public static IWorkbench getMockWorkbench(File file) {
		
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
	
	public static JavaStatementMeter createStubJavaMeter() {
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		when(meter.getNumOfMethods()).thenReturn(11);
		when(meter.getNumOfStatements()).thenReturn(22);
		when(meter.getNumOfTestAssertions()).thenReturn(33);
		when(meter.getNumOfTestMethods()).thenReturn(44);
		return meter;
	}

	public static ITextEditor createTestEditor(String filename, int fileLength) {
		
		File file = mock(File.class);
		when(file.getName()).thenReturn(filename);
		when(file.getPath()).thenReturn(filename);
		when(file.length()).thenReturn((long) fileLength);

		
		IPath ipath = mock(IPath.class);
		when(ipath.toFile()).thenReturn(file);
		
		IFile ifile = mock(IFile.class);
		when(ifile.getLocation()).thenReturn(ipath);
		IFileEditorInput fileInput = mock(IFileEditorInput.class);
		when(fileInput.getFile()).thenReturn(ifile);
		
		ITextEditor part = mock(ITextEditor.class);
		when(part.getEditorInput()).thenReturn(fileInput);
		
		return part;
	}
}
