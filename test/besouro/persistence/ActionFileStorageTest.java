package besouro.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.views.navigator.RefactorActionGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import besouro.classification.zorro.ZorroEpisodeClassifierStream;
import besouro.integration.TestFirstRecognition;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.model.Action;
import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.model.FileOpenedAction;
import besouro.model.JavaFileAction;
import besouro.model.RefactoringAction;
import besouro.model.ResourceAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.persistence.ActionFileStorage;

public class ActionFileStorageTest {
	
	private File file;
	private ActionFileStorage stream;

	@Before
	public void setup() {
		file = new File("test/testActions.txt");
	}
	
	@After
	public void destroy() {
		file.delete();
	}
	
	@Test
	public void shouldCreateAFileIfNeeded() throws Exception {
		stream = new ActionFileStorage(file);
		Assert.assertTrue("file should have been created", file.exists());
	}
	
	@Test
	public void shouldNotCreateAFileIfItAlreadyExists() throws Exception {
		
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(new byte[]{1,2,3,4,5});
		fos.close();
		
		Assert.assertEquals("file should have been initialized", 5, file.length());
		
		stream = new ActionFileStorage(file);
		
		Assert.assertEquals("file should remain untouched", 5, file.length());
		
	}
	
	@Test
	public void shouldStoreTheFirstActionInTheFile() throws Exception {
		
		Action a = new EditAction(new Date(), "anyFileName");
		
		stream = new ActionFileStorage(file);
		stream.addAction(a);
		
		Assert.assertTrue("file should have been writen", file.length() > 0);
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		Assert.assertEquals("length should be one", 1, readActions.length);
		
	}
	
	@Test
	public void shouldReadNothingFromEmptyFile() throws Exception {
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		Assert.assertNull(readActions);
	}

	@Test
	public void shouldStoreMoreThanOneAction() throws Exception {
		
		String resource = "anyFileName";
		
		stream = new ActionFileStorage(file);
		stream.addAction(new EditAction(new Date(),resource));
		stream.addAction(new EditAction(new Date(),resource));
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		Assert.assertEquals("should recover two action", 2, readActions.length);
		
	}

	@Test
	public void shouldStoreActionTypes() throws Exception {
		
		String resource = "anyFileName";
		
		stream = new ActionFileStorage(file);
		stream.addAction(new EditAction(new Date(),resource));
		stream.addAction(new UnitTestCaseAction(new Date(),resource));
		stream.addAction(new UnitTestSessionAction(new Date(),resource));
		stream.addAction(new RefactoringAction(new Date(),resource));
		stream.addAction(new FileOpenedAction(new Date(),resource));
		stream.addAction(new CompilationAction(new Date(),resource));
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertTrue("should be an EditAction", readActions[0] instanceof EditAction);
		Assert.assertTrue("should be an UnitTestCaseAction", readActions[1] instanceof UnitTestCaseAction);
		Assert.assertTrue("should be an UnitSessionCaseAction but was a " + readActions[2].getClass().getSimpleName(), readActions[2] instanceof UnitTestSessionAction);
		Assert.assertTrue("should be a RefactoringAction", readActions[3] instanceof RefactoringAction);
		Assert.assertTrue("should be a FileOpenAction", readActions[4] instanceof FileOpenedAction);
		Assert.assertTrue("should be a CompilationAction", readActions[5] instanceof CompilationAction);
		
	}
	
	@Test
	public void shouldStoreActionDate() throws Exception {
		
		stream = new ActionFileStorage(file);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date clock = format.parse("01/01/2010 11:22:33");
		
		stream.addAction(new EditAction(clock,null));
		stream.addAction(new UnitTestCaseAction(clock,null));
		stream.addAction(new RefactoringAction(clock,null));
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertEquals("should preserve the date", clock, readActions[0].getClock());
		Assert.assertEquals("should preserve the date", clock, readActions[1].getClock());
		Assert.assertEquals("should preserve the date", clock, readActions[2].getClock());
		
	}
	
