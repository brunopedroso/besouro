package athos.listeners;
import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import athos.model.Clock;
import athos.model.EditAction;
import athos.stream.ActionOutputStream;


/**
 * Provides "Open Project, "Close Project", and "Save File" events. Note that
 * this implementing class uses Visitor pattern so that key point to gather
 * these event information is inside the visitor method which is implemented
 * from <code>IResourceDeltaVisitor</code> class.
 * 
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class ResourceChangeListener implements IResourceChangeListener, IResourceDeltaVisitor {

	private ActionOutputStream sensor;
	private BuildErrorSensor buildErrorSensor;

	public ResourceChangeListener(ActionOutputStream s) {
		this.sensor = s;
		buildErrorSensor = new BuildErrorSensor(sensor);
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		if (((event.getType() & IResourceChangeEvent.POST_CHANGE) != 0)) {
			// ||
			// ((event.getType() & IResourceChangeEvent.POST_AUTO_BUILD) != 0))
			// {
			try {
				IResourceDelta rootDelta = event.getDelta();

				// Accepts the class instance to let the instance be able to
				// visit resource delta.
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

		// FIXME [build errors] do not catch errors caused in another editor...
		// If there is compilation problem with the current java file then send out the activity data.
		 if ((flag & IResourceDelta.MARKERS) != 0) {
			buildErrorSensor.findBuildProblem(delta);
		 }

		// :RESOLVED: 26 May 2003
		// Note that the 147456 enumeration type is not listed in the IResourceDelta static filed.
		// However, its number is generated when Project is either opened or
		// closed so that it is checked in the logical condition.
		int ANOTHER_OPEN_CLOSE_FLAG = 147456;
		
		if (resource instanceof IProject && ((flag == IResourceDelta.OPEN) || (flag == ANOTHER_OPEN_CLOSE_FLAG))) {
			
//			IProject project = resource.getProject();
//			String projectName = project.getName();
//			URI projectResoruce = project.getFile(".project").getLocationURI();
//
//			Map<String, String> keyValueMap = new HashMap<String, String>();
//			keyValueMap.put(ActionOutputStream.UNIT_TYPE, "project");
//			keyValueMap.put(ActionOutputStream.UNIT_NAME, projectName);
//
//			if (((IProject) resource).isOpen()) {
//				keyValueMap.put(ActionOutputStream.SUBTYPE, "Open");
//			} else {
//				keyValueMap.put(ActionOutputStream.SUBTYPE, "Close");
//			}
//			
//			sensor.addAction(ActionOutputStream.DEVEVENT_EDIT, projectResoruce, keyValueMap, projectResoruce.toString());

			
			//NOTE: We do not register project opens and closes yet
			// sensor.addAction(new ProjectOpenAction());

			
			// do not visit the children
			return false;
			
		} else if ((kind == IResourceDelta.CHANGED) && resource instanceof IFile && flag == IResourceDelta.CONTENT) {
				
			if (resource.getLocation().toString().endsWith(ActionOutputStream.JAVA_EXT)) {
				
				IFile changedFile = (IFile) resource;
				URI fileResource = changedFile.getLocationURI();
				
				//TODO need the duration
				EditAction action = new EditAction(new Clock(new Date()), new File(fileResource), 0);
				action.setOperation("Save");
				action.setUnitName(Utils.getFullyQualifedClassName(changedFile));
				
				// Number of test method and assertion statements.
				JavaStatementMeter testCounter = JavaStatementMeter.measureJavaFile(changedFile);
				if (testCounter.hasTest()) {
					
					// TODO shouldnt the measures be in another class?
					action.setCurrentSize(WindowListener.getActiveBufferSize());
					action.setCurrentMethods(testCounter.getNumOfMethods());
					action.setCurrentStatements(testCounter.getNumOfStatements());
					action.setCurrentTestMethods(testCounter.getNumOfTestMethods());
					action.setCurrentTestAssertions(testCounter.getNumOfTestMethods());
				}
				
				sensor.addAction(action);
				
			}

			
		}
		
		// visit the children
		return true; 

	}




}