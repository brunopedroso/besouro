package besouro.stream;

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

import besouro.integration.TestFirstRecognition;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.model.ResourceAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;

public class FileStorageActionStreamTest {
	
	private File file;
	private FileStorageActionStream stream;

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
		stream = new FileStorageActionStream(file);
		Assert.assertTrue("file should have been created", file.exists());
	}
	
	@Test
	public void shouldNotCreateAFileIfItAlreadyExists() throws Exception {
		
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(new byte[]{1,2,3,4,5});
		fos.close();
		
		Assert.assertEquals("file should have been initialized", 5, file.length());
		
		stream = new FileStorageActionStream(file);
		
		Assert.assertEquals("file should remain untouched", 5, file.length());
		
	}
	
	@Test
	public void shouldStoreTheFirstActionInTheFile() throws Exception {
		
		Action a = new EditAction(new Date(), "anyFileName");
		
		stream = new FileStorageActionStream(file);
		stream.addAction(a);
		
		Assert.assertTrue("file should have been writen", file.length() > 0);
		
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		Assert.assertEquals("length should be one", 1, readActions.length);
		
	}
	
	@Test
	public void shouldReadNothingFromEmptyFile() throws Exception {
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		Assert.assertNull(readActions);
	}

	@Test
	public void shouldStoreMoreThanOneAction() throws Exception {
		
		String resource = "anyFileName";
		
		stream = new FileStorageActionStream(file);
		stream.addAction(new EditAction(new Date(),resource));
		stream.addAction(new EditAction(new Date(),resource));
		
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		Assert.assertEquals("should recover two action", 2, readActions.length);
		
	}

	@Test
	public void shouldStoreActionTypes() throws Exception {
		
		String resource = "anyFileName";
		
		stream = new FileStorageActionStream(file);
		stream.addAction(new EditAction(new Date(),resource));
		stream.addAction(new UnitTestCaseAction(new Date(),resource));
		stream.addAction(new UnitTestSessionAction(new Date(),resource));
		stream.addAction(new RefactoringAction(new Date(),resource));
		
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		Assert.assertTrue("should be an EditAction", readActions[0] instanceof EditAction);
		Assert.assertTrue("should be an UnitTestCaseAction", readActions[1] instanceof UnitTestCaseAction);
		Assert.assertTrue("should be an UnitSessionCaseAction but was a " + readActions[2].getClass().getSimpleName(), readActions[2] instanceof UnitTestSessionAction);
		Assert.assertTrue("should be a RefactoringAction", readActions[3] instanceof RefactoringAction);
		
	}
	
	@Test
	public void shouldStoreActionDate() throws Exception {
		
		stream = new FileStorageActionStream(file);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date clock = format.parse("01/01/2010 11:22:33");
		
		stream.addAction(new EditAction(clock,null));
		stream.addAction(new UnitTestCaseAction(clock,null));
		stream.addAction(new RefactoringAction(clock,null));
		
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		
		Assert.assertEquals("should preserve the date", clock, readActions[0].getClock());
		Assert.assertEquals("should preserve the date", clock, readActions[1].getClock());
		Assert.assertEquals("should preserve the date", clock, readActions[2].getClock());
		
	}
	
	@Test
	public void shouldStoreActionResourceName() throws Exception {
		
		stream = new FileStorageActionStream(file);
		
		String resource = "anyFileName";
		
		Date clock = new Date();
		stream.addAction(new EditAction(clock,resource));
		stream.addAction(new UnitTestCaseAction(clock,resource));
		stream.addAction(new RefactoringAction(clock,resource));
		
		Action[] readActions = FileStorageActionStream.loadFromFile(file);
		
		Assert.assertEquals("should preserve the date", resource, ((ResourceAction)readActions[0]).getResource());
		
	}
	
//	@Test
	public void shouldStoreSufficientActionsForTestFirtOneRecognition() throws Exception {
		
		EpisodeClassifierStream stream = new EpisodeClassifierStream(){
			@Override
			public void addAction(Action action) {
				
				file.delete();
				FileStorageActionStream fileStream = new FileStorageActionStream(file);
				
				// store and load back the action
				fileStream.addAction(action);
				action = FileStorageActionStream.loadFromFile(file)[0];
				
				// then make the usual work
				super.addAction(action);
			}
		};
		
		TestFirstRecognition integrationTest = new TestFirstRecognition();
		integrationTest.setup(stream);
		
		integrationTest.testFirstCategory1();
	}
}
