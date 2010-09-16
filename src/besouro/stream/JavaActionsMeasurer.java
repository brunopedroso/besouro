package besouro.stream;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import besouro.listeners.JavaStatementMeter;
import besouro.model.JavaFileAction;

public class JavaActionsMeasurer {

	private JavaStatementMeter javaFileMeasurer = new JavaStatementMeter();
	private Map<String, JavaFileAction> previousEditActionPerFile = new HashMap<String, JavaFileAction>();
	
	/**
	 * for testing purposes
	 */
	public void setJavaFileMeasurer(JavaStatementMeter javaFileMeasurer) {
		this.javaFileMeasurer = javaFileMeasurer;
	}

	
	public void measureJavaActions(JavaFileAction javaAction) {
		
		linkActions(javaAction);
		JavaStatementMeter metrics = javaFileMeasurer.measureJavaFile((IFile) javaAction.getResource());
		
		javaAction.setFileSize((int) javaAction.getResource().getLocation().toFile().length());
		javaAction.setIsTestEdit(metrics.isTest());
		javaAction.setMethodsCount(metrics.getNumOfMethods());
		javaAction.setStatementsCount(metrics.getNumOfStatements());
		javaAction.setTestMethodsCount(metrics.getNumOfTestMethods());
		javaAction.setTestAssertionsCount(metrics.getNumOfTestAssertions());
			
	}

	private void linkActions(JavaFileAction linkedAction) {
		String path = linkedAction.getResource().getName();
		linkedAction.setPreviousAction(previousEditActionPerFile.get(path)); // 1st time will be null, I know...
		previousEditActionPerFile.put(path, linkedAction);
		
	}

	
}
