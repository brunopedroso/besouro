package besouro.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;

public class JUnitEventFactory {
	
	public static ITestRunSession createJunitSession(String sessionName, String fileName, Result result) {

		ITestRunSession session = mock(ITestRunSession.class);
		ITestSuiteElement testCase = mock(ITestSuiteElement.class);
		
		when(session.getTestRunName()).thenReturn(sessionName);
		when(session.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getSuiteTypeName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return session;
	}

	public static ITestRunSession createDeepJunitExecutionHierarchy(String fileName, Result result) {

		ITestRunSession srcFolderTest = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestSuiteElement testCase = mock(ITestSuiteElement.class);
		
		when(srcFolderTest.getChildren()).thenReturn(new ITestElement[]{packageTest});
		when(srcFolderTest.getTestRunName()).thenReturn(fileName);

		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getSuiteTypeName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return srcFolderTest;
	}
	
	public static ITestRunSession createTwoTestCases(String testFIle1, boolean passing1, String testFile2, boolean passing2) {
		
		ITestRunSession session = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestSuiteElement testCase1 = mock(ITestSuiteElement.class);
		ITestSuiteElement testCase2 = mock(ITestSuiteElement.class);
		
		when(session.getChildren()).thenReturn(new ITestElement[]{packageTest});
		when(session.getTestRunName()).thenReturn("TestSession");
		when(session.getTestResult(true)).thenReturn(Result.OK);
		
		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase1, testCase2});
		
		// same files
		when(testCase1.getTestResult(true)).thenReturn(passing1?Result.OK:Result.ERROR);
		when(testCase1.getSuiteTypeName()).thenReturn(testFIle1);
		
		when(testCase2.getTestResult(true)).thenReturn(passing2?Result.OK:Result.ERROR);
		when(testCase2.getSuiteTypeName()).thenReturn(testFile2);
		return session;
	}


}
