package besouro.measure;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaModelMarker;

import besouro.model.CompilationAction;
import besouro.stream.ActionOutputStream;


/**
 * Provides an approach to find build error using problem markers.
 * 
 * @author Hongbing Kou
 */
public class BuildErrorSensor {

	private ActionOutputStream sensor;

	public BuildErrorSensor(ActionOutputStream sensor) {
		this.sensor = sensor;
	}

	public void findBuildProblem(IResourceDelta delta) {

		// we are registering build errors in any file.
		// not only in the changed file, as it was in the original version
		// from Hongbing. Aparently, rules are gonna manage it.

		IResource resource = delta.getResource();
		if (resource == null) {
			return;
		}

		IPath location = resource.getLocation();
		if (location == null) {
			return;
		}

		IMarkerDelta markerDeltas[] = delta.getMarkerDeltas();
		if (markerDeltas == null || markerDeltas.length == 0) {
			return;
		}

		// Message pool is used to filter out the repeated compilation error.
		HashSet<String> messagePool = new HashSet<String>();
		for (int i = 0; i < markerDeltas.length; i++) {

			IMarkerDelta markerDelta = (IMarkerDelta) markerDeltas[i];
			Map<String, String> keyValueMap = processPossibleBuildErrors(markerDelta);

			if (!keyValueMap.isEmpty()) {

				String errorMsg = keyValueMap.get("Error");
				// String data = file.getLocation().toString() + "#" + errorMsg;
				String data = location.toString() + "#" + errorMsg;

				// dont repeat
				if (!messagePool.contains(data)) {

					CompilationAction action = new CompilationAction(new Date(), resource.getName());
					action.setErrorMessage(errorMsg);
					this.sensor.addAction(action);
					messagePool.add(data);

				}
			}
		}
	}

	private Map<String, String> processPossibleBuildErrors(
			IMarkerDelta markerDelta) {

		Map<String, String> keyValueMap = new HashMap<String, String>();

		if (markerDelta.getType() == IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER) {

			String severity = markerDelta.getAttribute("severity").toString();
			String message = markerDelta.getAttribute("message").toString();

			// Only error will be processed, warning & info will be ignored.
			if ("2".equals(severity)
					&& (markerDelta.getKind() == IResourceDelta.ADDED || markerDelta
							.getKind() == IResourceDelta.CHANGED)) {

				keyValueMap.put("Subtype", "Compile");
				keyValueMap.put("Success", "false");
				keyValueMap.put("Error", message);

			}
		}

		return keyValueMap;
	}
}
