package besouro.stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.FileOpenedAction;



public class IncreasesCalculationTest {
	
	private IResource file;
	private EpisodeClassifierStream stream;
	private EditAction action1;
	private EditAction action2;
	private Date clock;

	@Before
	public void setup() throws Exception {

		stream = new EpisodeClassifierStream();

		file = mock(IResource.class);
		IPath path = mock(IPath.class);
		File aFile = mock(File.class);
		when(file.getName()).thenReturn("afile.any");
		when(file.getLocation()).thenReturn(path);
		when(path.toFile()).thenReturn(aFile);
		when(aFile.length()).thenReturn(33l);
		
		clock = new Date();
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		action2.setPreviousAction(action1);
		

	}
	
	
	
	@Test
	public void shouldLinkTheEditActions() throws Exception {
		
		// two brand new *unlinked* actions
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		
		stream.addAction(action1);
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(2, actions.size());
		Assert.assertEquals(actions.get(0), ((EditAction)actions.get(1)).getPreviousAction());
		
	}
	
	
	@Test
	public void shouldLinkActionsPerFile() throws Exception {
		
		IResource anotherFile = mock(IResource.class);
		when(anotherFile.getName()).thenReturn("anotherfile.any");
		
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		EditAction action3 = new EditAction(clock, anotherFile);
		EditAction action4 = new EditAction(clock, anotherFile);

		stream.addAction(action1);
		stream.addAction(action3);
		stream.addAction(action2);
		stream.addAction(action4);
		
		// should link action1 -> action2
		Assert.assertEquals(action1, action2.getPreviousAction());
		Assert.assertNull(action1.getPreviousAction());
		
		// should link action3 -> action4
		Assert.assertEquals(action3, action4.getPreviousAction());
		Assert.assertNull(action3.getPreviousAction());
	}
	
	
	
	
	
	@Test
	public void shouldCalculateFileIncreases() throws Exception {
		action1.setFileSize(50);
		action2.setFileSize(150);
		Assert.assertEquals(0, action1.getFileSizeIncrease());
		Assert.assertEquals(100, action2.getFileSizeIncrease());
	}
	
	@Test
	public void shouldCalculateMethodsIncreases() throws Exception {
		action1.setMethodsCount(5);
		action2.setMethodsCount(7);
		Assert.assertEquals(0, action1.getMethodIncrease());
		Assert.assertEquals(2, action2.getMethodIncrease());
	}

	@Test
	public void shouldCalculateStatementsIncreases() throws Exception {
		action1.setStatementsCount(5);
		action2.setStatementsCount(8);
		Assert.assertEquals(0, action1.getStatementIncrease());
		Assert.assertEquals(3, action2.getStatementIncrease());
	}
	
	@Test
	public void shouldCalculateTestAssertionsIncreases() throws Exception {
		action1.setTestAssertionsCount(15);
		action2.setTestAssertionsCount(19);
		Assert.assertEquals(0, action1.getTestAssertionIncrease());
		Assert.assertEquals(4, action2.getTestAssertionIncrease());
	}
	
	@Test
	public void shouldCalculateTestMethodsIncreases() throws Exception {
		action1.setTestMethodsCount(11);
		action2.setTestMethodsCount(19);
		Assert.assertEquals(0, action1.getTestMethodIncrease());
		Assert.assertEquals(8, action2.getTestMethodIncrease());
	}

	
	
	@Test
	public void shouldLinkEditActionsWithFileOpenActions() throws Exception {
		
		FileOpenedAction open = new FileOpenedAction(clock, file);
		
		// two brand new *unlinked* actions
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		
		IResource anotherFile = mock(IResource.class);
		when(anotherFile.getName()).thenReturn("anotherfile.any");

		
		EditAction action3 = new EditAction(clock, anotherFile);

		stream.addAction(open);
		stream.addAction(action1);
		stream.addAction(action3); // should not be linked
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(4, actions.size());
		Assert.assertEquals(open, action1.getPreviousAction());
		Assert.assertEquals(action1, action2.getPreviousAction());
		
		Assert.assertNull(action3.getPreviousAction());
		
	}
	
	@Test
	public void shouldCalculateFileIncreasesToPreviousFileOpenAction() throws Exception {
		
		FileOpenedAction open = new FileOpenedAction(clock, file);
		open.setFileSize(50);
		
		action2 = new EditAction(clock, file);
		action2.setFileSize(150);
		
		action2.setPreviousAction(open);
		
		Assert.assertEquals(100, action2.getFileSizeIncrease());
	}


	

}
