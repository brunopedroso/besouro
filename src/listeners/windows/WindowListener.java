package listeners.windows;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import listeners.ResourceChangeAdapter;

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
 * Provides the IWindowListener-implemented class to catch the "Browser activated", "Browser
 * closing" event. This inner class is designed to be used by the outer EclipseSensor class.
 *
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class WindowListener implements IWindowListener, IPartListener, IDocumentListener {
	
	private static int activeBufferSize;	
	public static int getActiveBufferSize() {
		return activeBufferSize;
	}

//	private static int thresholdBufferSize;
//	private static Object isModifiedFromFile;
//	private static int fromFileBufferSize;
//	private static ITextEditor previousTextEditor;
//	private static ITextEditor activeTextEditor;
//	private static ITextEditor deactivatedTextEditor;
//	private static boolean isActivatedWindow;

	
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
      
      // BuffTrans: Copy the new active file size to the threshold buffer size .
      activeBufferSize = activeTextEditor.getDocumentProvider().getDocument(activeTextEditor.getEditorInput()).getLength();
//      thresholdBufferSize = activeBufferSize;

    }
  }


  public void windowDeactivated(IWorkbenchWindow window) {
	
//    isActivatedWindow = false;
    
//    IEditorPart activeEditorPart = window.getActivePage().getActiveEditor();
    
//    if (activeEditorPart instanceof ITextEditor) {
    	
      // provider could be null if the text editor is closed before this method is called.
//      previousTextEditor = (ITextEditor) activeEditorPart;
//      fromFileBufferSize = previousTextEditor.getDocumentProvider().getDocument(previousTextEditor.getEditorInput()).getLength();

      // Check if a threshold buffer is either dirty or
      // not the same as the current from file buffer size;
//      isModifiedFromFile = (previousTextEditor.isDirty() || (thresholdBufferSize != fromFileBufferSize));
//    }
  }

  public void windowOpened(IWorkbenchWindow window) {
	  
	    IWorkbenchWindow[] activeWindows = Activator.getDefault().getWorkbench().getWorkbenchWindows();

	    for (int i = 0; i < activeWindows.length; i++) {
	    	
	      IWorkbenchPage activePage = activeWindows[i].getActivePage();
	      
	      
	      IEditorPart activeEditorPart = activePage.getActiveEditor();
	      if (activeEditorPart instanceof ITextEditor) {
	    	  
	        // --- Sets activeTextEditor. Otherwise a first activated file would not be recorded.
	    	ITextEditor activeTextEditor = (ITextEditor) activeEditorPart;
			String fileResource = activeTextEditor.getEditorInput().getName();
	        
	        //TODO should it be done here?
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
			
			
			IDocument document = activeTextEditor.getDocumentProvider().getDocument(activeEditorPart.getEditorInput());
			
			// update shared vars
	        activeBufferSize = document.getLength();
//	        thresholdBufferSize = document.getLength();
	        
	        // add listeners
	        document.addDocumentListener(this);
	        activePage.addPartListener(this);
	        
	        
	      }
	    }
  }
  
  
	public void partActivated(IWorkbenchPart part) {

	    if (part instanceof ITextEditor) {
	      
//	      isActivatedWindow = true;
	      ITextEditor activeTextEditor = (ITextEditor) part;
	      
	      IDocumentProvider provider = activeTextEditor.getDocumentProvider();
	      provider.getDocument(activeTextEditor.getEditorInput()).addDocumentListener(this);
	      activeBufferSize = provider.getDocument(activeTextEditor.getEditorInput()).getLength();
	
	      // BuffTrans: Copy the new active file size to the threshold buffer size .
//	      thresholdBufferSize = activeBufferSize;
	      
	    }
	  }

	  public void partBroughtToTop(IWorkbenchPart part) {
	    // not supported in Eclipse Sensor.
	  }

	  public void partClosed(IWorkbenchPart part) {
	    if (part instanceof ITextEditor) {
	    	
	    	//Does it work?
	//      URI fileResource = EclipseSensor.this.getFileResource((ITextEditor) part);
	      URI fileResource;
	      
		try {
			fileResource = new URI( ((ITextEditor) part).getEditorInput().getName());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	      
	      Map<String, String> keyValueMap = new HashMap<String, String>();
	      keyValueMap.put(ISensor.SUBTYPE, "Close");
	      if (fileResource != null && fileResource.toString().endsWith(ISensor.JAVA_EXT)) {
	        keyValueMap.put("Language", "java");
	      }
	      if (fileResource != null) {
	        keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);
	        
	        //TODO z extract this utils
	        keyValueMap.put(ISensor.UNIT_NAME, ResourceChangeAdapter.extractFileName(fileResource));
	        sensor.addDevEvent(ISensor.DEVEVENT_EDIT,fileResource, keyValueMap, fileResource.toString());
	        
	      }
	      
//	      IEditorPart activeEditorPart = part.getSite().getPage().getActiveEditor();
//	      if (activeEditorPart == null) {
//	        activeTextEditor = null;
//	      }
	      
	    }
	  }
	
	  public void partDeactivated(IWorkbenchPart part) {
		  
//	    if (part instanceof ITextEditor) {// && !part.equals(deactivatedTextEditor)) {
	    	
//	      ITextEditor deactivatedTextEditor = (ITextEditor) part;
	      
//	      if (isActivatedWindow) {
//	        IEditorPart activeEditorPart = part.getSite().getPage().getActiveEditor();
	
	        // Sets activeTextEdtior to be null only when there is no more active editor.
	        // Otherwise the case that the non text editor part is active causes the activeTextEditor
	        // to be null so that sensor is not collected after that.
//	        if (activeEditorPart == null) {
//	          this.activeTextEditor = null;
//	        }
	
	        // BuffTrans to get the toFrom buffer size.
//	        ITextEditor editor = (ITextEditor) part;
//	        IDocumentProvider provider = editor.getDocumentProvider();
	
	        // provider could be null if the text editor is closed before this method is called.
//	        isModifiedFromFile = false;
//	        previousTextEditor = null;
	
//	        if (provider != null) {
//	        	previousTextEditor = editor;
//	          int fromFileBufferSize = provider.getDocument(editor.getEditorInput()).getLength();
	
	          // Check if a threshold buffer is either dirty or
	          // not the same as the current from file buffer size;
//	          isModifiedFromFile = (editor.isDirty() || (thresholdBufferSize != fromFileBufferSize));
	          
//	        }
//	      }
//	      else {
//	    	  isActivatedWindow = true;
//	      }
//	    }
	  }
	

	  public void partOpened(IWorkbenchPart part) {
		  
	    if (part instanceof ITextEditor) {
//	    		&& !part.equals(this.activeTextEditor)) {
	    	
			ITextEditor activeTextEditor = (ITextEditor) part;
			  
			  
			  //Does it work?
			//      URI fileResource = EclipseSensor.this.getFileResource((ITextEditor) part);
			URI fileResource;
			try {
				fileResource = new URI( ((ITextEditor) part).getEditorInput().getName());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
	      
			  Map<String, String> keyValueMap = new HashMap<String, String>();
			  keyValueMap.put(ISensor.SUBTYPE, "Open");
			  keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);
			  keyValueMap.put(ISensor.UNIT_NAME, 
			      ResourceChangeAdapter.extractFileName(fileResource));
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
		  //TODO z is it time to send anithing?
//	    EclipseSensor.this.sensorShellWrapper.send();
		  
		  //TODO should we register window closing?
		  
	  }

	  
	  
	  
  
}