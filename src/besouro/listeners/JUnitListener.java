package besouro.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
		for (UnitTestAction action: getTestFileActions(session)) {
			stream.addAction(action);
			isSuccessfull &= action.isSuccessful();
		}
		
		// registers the session action. It breake the episode, but doesnt count on the classification
		UnitTestSessionAction action = new UnitTestSessionAction(new Clock(new Date()), new File(session.getTestRunName()));
		action.setSuccessValue(isSuccessfull);
		stream.addAction(action);
		
	}

	private Collection<UnitTestCaseAction> getTestFileActions(ITestElement session) {
		
		List<UnitTestCaseAction> list = new ArrayList<UnitTestCaseAction>();
		
		if (session instanceof ITestSuiteElement) {
			
			ITestSuiteElement testCase = (ITestSuiteElement) session;
			String className = testCase.getSuiteTypeName() + ".java";
			
			UnitTestCaseAction action = new UnitTestCaseAction(new Clock(new Date()), new File(className));
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.add(action);
			
		} else if (session instanceof ITestCaseElement) {
			
			// will reach this case only when user executes a single test method
			ITestCaseElement testCase = (ITestCaseElement) session;
			
			UnitTestCaseAction action = new UnitTestCaseAction(new Clock(new Date()), new File(testCase.getTestClassName()+".java"));
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.add(action);
						
		} else if (session instanceof ITestElementContainer) {
			ITestElementContainer container = (ITestElementContainer) session; 
			for(ITestElement child: container.getChildren()){
				list.addAll(getTestFileActions(child));
			}
		}
		
		
		return list;
		
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
