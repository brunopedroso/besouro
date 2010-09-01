package athos.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class JUnitEventFactory {
	
	public static ITestRunSession createPassingSession() {
		ITestRunSession session = mock(ITestRunSession.class);
		when(session.getTestRunName()).thenReturn("MyTest.java");
		when(session.getTestResult(true)).thenReturn(Result.OK);
		return session;
	}
	
	public static ITestRunSession createFailingSession() {
		ITestRunSession session = mock(ITestRunSession.class);
		when(session.getTestRunName()).thenReturn("MyTest.java");
		when(session.getTestResult(true)).thenReturn(Result.ERROR);
		return session;
	}

}
