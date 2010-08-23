package athos.listeners;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import athos.stream.ActionOutputStream;


public class Utils {

	

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
		if (file.exists() && file.getName().endsWith(ActionOutputStream.JAVA_EXT)) {
			ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(file);
			String className = compilationUnit.getElementName();
			if (className.endsWith(ActionOutputStream.JAVA_EXT)) {
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
