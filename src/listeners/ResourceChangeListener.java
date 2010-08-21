package listeners;
import java.net.URI;
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

import sensor.ISensor;

/**
 * Provides "Open Project, "Close Project", and "Save File" events. Note that
 * this implementing class uses Visitor pattern so that key point to gather
 * these event information is inside the visitor method which is implemented
 * from <code>IResourceDeltaVisitor</code> class.
 * 
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class ResourceChangeListener implements IResourceChangeListener,
		IResourceDeltaVisitor {

	private ISensor sensor;

	public ResourceChangeListener(ISensor s) {
		this.sensor = s;
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

		// TODO z lots of dependencies here
		// If there is compilation problem with the current java file then send
		// out the activity data.
		// if ((flag & IResourceDelta.MARKERS) != 0 &&
		// EclipseSensor.this.buildErrorSensor != null) {
		// EclipseSensor.this.buildErrorSensor.findBuildProblem(delta);
		// }

		// :RESOLVED: 26 May 2003
		// Note that the 147456 enumeration type is not listed in the
		// IResourceDelta static filed.
		// However, its number is generated when Project is either opened or
		// closed so that
		// it is checked in the logical condition.
		if (resource instanceof IProject
				&& ((flag == IResourceDelta.OPEN) || (flag == 147456))) {
			
			IProject project = resource.getProject();
			String projectName = project.getName();
			URI projectResoruce = project.getFile(".project").getLocationURI();

			Map<String, String> keyValueMap = new HashMap<String, String>();
			keyValueMap.put(ISensor.UNIT_TYPE, "project");
			keyValueMap.put(ISensor.UNIT_NAME, projectName);

			if (((IProject) resource).isOpen()) {
				
				keyValueMap.put(ISensor.SUBTYPE, "Open");
				sensor.addDevEvent(ISensor.DEVEVENT_EDIT, projectResoruce, keyValueMap, projectResoruce.toString());
				
			} else {
				
				keyValueMap.put(ISensor.SUBTYPE, "Close");
				sensor.addDevEvent(ISensor.DEVEVENT_EDIT, projectResoruce, keyValueMap, projectResoruce.toString());
				
			}
			return false;
		}
		
		
		//TODO analyse and simplify
		
		if ((kind == IResourceDelta.CHANGED)
				&& (flag == IResourceDelta.CONTENT)
				&& resource instanceof IFile) {
			
			if (resource.getLocation().toString().endsWith(ISensor.JAVA_EXT)) {
				IFile file = (IFile) resource;

				Map<String, String> keyValueMap = new HashMap<String, String>();

				keyValueMap.put("Language", "java");
				keyValueMap.put(ISensor.UNIT_TYPE, ISensor.FILE);

				// Fully qualified class path
				String className = getFullyQualifedClassName(file);
				keyValueMap.put("Class-Name", className);

				// Size of the file in buffer
				String bufferSize = String.valueOf(WindowListener.getActiveBufferSize());
				keyValueMap.put("Current-Size", bufferSize);

				// Measure java file.
				JavaStatementMeter testCounter = JavaStatementMeter.measureJavaFile(file);
				
				//TODO test: two times?
//				testCounter = measureJavaFile(file);
				
				
				//TODO is it countins test methods corectly?
				
				String methodCount = String.valueOf(testCounter.getNumOfMethods());
				keyValueMap.put("Current-Methods", methodCount);

				String statementCount = String.valueOf(testCounter.getNumOfStatements());
				keyValueMap.put("Current-Statements", statementCount);

				// Number of test method and assertion statements.
				if (testCounter.hasTest()) {
					String testMethodCount = String.valueOf(testCounter.getNumOfTestMethods());
					keyValueMap.put("Current-Test-Methods", testMethodCount);

					String testAssertionCount = String.valueOf(testCounter.getNumOfTestAssertions());
					keyValueMap.put("Current-Test-Assertions",testAssertionCount);
				}

				// EclipseSensor.this.eclipseSensorShell.doCommand("Activity",
				// activityData);
				// Construct message to display on Eclipse status bar.
				URI fileResource = file.getLocationURI();

				StringBuffer message = new StringBuffer("Save File");
				message.append(" : ").append(extractFileName(fileResource));

				keyValueMap.put(ISensor.SUBTYPE, "Save");
				sensor.addDevEvent(ISensor.DEVEVENT_EDIT, fileResource,keyValueMap, message.toString());
			}

			// Visit the children because it is not necessary for the saving
			// file to be only one file.
			return true;
		}
		return true; // visit the children
	}


	//TODO extract utilities

	/**
	 * Extracts file name from a file resource URI.
	 * 
	 * @param fileResource
	 *            File name path.
	 * @return File name.
	 */
	public static String extractFileName(URI fileResource) {
		
		String fileStirng = fileResource.toString();
		
		if (fileStirng != null && fileStirng.indexOf('/') > 0) {
			
			return fileStirng.substring(fileStirng.lastIndexOf('/') + 1);
			
		} else {
			
			return fileStirng;
			
		}
	}

	/**
	 * Gets the fully qualified class name for an active file. For example, its
	 * value is foo.bar.Baz.
	 * 
	 * @param file
	 *            Get fully qualified class file.
	 * @return The fully qualified class name. For example,foo.bar.Baz.
	 */
	public static String getFullyQualifedClassName(IFile file) {
		String fullClassName = "";
		if (file.exists() && file.getName().endsWith(ISensor.JAVA_EXT)) {
			ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(file);
			String className = compilationUnit.getElementName();
			if (className.endsWith(ISensor.JAVA_EXT)) {
				className = className.substring(0, className.length() - 5);
			}

			try {
				
				IPackageDeclaration[] packageDeclarations = compilationUnit.getPackageDeclarations();
				// Should only have one package declaration
				if (packageDeclarations == null || packageDeclarations.length == 0) {
					fullClassName = className;
				} else {
					fullClassName = packageDeclarations[0].getElementName() + '.' + className;
				}
				
			} catch (JavaModelException e) {
				// This exception will be thrown if user is working on a Java
				// but did not open
				// it with "Java Perspective". Thus, the Java Model does not
				// exist to parse
				// Java files. So we only log out exception while Eclipse's Java
				// Perspective
				// exits.
				if (!e.isDoesNotExist()) {
					// TODO z what to do with exception?
					// EclipseSensorPlugin.getDefault().log(file.getName(), e);
				}
			}
		}

		return fullClassName;
	}

}