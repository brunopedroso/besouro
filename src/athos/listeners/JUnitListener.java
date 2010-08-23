package athos.listeners;

import java.io.File;
import java.util.Date;

import org.eclipse.jdt.internal.junit.model.ITestRunListener2;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

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
		UnitTestAction action = new UnitTestAction(new Clock(new Date()), new File(session.getTestRunName()));
		Result testResult = session.getTestResult(true);
		action.setSuccessValue(testResult.toString().equals("OK"));
		stream.addAction(action);
//		print(session);
	}

//	private void print(ITestElement session) {
//
//		System.out.println(session);
//		
//		if (session instanceof ITestSuiteElement) {
//			ITestSuiteElement suite = (ITestSuiteElement) session;
//			
//			for (ITestElement test : suite.getChildren()) {
//				print(test);
//			}
//			
//		}else if (session instanceof ITestRunSession) {
//			ITestRunSession suite = (ITestRunSession) session;
//			
//			for (ITestElement test : suite.getChildren()) {
//				print(test);
//			}
//		}
//		
//	}

	
}
