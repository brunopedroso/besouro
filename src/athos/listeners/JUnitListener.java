package athos.listeners;

import java.io.File;
import java.util.Date;

import org.eclipse.jdt.internal.junit.model.ITestSessionListener;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;

import athos.model.Clock;
import athos.model.UnitTestAction;
import athos.stream.ActionOutputStream;

public class JUnitListener extends TestRunListener {

	private ActionOutputStream stream;

	public JUnitListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		
		String className = getTestClassName(session);
		
		File file = new File(className);
		
		UnitTestAction action = new UnitTestAction(new Clock(new Date()), file);
		Result testResult = session.getTestResult(true);
		action.setSuccessValue(testResult.equals(Result.OK));
		stream.addAction(action);
		// print(session);
	}

	private String getTestClassName(ITestElement element) {
		
		if (element instanceof ITestCaseElement) {
			ITestCaseElement testCase = (ITestCaseElement) element;
			return testCase.getTestClassName();
			
		} else if (element instanceof ITestElementContainer) {
			ITestElementContainer session = (ITestElementContainer) element; 
			return getTestClassName(session.getChildren()[0]);
		}
		
		return null;
	}
	
	// private void print(ITestElement session) {
	//
	// System.out.println(session);
	//
	// if (session instanceof ITestSuiteElement) {
	// ITestSuiteElement suite = (ITestSuiteElement) session;
	//
	// for (ITestElement test : suite.getChildren()) {
	// print(test);
	// }
	//
	// }else if (session instanceof ITestRunSession) {
	// ITestRunSession suite = (ITestRunSession) session;
	//
	// for (ITestElement test : suite.getChildren()) {
	// print(test);
	// }
	// }
	//
	// }

}
