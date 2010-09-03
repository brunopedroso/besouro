package athos.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class JUnitEventFactory {
	
	public static ITestRunSession createJunitSession(String sessionName, String fileName, Result result) {

		ITestRunSession session = mock(ITestRunSession.class);
		when(session.getTestRunName()).thenReturn(sessionName);
		when(session.getTestResult(true)).thenReturn(result);
		
		ITestCaseElement testCase = mock(ITestCaseElement.class);
		when(testCase.getTestClassName()).thenReturn(fileName);
		when(session.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		return session;
	}

	public static ITestRunSession createDeepJunitExecutionHierarchy(String fileName, Result result) {

		ITestRunSession firstContainer = mock(ITestRunSession.class);
		ITestElementContainer secondContainer = mock(ITestElementContainer.class);
		ITestCaseElement testCase = mock(ITestCaseElement.class);
		
		when(firstContainer.getTestResult(true)).thenReturn(result);
		when(firstContainer.getChildren()).thenReturn(new ITestElement[]{secondContainer});

		when(secondContainer.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getTestClassName()).thenReturn(fileName);
		
		return firstContainer;
	}

}
