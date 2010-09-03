package athos.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		Map<String, UnitTestAction> actions = createUnitTestAction(session);
		
		for (UnitTestAction action: actions.values()) {
//			action.setSuccessValue(session.getTestResult(true).equals(Result.OK));
			stream.addAction(action);
		}
	}

	private Map<String, UnitTestAction> createUnitTestAction(ITestElement element) {
		
		Map<String, UnitTestAction> list = new HashMap<String, UnitTestAction>();
		
		if (element instanceof ITestCaseElement) {
			ITestCaseElement testCase = (ITestCaseElement) element;
			String className = testCase.getTestClassName();
			UnitTestAction action = new UnitTestAction(new Clock(new Date()), new File(className));
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.put(className, action);
			
		} else if (element instanceof ITestElementContainer) {
			ITestElementContainer container = (ITestElementContainer) element; 
			for(ITestElement child: container.getChildren()){
				list.putAll(createUnitTestAction(child));
			}
		}
		
		return list;
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
