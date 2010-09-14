package besouro.listeners.mock;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class JUnitEventFactory {
	
	public static ITestRunSession createJunitSession(String sessionName, String fileName, Result result) throws Exception {

		ITestRunSession session = mock(ITestRunSession.class);
		ITestSuiteElement testCase = mock(ITestSuiteElement.class);
		
		IJavaProject prj = createTestProject();
		when(session.getLaunchedProject()).thenReturn(prj);
		
		when(session.getTestRunName()).thenReturn(sessionName);
		when(session.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getSuiteTypeName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return session;
	}

	private static IJavaProject createTestProject() throws JavaModelException {
		
		IJavaProject project = mock(IJavaProject.class);
		
		when(project.findElement(any(IPath.class))).thenAnswer(new Answer<IJavaElement>() {
			
			// returns an element with a resource whose name is the toString of the argument ipath :-P
			public IJavaElement answer(InvocationOnMock invocation) throws Throwable {
				IJavaElement element = mock(IJavaElement.class);
				IResource resource = mock(IResource.class);
				when(element.getResource()).thenReturn(resource);
				when(resource.getName()).thenReturn(invocation.getArguments()[0].toString());
				return element;
			}
			
		});
		
		return project;
	}

	public static ITestRunSession createJunitSessionForSingleMethod(String sessionName, String classname, Result result) throws Exception {
		
		ITestRunSession session = mock(ITestRunSession.class);
		ITestCaseElement testCase = mock(ITestCaseElement.class);
		
		when(session.getTestRunName()).thenReturn(sessionName);
		when(session.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		IJavaProject prj = createTestProject();
		when(session.getLaunchedProject()).thenReturn(prj);
		
		when(testCase.getTestClassName()).thenReturn(classname);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return session;
	}
	
	public static ITestRunSession createDeepJunitExecutionHierarchy(String fileName, Result result) throws Exception {

		ITestRunSession srcFolderTest = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestSuiteElement testCase = mock(ITestSuiteElement.class);
		
		when(srcFolderTest.getChildren()).thenReturn(new ITestElement[]{packageTest});
		when(srcFolderTest.getTestRunName()).thenReturn(fileName);
		
		IJavaProject prj = createTestProject();
		when(srcFolderTest.getLaunchedProject()).thenReturn(prj);

		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase});
		
		when(testCase.getSuiteTypeName()).thenReturn(fileName);
		when(testCase.getTestResult(true)).thenReturn(result);
		
		return srcFolderTest;
	}
	
	public static ITestRunSession createTwoTestCases(String testFile1, boolean passing1, String testFile2, boolean passing2) throws Exception {
		
		ITestRunSession session = mock(ITestRunSession.class);
		ITestElementContainer packageTest = mock(ITestElementContainer.class);
		ITestSuiteElement testCase1 = mock(ITestSuiteElement.class);
		ITestSuiteElement testCase2 = mock(ITestSuiteElement.class);
		
		when(session.getChildren()).thenReturn(new ITestElement[]{packageTest});
		when(session.getTestRunName()).thenReturn("TestSession");
		when(session.getTestResult(true)).thenReturn(Result.OK);
		
		IJavaProject prj = createTestProject();
		when(session.getLaunchedProject()).thenReturn(prj);
		
		when(packageTest.getChildren()).thenReturn(new ITestElement[]{testCase1, testCase2});
		
		// same files
		when(testCase1.getTestResult(true)).thenReturn(passing1?Result.OK:Result.ERROR);
		when(testCase1.getSuiteTypeName()).thenReturn(testFile1);
		
		when(testCase2.getTestResult(true)).thenReturn(passing2?Result.OK:Result.ERROR);
		when(testCase2.getSuiteTypeName()).thenReturn(testFile2);
		return session;
	}


}
