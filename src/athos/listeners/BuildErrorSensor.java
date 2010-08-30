package athos.listeners;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ui.IFileEditorInput;

import athos.model.BuildErrorAction;
import athos.model.Clock;
import athos.stream.ActionOutputStream;


/**
 * Provides an approach to find build error using problem markers.
 *  
 * @author Hongbing Kou
 * @version $Id: BuildErrorSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class BuildErrorSensor {

	private ActionOutputStream sensor;
  
  public BuildErrorSensor(ActionOutputStream sensor) {
    this.sensor = sensor;
  }
  

  public void findBuildProblem(IResourceDelta delta) {
	
	  //TODO z[clean] do we realy need to use the active editor?
	  
    ITextEditor activeEditor = WindowListener.getActiveTextEditor();
    
    // Do nothing if there is no file edited.
    if (activeEditor == null) {
    	return;
    }
      
    if (!(activeEditor.getEditorInput() instanceof IFileEditorInput)) {
    	return;
    }
    
    IFileEditorInput fileEditorInput = (IFileEditorInput) activeEditor.getEditorInput();
    // Do nothing if it is not file edit
    if (fileEditorInput == null) {
    	return;
    }
    
    // We are interested in java file only.
    IFile file = fileEditorInput.getFile();
    if (file == null) {
    	return;
    }
    
    String fileName2 = file.getLocation().toString();
    if (!fileName2.endsWith(".java")) {
    	return;
    }
    
    IResource resource = delta.getResource();
    if (resource == null) {
    	return;
    }    
    IPath location = resource.getLocation();
    if (location == null) {
    	return;
    }    
    String deltaFileName = location.toString();
    if (!fileName2.equals(deltaFileName)) {
    	return;
    }
    
    URI fileResource = fileEditorInput.getFile().getLocationURI();
    IMarkerDelta markerDeltas[] = delta.getMarkerDeltas();
    if (markerDeltas == null || markerDeltas.length == 0) {
      return;
    }
    
    // Message pool is used to filter out the repeated compilation error.
    HashSet<String> messagePool = new HashSet<String>();
    for (int i = 0; i < markerDeltas.length; i++) {
      IMarkerDelta markerDelta = (IMarkerDelta) markerDeltas[i];
      Map<String, String> keyValueMap = processPossibleBuildErrors(fileResource, markerDelta);
      if (!keyValueMap.isEmpty()) {

        String errorMsg = keyValueMap.get("Error");
        String data = file.getLocation().toString() + "#" + errorMsg;
        
        // dont repeat
        if (!messagePool.contains(data)) {
        	
        	
        	// FUI EU QUEM CRIOU O BUILD ERROR ?    ƒ IGUAL AO COMPILATION ?
        	
          this.sensor.addAction( new BuildErrorAction(new Clock(new Date()), new File(fileResource), errorMsg));
          messagePool.add(data);
          
        }
      }
    }      
  }
  
  /**
   * Processes the marker defined in the resource file.
   * 
   * @param fileResource File resource.
   * @param markerDelta Marker to in the resource file.
   * @return A map contains metric names and values.
   */
  private Map<String, String> processPossibleBuildErrors(URI fileResource, 
      IMarkerDelta markerDelta) {
    Map<String, String> keyValueMap = new HashMap<String, String>();
    if (markerDelta.getType() == IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER) {
      String severity = markerDelta.getAttribute("severity").toString();
      String message = markerDelta.getAttribute("message").toString();
      
      // Only error will be processed, warning & info will be ignored.
      if ("2".equals(severity) &&  
          (markerDelta.getKind() == IResourceDelta.ADDED || 
           markerDelta.getKind() == IResourceDelta.CHANGED)) {
        keyValueMap.put("Subtype", "Compile");
        keyValueMap.put("Success", "false");
        keyValueMap.put("Error", message);
      }
    }  

    return keyValueMap;
  }
}