	@Test
	public void shouldStoreActionResourceName() throws Exception {
		
		stream = new ActionFileStorage(file);
		
		String resource = "anyFileName";
		
		Date clock = new Date();
		stream.addAction(new EditAction(clock,resource));
		stream.addAction(new UnitTestCaseAction(clock,resource));
		stream.addAction(new RefactoringAction(clock,resource));
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertEquals("should preserve the date", resource, ((ResourceAction)readActions[0]).getResource());
		
	}
	
	@Test
	public void shouldStoreJavaActionDetails() throws Exception {
		
		int filesize = 33;
		int methods = 34;
		int statements = 35;
		int testAssertions = 36;
		
		EditAction action = new EditAction(new Date(),"anyFileName");
		action.setFileSize(filesize);
		action.setMethodsCount(methods);
		action.setStatementsCount(statements);
		action.setTestAssertionsCount(testAssertions);
		
		stream = new ActionFileStorage(file);
		stream.addAction(action);
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertEquals("should load one action", 1, readActions.length);
		Assert.assertEquals("should preserve the date", filesize, ((JavaFileAction)readActions[0]).getFileSize());
		Assert.assertEquals("should preserve methods count", methods, ((JavaFileAction)readActions[0]).getMethodsCount());
		Assert.assertEquals("should preserve statements count", statements, ((JavaFileAction)readActions[0]).getStatementsCount());
		Assert.assertEquals("should preserve test statements count", testAssertions, ((JavaFileAction)readActions[0]).getTestAssertionsCount());
		
	}
	
	@Test
	public void shouldStoreRefactoringDetails() throws Exception {
		
		String name = "abc=>def";
		String op = "RENAME";
		String type = "AnyClass";
		
		RefactoringAction action = new RefactoringAction(new Date(),"anyFileName");
		action.setOperator(op);
		action.setSubjectName(name);
		action.setSubjectType(type);
		
		file.delete();
		stream = new ActionFileStorage(file);
		stream.addAction(action);
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertEquals("should load one action", 1, readActions.length);
		Assert.assertEquals("should preserve operator", op, ((RefactoringAction)readActions[0]).getOperator());
		Assert.assertEquals("should preserve SubjectName", name, ((RefactoringAction)readActions[0]).getSubjectName());
		Assert.assertEquals("should preserve the SubjectType", type, ((RefactoringAction)readActions[0]).getSubjectType());
		
	}
	
	@Test
	public void shouldStoreUnitTestResult() throws Exception {
		
		UnitTestCaseAction testCase = new UnitTestCaseAction(new Date(),"anyFileName");
		testCase.setSuccessValue(false);
		
		UnitTestSessionAction testSession = new UnitTestSessionAction(new Date(),"anyFileName");
		testSession.setSuccessValue(false);
		
		file.delete();
		stream = new ActionFileStorage(file);
		stream.addAction(testCase);
		stream.addAction(testSession);
		
		Action[] readActions = ActionFileStorage.loadFromFile(file);
		
		Assert.assertEquals("should load one action", 2, readActions.length);
		Assert.assertFalse("should preserve test result", ((UnitTestAction)readActions[0]).isSuccessful());
		Assert.assertFalse("should preserve test result", ((UnitTestAction)readActions[1]).isSuccessful());
		
	}
	
	@Test
	public void shouldStoreSufficientActionsForTestFirtOneRecognition() throws Exception {
		
		ZorroEpisodeClassifierStream stream = new ZorroEpisodeClassifierStream(){
			@Override
			public void addAction(Action action) {
				
				file.delete();
				ActionFileStorage fileStream = new ActionFileStorage(file);
				
				// store and load back the action
				fileStream.addAction(action);
				Action[] actionsLoaded = ActionFileStorage.loadFromFile(file);
				action = actionsLoaded[actionsLoaded.length-1];
				
				// then make the usual work
				super.addAction(action);
			}
		};
		
		TestFirstRecognition integrationTest = new TestFirstRecognition();
		integrationTest.setup(stream);
		
		integrationTest.testFirstCategory1();
	}
}
