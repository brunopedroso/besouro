package besouro.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;

import besouro.model.Clock;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.stream.ActionOutputStream;


public class JUnitListener extends TestRunListener {

	private ActionOutputStream stream;

	public JUnitListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		
		boolean isSuccessfull = true;
		for (UnitTestAction action: getTestFileActions(session, session.getLaunchedProject())) {
			stream.addAction(action);
			isSuccessfull &= action.isSuccessful();
		}
		
		IResource res = findTestResource(session.getLaunchedProject(), session.getTestRunName());
		
		// registers the session action. It breake the episode, but doesnt count on the classification
		UnitTestSessionAction action = new UnitTestSessionAction(new Clock(new Date()), res);
		action.setSuccessValue(isSuccessfull);
		stream.addAction(action);
		
	}

	private Collection<UnitTestCaseAction> getTestFileActions(ITestElement session, IJavaProject project) {
		
		List<UnitTestCaseAction> list = new ArrayList<UnitTestCaseAction>();
		
		if (session instanceof ITestSuiteElement) {
			
			ITestSuiteElement testCase = (ITestSuiteElement) session;

			IResource res = findTestResource(project, testCase.getSuiteTypeName());
			
			UnitTestCaseAction action = new UnitTestCaseAction(new Clock(new Date()), res);
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.add(action);
			
		} else if (session instanceof ITestCaseElement) {
			
			ITestCaseElement testCase = (ITestCaseElement) session;
			
			IResource res = findTestResource(project, testCase.getTestClassName());
				
			// will reach this case only when user executes a single test method
			
			UnitTestCaseAction action = new UnitTestCaseAction(new Clock(new Date()),res);
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.add(action);
						
		} else if (session instanceof ITestElementContainer) {
			ITestElementContainer container = (ITestElementContainer) session; 
			for(ITestElement child: container.getChildren()){
				list.addAll(getTestFileActions(child, project));
			}
		}
		
		
		return list;
		
	}

	private IResource findTestResource(IJavaProject project, String className) {
		IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
		try {
			return project.findElement(path).getResource();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}


//	 private void print(ITestElement session) {
//		
//		
//		 if (session instanceof ITestSuiteElement) {
//			 
//			 ITestSuiteElement suite = (ITestSuiteElement) session;
//			 
//		 } else if (session instanceof ITestElementContainer) {
//			 
//			 ITestElementContainer suite = (ITestElementContainer) session;
//			
//			 for (ITestElement test : suite.getChildren()) {
//				 print(test);
//			 }
//			
//		 }
//		 
//	 }

}
