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

	public static IWorkbench getMockWorkbench(String file) {
		
		IWorkbench workbench = mock(IWorkbench.class);
		IWorkbenchWindow window = mock(IWorkbenchWindow.class);
		IWorkbenchPage page = mock(IWorkbenchPage.class);
		
		ITextEditor editor = mock(ITextEditor.class);
		
		IDocumentProvider docProvider = mock(IDocumentProvider.class);
		IDocument doc = mock(IDocument.class);
		
		IPath ipath = mock(IPath.class);
		File afile = mock(File.class);
		IFile inputFile = mock(IFile.class);
		
		IFileEditorInput editorInput = mock(IFileEditorInput.class);
		
		when(workbench.getWorkbenchWindows()).thenReturn(new IWorkbenchWindow[]{window});
		when(window.getActivePage()).thenReturn(page);
		when(page.getActiveEditor()).thenReturn(editor);
		
		when(editor.getEditorInput()).thenReturn(editorInput);
		when(editor.getDocumentProvider()).thenReturn(docProvider);
		
		when(editorInput.getFile()).thenReturn(inputFile);
		when(docProvider.getDocument(any())).thenReturn(doc);
		
		when(inputFile.getLocation()).thenReturn(ipath);
		when(inputFile.getName()).thenReturn(file);
		
		when(ipath.toFile()).thenReturn(afile);
		when(afile.length()).thenReturn(33l);
		
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
		
		ITextEditor part = mock(ITextEditor.class);
		
		IPath ipath = mock(IPath.class);
		File file = mock(File.class);
		IFile ifile = mock(IFile.class);
		IFileEditorInput fileInput = mock(IFileEditorInput.class);
		
		when(part.getEditorInput()).thenReturn(fileInput);
		when(fileInput.getFile()).thenReturn(ifile);
		
		when(ifile.getLocation()).thenReturn(ipath);
		when(ifile.getName()).thenReturn(filename);
		
		when(ipath.toFile()).thenReturn(file);
		
		when(file.getName()).thenReturn(filename);
		when(file.getPath()).thenReturn(filename);
		when(file.length()).thenReturn((long) fileLength);

		return part;
	}
}
