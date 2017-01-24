package besouro.listeners;

import java.io.IOException;
import java.util.Date;

import org.eclipse.core.resources.IFile;
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

import besouro.measure.CoverageMeter;
import besouro.measure.JavaStatementMeter;
import besouro.model.FileOpenedAction;
import besouro.plugin.Activator;
import besouro.stream.ActionOutputStream;


public class WindowListener implements IWindowListener, IPartListener, IDocumentListener {

	private ActionOutputStream stream;
	private IWorkbench workbench;

	private JavaStatementMeter measurer = new JavaStatementMeter();
	
	public WindowListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	/**
	 * For testing purposes
	 */
	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public void windowOpened(IWorkbenchWindow window) {

		if (workbench == null)
			workbench = Activator.getDefault().getWorkbench();

		IWorkbenchWindow[] activeWindows = workbench.getWorkbenchWindows();

		for (int i = 0; i < activeWindows.length; i++) {

			// not using doclistener
//			installDocumentListener(activeWindows[i].getActivePage().getActiveEditor());

			IWorkbenchPage activePage = activeWindows[i].getActivePage();
			if (activePage != null) {
				activePage.addPartListener(this);
				try {
					registerFileOpenAction(activePage.getActiveEditor());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		}
	}

	public void windowDeactivated(IWorkbenchWindow window) {
	}

	public void windowActivated(IWorkbenchWindow window) {
		// not using doclistener
//		installDocumentListener(window.getActivePage().getActiveEditor());
	}

	public void partOpened(IWorkbenchPart part) {
		try {
			registerFileOpenAction(part);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void partActivated(IWorkbenchPart part) {
		// not using doclistener
//		installDocumentListener(part);
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
		// [data] we can use it to generate small detailed edit events. Is it necessary?
	}

	public void windowClosed(IWorkbenchWindow window) {
	}

//	private void installDocumentListener(IWorkbenchPart part) {
//		if (part instanceof ITextEditor) {
//			ITextEditor activeTextEditor = (ITextEditor) part;
//			IDocument document = activeTextEditor.getDocumentProvider().getDocument(activeTextEditor.getEditorInput());
//			document.addDocumentListener(this);
//		}
//	}

	private void registerFileOpenAction(IWorkbenchPart part) throws IOException {

		if (part instanceof ITextEditor) {

			ITextEditor textEditor = (ITextEditor) part;
			IEditorInput input = textEditor.getEditorInput();
			
			if (input instanceof IFileEditorInput) {
				
				IFileEditorInput fileInput = (IFileEditorInput) input;
				IFile file = fileInput.getFile();
				FileOpenedAction action = new FileOpenedAction(new Date(), file.getName());
				
				JavaStatementMeter meter = this.measurer.measureJavaFile(file);

				CoverageMeter coverageMeter = new CoverageMeter();
				
				coverageMeter.execute(file.getLocation().toFile().toString());
				
				action.setFileSize((int) file.getLocation().toFile().length());
				action.setIsTestEdit(meter.isTest());
				action.setMethodsCount(meter.getNumOfMethods());
				action.setStatementsCount(meter.getNumOfStatements());
				action.setTestMethodsCount(meter.getNumOfTestMethods());
				action.setTestAssertionsCount(meter.getNumOfTestAssertions());
				
				stream.addAction(action);
				
			}

		}
	}
	
	/**
	 * for testing purposes
	 */
	public void setMeasurer(JavaStatementMeter meter) {
		this.measurer = meter;
	}
}