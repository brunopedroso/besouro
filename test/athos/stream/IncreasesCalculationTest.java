package athos.stream;

import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import athos.model.Action;
import athos.model.Clock;
import athos.model.EditAction;

// TODO [0]  increases - per file + metricas inicializadas no win-open?


public class IncreasesCalculationTest {
	
	private File file;
	private EpisodeClassifierStream stream;
	private EditAction action1;
	private EditAction action2;
	private Clock clock;

	@Before
	public void setup() throws Exception {

		stream = new EpisodeClassifierStream();

		Date referenceDate = new Date();
		
		file = new File("afile.any");
		clock = new Clock(referenceDate);
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		action2.setPreviousAction(action1);
		

	}
	
	
	
	@Test
	public void shouldLinkTheEditActions() throws Exception {
		
		Date referenceDate = new Date();
		
		// two brand new *unlinked* actions
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		
		stream.addAction(action1);
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(2, actions.size());
		Assert.assertEquals(actions.get(0), ((EditAction)actions.get(1)).getPrevisousAction());
		
	}
	
	
	@Test
	public void shouldLinkActionsPerFile() throws Exception {
		
		File anotherFile = new File("another.file");
		
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, anotherFile);
		EditAction action3 = new EditAction(clock, file);

		stream.addAction(action1);
		stream.addAction(action2);
		stream.addAction(action3);
		
		// should link action1 -> action3
		Assert.assertEquals(action1, action3.getPrevisousAction());
		
		// should not link aciton2, because its on other file
		Assert.assertNull(action2.getPrevisousAction());
	}
	
	
	//TODO   should calculate the first correctly
	// link the OpenAction with the original metrics ;-)
	//		we'll have to link all kinds of actions, i guess..
	
	
	
//	@Test
//	public void shouldCalculateTheDuration() throws Exception {
//		// We'r considering the 1st action with 0 duration
//		Assert.assertEquals(0, action1.getDuration());
//		Assert.assertEquals(4, action2.getDuration());
//	}
	
	@Test
	public void shouldCalculateFileIncreases() throws Exception {
		action1.setFileSize(50);
		action2.setFileSize(150);
		Assert.assertEquals(0, action1.getFileSizeIncrease());
		Assert.assertEquals(100, action2.getFileSizeIncrease());
	}
	
	@Test
	public void shouldCalculateMethodsIncreases() throws Exception {
		action1.setCurrentMethods(5);
		action2.setCurrentMethods(7);
		Assert.assertEquals(0, action1.getMethodIncrease());
		Assert.assertEquals(2, action2.getMethodIncrease());
	}

	@Test
	public void shouldCalculateStatementsIncreases() throws Exception {
		action1.setCurrentStatements(5);
		action2.setCurrentStatements(8);
		Assert.assertEquals(0, action1.getStatementIncrease());
		Assert.assertEquals(3, action2.getStatementIncrease());
	}
	
	@Test
	public void shouldCalculateTestAssertionsIncreases() throws Exception {
		action1.setCurrentTestAssertions(15);
		action2.setCurrentTestAssertions(19);
		Assert.assertEquals(0, action1.getTestAssertionIncrease());
		Assert.assertEquals(4, action2.getTestAssertionIncrease());
	}
	
	@Test
	public void shouldCalculateTestMethodsIncreases() throws Exception {
		action1.setCurrentTestMethods(11);
		action2.setCurrentTestMethods(19);
		Assert.assertEquals(0, action1.getTestMethodIncrease());
		Assert.assertEquals(8, action2.getTestMethodIncrease());
	}


	

}
