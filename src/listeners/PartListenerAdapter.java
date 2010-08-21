package listeners;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import sensor.ISensor;

/**
 * Provides the IPartListener-implemented class to catch "part opened", "part closed" event as
 * well as setting active editor part to the activeTextEditor instance and setting active buffer
 * size of the activeBufferSize field of the EclipseSensor class. Note that methods are called by
 * the following order:
 * <ol>
 * <li>partClosed() or partOpened()</li>
 * <li>partDeactivated()</li>
 * <li>partActivate() if any</li>
 * </ol>
 *
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class PartListenerAdapter implements IPartListener {
	
	private ITextEditor activeTextEditor;
	private ISensor sensor;
	
	public PartListenerAdapter(ISensor sensor) {
		this.sensor = sensor;
	}
  

	public void partActivated(IWorkbenchPart part) {

	    if (part instanceof ITextEditor) {
	      
	      WindowListenerAdapter.isActivatedWindow = true;
	      activeTextEditor = (ITextEditor) part;
	      
	      ITextEditor editor = activeTextEditor;
	      IDocumentProvider provider = editor.getDocumentProvider();
	      IDocument document = provider.getDocument(editor.getEditorInput());
	      document.addDocumentListener(new DocumentListenerAdapter());
	      WindowListenerAdapter.activeBufferSize = provider.getDocument(editor.getEditorInput()).getLength();
	
	      // BuffTrans: Copy the new active file size to the threshold buffer size .
	      WindowListenerAdapter.thresholdBufferSize = WindowListenerAdapter.activeBufferSize;
	      
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
	      
	      IEditorPart activeEditorPart = part.getSite().getPage().getActiveEditor();
	      if (activeEditorPart == null) {
	        activeTextEditor = null;
	      }
	      
	    }
	  }
	
	  public void partDeactivated(IWorkbenchPart part) {
		  
	    if (part instanceof ITextEditor && !part.equals(WindowListenerAdapter.deactivatedTextEditor)) {
	    	
	      WindowListenerAdapter.deactivatedTextEditor = (ITextEditor) part;
	      
	      if (WindowListenerAdapter.isActivatedWindow) {
	        IEditorPart activeEditorPart = part.getSite().getPage().getActiveEditor();
	
	        // Sets activeTextEdtior to be null only when there is no more active editor.
	        // Otherwise the case that the non text editor part is active causes the activeTextEditor
	        // to be null so that sensor is not collected after that.
	        if (activeEditorPart == null) {
	          this.activeTextEditor = null;
	        }
	
	        // BuffTrans to get the toFrom buffer size.
	        ITextEditor editor = (ITextEditor) part;
	        IDocumentProvider provider = editor.getDocumentProvider();
	
	        // provider could be null if the text editor is closed before this method is called.
	        WindowListenerAdapter.isModifiedFromFile = false;
	        WindowListenerAdapter.previousTextEditor = null;
	
	        if (provider != null) {
	        	WindowListenerAdapter.previousTextEditor = editor;
	          int fromFileBufferSize = provider.getDocument(editor.getEditorInput()).getLength();
	
	          // Check if a threshold buffer is either dirty or
	          // not the same as the current from file buffer size;
	          WindowListenerAdapter.isModifiedFromFile = (editor.isDirty() || (WindowListenerAdapter.thresholdBufferSize != fromFileBufferSize));
	          
	        }
	      }
	      else {
	    	  WindowListenerAdapter.isActivatedWindow = true;
	      }
	    }
	  }
	

	  public void partOpened(IWorkbenchPart part) {
		  
	    if (part instanceof ITextEditor && !part.equals(this.activeTextEditor)) {
	    	
	      this.activeTextEditor = (ITextEditor) part;
	      
	      
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
}
