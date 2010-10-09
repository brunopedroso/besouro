package besouro.listeners;

import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import besouro.measure.BuildErrorSensor;
import besouro.measure.JavaStatementMeter;
import besouro.model.EditAction;
import besouro.stream.ActionOutputStream;


/**
 * Provides "Open Project, "Close Project", and "Save File" events. Note that
 * this implementing class uses Visitor pattern so that key point to gather
 * these event information is inside the visitor method which is implemented
 * from <code>IResourceDeltaVisitor</code> class.
 * 
 * @author Takuya Yamashita
 */
public class ResourceChangeListener implements IResourceChangeListener, IResourceDeltaVisitor {

	private ActionOutputStream sensor;
	private BuildErrorSensor buildErrorSensor;
	private JavaStatementMeter measurer = new JavaStatementMeter();

	public ResourceChangeListener(ActionOutputStream s) {
		this.sensor = s;
		buildErrorSensor = new BuildErrorSensor(sensor);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		
		if ((event.getType() & IResourceChangeEvent.POST_CHANGE) != 0) {

			try {
				IResourceDelta rootDelta = event.getDelta();

				// Accepts the class instance to let the instance be able to visit resource delta.
				rootDelta.accept(this);
				
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean visit(IResourceDelta delta) throws CoreException {

		IResource resource = delta.getResource();
		int flag = delta.getFlags();
		int kind = delta.getKind();

		// If there is compilation problem with the current java file then send out the activity data.
		// do not catch errors in other files
		if ((flag & IResourceDelta.MARKERS) != 0) {
			buildErrorSensor.findBuildProblem(delta);
		}

		// :RESOLVED: 26 May 2003
		// Note that the 147456 enumeration type is not listed in the IResourceDelta static filed.
		// However, its number is generated when Project is either opened or closed so that it is checked in the logical condition.
		int ANOTHER_OPEN_CLOSE_FLAG = 147456;

		if (resource instanceof IProject && ((flag == IResourceDelta.OPEN) || (flag == ANOTHER_OPEN_CLOSE_FLAG))) {

			// NOTE: We do not register project opens and closes yet

			// do not visit the children
			return false;

		} else if ((kind == IResourceDelta.CHANGED) && resource instanceof IFile && flag == IResourceDelta.CONTENT) {

			if (resource.getLocation().toString().endsWith(".java")) {

				IFile changedFile = (IFile) resource;
				EditAction action = new EditAction(new Date(), changedFile.getName());
				
				JavaStatementMeter meter = this.measurer.measureJavaFile(changedFile);
				
				action.setFileSize((int) changedFile.getLocation().toFile().length());
				action.setIsTestEdit(meter.isTest());
				action.setMethodsCount(meter.getNumOfMethods());
				action.setStatementsCount(meter.getNumOfStatements());
				action.setTestMethodsCount(meter.getNumOfTestMethods());
				action.setTestAssertionsCount(meter.getNumOfTestAssertions());
				
				sensor.addAction(action);

			}

		}

		// visit the children
		return true;

	}
	
	/**
	 * for testing purposes
	 */
	public void setMeasurer(JavaStatementMeter meter) {
		this.measurer = meter;
	}

}