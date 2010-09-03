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
		ITestCaseElement testCase = mock(ITestCaseElement.class);
		
		when(session.getTestRunName()).thenReturn(sessionName);
		when(session.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getTestClassName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return session;
	}

	public static ITestRunSession createDeepJunitExecutionHierarchy(String fileName, Result result) {

		ITestRunSession srcFolderTest = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestCaseElement testCase = mock(ITestCaseElement.class);
		
		when(srcFolderTest.getChildren()).thenReturn(new ITestElement[]{packageTest});

		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getTestClassName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return srcFolderTest;
	}
	
	public static ITestRunSession createTwoTestCases(String testFIle1, boolean passing1, String testFile2, boolean passing2) {
		ITestRunSession sesison = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestCaseElement testCase1 = mock(ITestCaseElement.class);
		ITestCaseElement testCase2 = mock(ITestCaseElement.class);
		
		when(sesison.getChildren()).thenReturn(new ITestElement[]{packageTest});
		
		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase1, testCase2});
		
		// same files
		when(testCase1.getTestResult(true)).thenReturn(Result.ERROR);
		when(testCase1.getTestClassName()).thenReturn(testFIle1);
		
		when(testCase2.getTestResult(true)).thenReturn(Result.OK);
		when(testCase2.getTestClassName()).thenReturn(testFile2);
		return sesison;
	}


}
