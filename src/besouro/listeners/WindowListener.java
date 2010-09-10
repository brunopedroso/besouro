package besouro.listeners;

import java.io.File;
import java.util.Date;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import besouro.model.Clock;
import besouro.model.FileOpenedAction;
import besouro.plugin.Activator;
import besouro.stream.ActionOutputStream;


public class WindowListener implements IWindowListener, IPartListener,
		IDocumentListener {

//	// we do not need to maintain the active editor any more
//	private static ITextEditor activeTextEditor;
//
//	public static ITextEditor getActiveTextEditor() {
//		return activeTextEditor;
//	}

	private ActionOutputStream stream;
	private IWorkbench workbench;
	private JavaStatementMeter javaMeter = new JavaStatementMeter();

	public WindowListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	/**
	 * For testing purposes
	 */
	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	/**
	 * For testing purposes
	 */
	public void setJavaMeter(JavaStatementMeter javaMeter) {
		this.javaMeter = javaMeter;
	}

	public void windowOpened(IWorkbenchWindow window) {

		if (workbench == null)
			workbench = Activator.getDefault().getWorkbench();

		IWorkbenchWindow[] activeWindows = workbench.getWorkbenchWindows();

		for (int i = 0; i < activeWindows.length; i++) {

			installDocumentListener(activeWindows[i].getActivePage()
					.getActiveEditor());

			IWorkbenchPage activePage = activeWindows[i].getActivePage();
			activePage.addPartListener(this);

			registerFileOpenAction(activePage.getActiveEditor());

		}
	}

	public void windowDeactivated(IWorkbenchWindow window) {
	}

	public void windowActivated(IWorkbenchWindow window) {
		installDocumentListener(window.getActivePage().getActiveEditor());
	}

	public void partOpened(IWorkbenchPart part) {
		registerFileOpenAction(part);
	}

	public void partActivated(IWorkbenchPart part) {
		installDocumentListener(part);
	}

	public void partBroughtToTop(IWorkbenchPart part) {
	}

	public void partClosed(IWorkbenchPart part) {
	}

	public void partDeactivated(IWorkbenchPart part) {
	}

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {
	}

	public void windowClosed(IWorkbenchWindow window) {
	}

	private void installDocumentListener(IWorkbenchPart part) {
		if (part instanceof ITextEditor) {
			ITextEditor activeTextEditor = (ITextEditor) part;
			IDocument document = activeTextEditor.getDocumentProvider()
					.getDocument(activeTextEditor.getEditorInput());
			document.addDocumentListener(this);
		}
	}

	private void registerFileOpenAction(IWorkbenchPart part) {

		if (part instanceof ITextEditor) {

			ITextEditor textEditor = (ITextEditor) part;

			IEditorInput input = textEditor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFileEditorInput fileInput = (IFileEditorInput) input;

				File file = fileInput.getFile().getLocation().toFile();
				FileOpenedAction action = new FileOpenedAction(new Clock(new Date()), file);

				 action.setFileSize((int) file.length());

				javaMeter.reset();
				javaMeter.measureJavaFile(fileInput.getFile());
				action.setMethodsCount(javaMeter.getNumOfMethods());
				action.setStatementsCount(javaMeter.getNumOfStatements());
				action.setTestAssertionsCount(javaMeter.getNumOfTestAssertions());
				action.setTestMethodsCount(javaMeter.getNumOfTestMethods());

				stream.addAction(action);
			}

		}
	}

}