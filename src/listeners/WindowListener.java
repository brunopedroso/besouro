package listeners;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import sensor.ISensor;
import test_plugin.Activator;

/**
 * Provides the IWindowListener-implemented class to catch the
 * "Browser activated", "Browser closing" event. This inner class is designed to
 * be used by the outer EclipseSensor class.
 * 
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class WindowListener implements IWindowListener, IPartListener,
		IDocumentListener {

	private static int activeBufferSize;

	public static int getActiveBufferSize() {
		return activeBufferSize;
	}

	private ISensor sensor;

	public WindowListener(ISensor sensor) {
		this.sensor = sensor;
	}

	public void windowActivated(IWorkbenchWindow window) {

		IEditorPart activeEditorPart = window.getActivePage().getActiveEditor();

		if (activeEditorPart instanceof ITextEditor) {

			ITextEditor activeTextEditor = (ITextEditor) activeEditorPart;

			activeTextEditor.getDocumentProvider()
					.getDocument(activeTextEditor.getEditorInput())
					.addDocumentListener(this);

			activeBufferSize = activeTextEditor.getDocumentProvider()
					.getDocument(activeTextEditor.getEditorInput()).getLength();

		}
	}

	public void windowDeactivated(IWorkbenchWindow window) {

	}

	public void windowOpened(IWorkbenchWindow window) {

		IWorkbenchWindow[] activeWindows = Activator.getDefault()
				.getWorkbench().getWorkbenchWindows();

		for (int i = 0; i < activeWindows.length; i++) {

			IWorkbenchPage activePage = activeWindows[i].getActivePage();

			IEditorPart activeEditorPart = activePage.getActiveEditor();
			if (activeEditorPart instanceof ITextEditor) {

				// --- Sets activeTextEditor. Otherwise a first activated file
				// would not be recorded.
				ITextEditor activeTextEditor = (ITextEditor) activeEditorPart;
				String fileResource = activeTextEditor.getEditorInput()
						.getName();

				// Register window open event
				// TODO: do we realy need an URI?! :-(
				URI uri;
				try {
					uri = new URI(fileResource);
				} catch (URISyntaxException e) {
					throw new RuntimeException(e);
				}

				// TODO simplify event register API
				Map<String, String> keyValueMap = new HashMap<String, String>();
				keyValueMap.put(ISensor.SUBTYPE, "Open");
				keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);
				keyValueMap.put(ISensor.UNIT_NAME, fileResource);
				sensor.addDevEvent(ISensor.DEVEVENT_EDIT, uri, keyValueMap,
						"Opened " + fileResource.toString());

				IDocument document = activeTextEditor.getDocumentProvider()
						.getDocument(activeEditorPart.getEditorInput());

				activeBufferSize = document.getLength();

				// add listeners
				document.addDocumentListener(this);
				activePage.addPartListener(this);

			}
		}
	}

	public void partActivated(IWorkbenchPart part) {

		if (part instanceof ITextEditor) {

			ITextEditor activeTextEditor = (ITextEditor) part;

			IDocumentProvider provider = activeTextEditor.getDocumentProvider();
			provider.getDocument(activeTextEditor.getEditorInput())
					.addDocumentListener(this);
			activeBufferSize = provider.getDocument(
					activeTextEditor.getEditorInput()).getLength();

		}
	}

	public void partBroughtToTop(IWorkbenchPart part) {
	}

	public void partClosed(IWorkbenchPart part) {
		if (part instanceof ITextEditor) {

			// Does it work?
			// URI fileResource =
			// EclipseSensor.this.getFileResource((ITextEditor) part);
			URI fileResource;

			try {
				fileResource = new URI(((ITextEditor) part).getEditorInput()
						.getName());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			Map<String, String> keyValueMap = new HashMap<String, String>();
			keyValueMap.put(ISensor.SUBTYPE, "Close");
			if (fileResource != null
					&& fileResource.toString().endsWith(ISensor.JAVA_EXT)) {
				keyValueMap.put("Language", "java");
			}
			if (fileResource != null) {
				keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);

				keyValueMap.put(ISensor.UNIT_NAME,
						ResourceChangeListener.extractFileName(fileResource));
				sensor.addDevEvent(ISensor.DEVEVENT_EDIT, fileResource,
						keyValueMap, fileResource.toString());

			}

		}
	}

	public void partDeactivated(IWorkbenchPart part) {

	}

	public void partOpened(IWorkbenchPart part) {

		if (part instanceof ITextEditor) {

			ITextEditor activeTextEditor = (ITextEditor) part;

			// TODO: do we realy need an URI?! :-(
			// Does it work?
			// URI fileResource = EclipseSensor.this.getFileResource((ITextEditor) part);
			URI fileResource;
			try {
				fileResource = new URI(activeTextEditor.getEditorInput().getName());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			Map<String, String> keyValueMap = new HashMap<String, String>();
			keyValueMap.put(ISensor.SUBTYPE, "Open");
			keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);
			keyValueMap.put(ISensor.UNIT_NAME,
					ResourceChangeListener.extractFileName(fileResource));
			sensor.addDevEvent(ISensor.DEVEVENT_EDIT, fileResource,
					keyValueMap, fileResource.toString());

		}
	}

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {
		activeBufferSize = event.getDocument().getLength();
	}

	public void windowClosed(IWorkbenchWindow window) {

	}

}