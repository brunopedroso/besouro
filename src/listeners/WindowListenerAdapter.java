package listeners;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import sensor.ISensor;
import test_plugin.Activator;
import test_plugin.Startup;

/**
 * Provides the IWindowListener-implemented class to catch the "Browser activated", "Browser
 * closing" event. This inner class is designed to be used by the outer EclipseSensor class.
 *
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class WindowListenerAdapter implements IWindowListener {
	
	//TODO shared: who uses what?
	public static int activeBufferSize;
	public static int thresholdBufferSize;
	
	public static ITextEditor activeTextEditor;
	public static ITextEditor deactivatedTextEditor;
	public static ITextEditor previousTextEditor;
	
	public static Object isModifiedFromFile;
	public static boolean isActivatedWindow;
	
	private ISensor sensor;
	
	public WindowListenerAdapter(ISensor sensor) {
		this.sensor = sensor;
	}


  public void windowActivated(IWorkbenchWindow window) {
	  
    IEditorPart activeEditorPart = window.getActivePage().getActiveEditor();
    
    if (activeEditorPart instanceof ITextEditor) {
    	
      activeTextEditor = (ITextEditor) activeEditorPart;
      
      activeTextEditor.getDocumentProvider()
      				  .getDocument(activeTextEditor.getEditorInput())
      				  .addDocumentListener(new DocumentListenerAdapter());
      
      // BuffTrans: Copy the new active file size to the threshold buffer size .
      activeBufferSize = activeTextEditor.getDocumentProvider().getDocument(activeTextEditor.getEditorInput()).getLength();
      thresholdBufferSize = activeBufferSize;

    }
  }


  public void windowDeactivated(IWorkbenchWindow window) {
	
//    isActivatedWindow = false;
    
    IEditorPart activeEditorPart = window.getActivePage().getActiveEditor();
    
    if (activeEditorPart instanceof ITextEditor) {
    	
      // provider could be null if the text editor is closed before this method is called.
//      previousTextEditor = (ITextEditor) activeEditorPart;
//      int fromFileBufferSize = previousTextEditor.getDocumentProvider().getDocument(previousTextEditor.getEditorInput()).getLength();

      // Check if a threshold buffer is either dirty or
      // not the same as the current from file buffer size;
//      isModifiedFromFile = (previousTextEditor.isDirty() || (thresholdBufferSize != fromFileBufferSize));
    }
  }

  public void windowOpened(IWorkbenchWindow window) {
	  
	    IWorkbenchWindow[] activeWindows = Activator.getDefault().getWorkbench().getWorkbenchWindows();

	    for (int i = 0; i < activeWindows.length; i++) {
	    	
	      IWorkbenchPage activePage = activeWindows[i].getActivePage();
	      
	      
	      IEditorPart activeEditorPart = activePage.getActiveEditor();
	      if (activeEditorPart instanceof ITextEditor) {
	    	  
	        // --- Sets activeTextEditor. Otherwise a first activated file would not be recorded.
	        activeTextEditor = (ITextEditor) activeEditorPart;
	        
	        //TODO should it be done here?
	        registerOpenFileEvent(activeTextEditor.getEditorInput().getName());
			
			
			IDocument document = activeTextEditor.getDocumentProvider().getDocument(activeEditorPart.getEditorInput());
			
			// update shared vars
	        activeBufferSize = document.getLength();
	        thresholdBufferSize = document.getLength();
	        
	        // add listeners
	        document.addDocumentListener(new DocumentListenerAdapter());
	        activePage.addPartListener(new PartListenerAdapter(sensor));
	        
	        
	      }
	    }
  }
  
	private void registerOpenFileEvent(String fileResource) {
		
		//TODO: do we realy need an URI?! :-(
		URI uri;
		try {
			uri = new URI(fileResource);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		
		//TODO simplify event register API
		Map<String, String> keyValueMap = new HashMap<String, String>();
		keyValueMap.put(ISensor.SUBTYPE, "Open");
		keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);
		keyValueMap.put(ISensor.UNIT_NAME, fileResource);
		sensor.addDevEvent(ISensor.DEVEVENT_EDIT, uri, keyValueMap, "Opened " + fileResource.toString());
	}
	
	
	  public void windowClosed(IWorkbenchWindow window) {
		  //TODO z is it time to send anithing?
//	    EclipseSensor.this.sensorShellWrapper.send();
		  
		  //TODO should we register window closing?
		  
	  }

  
}