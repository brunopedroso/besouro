package listeners;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;


public class JUnitListener extends TestRunListener {

	//TODO [int] que dados precisamos do junit?
	
	@Override
	public void sessionFinished(ITestRunSession session) {
		Map map = new HashMap<String, String>();
		print(session);
	}

	private void print(ITestElement session) {

		System.out.println(session);
		
		if (session instanceof ITestSuiteElement) {
			ITestSuiteElement suite = (ITestSuiteElement) session;
			
			for (ITestElement test : suite.getChildren()) {
				print(test);
			}
			
		}else if (session instanceof ITestRunSession) {
			ITestRunSession suite = (ITestRunSession) session;
			
			for (ITestElement test : suite.getChildren()) {
				print(test);
			}
		}
		
	}

	
}
