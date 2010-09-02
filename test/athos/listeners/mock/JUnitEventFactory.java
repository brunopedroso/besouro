package athos.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class JUnitEventFactory {
	
	public static ITestRunSession createPassingSession(String filename) {
		ITestRunSession session = mock(ITestRunSession.class);
		when(session.getTestRunName()).thenReturn(filename);
		when(session.getTestResult(true)).thenReturn(Result.OK);
		return session;
	}
	
	public static ITestRunSession createFailingSession(String filename) {
		ITestRunSession session = mock(ITestRunSession.class);
		when(session.getTestRunName()).thenReturn(filename);
		when(session.getTestResult(true)).thenReturn(Result.ERROR);
		return session;
	}

}
