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

public class IncreasesCalculationTest {
	
	private File file;
	private EpisodeClassifierStream stream;
	private Clock clock;

	@Before
	public void setup() throws Exception {
		file = new File("afile.any");
		stream = new EpisodeClassifierStream();
		clock = new Clock(new Date());
	}
	
	@Test
	public void shouldLinkTheEditActions() throws Exception {
		
		Date referenceDate = new Date();
		
		EditAction action1 = new EditAction(new Clock(referenceDate), file);
		EditAction action2 = new EditAction(new Clock(new Date(referenceDate.getTime()+4000)), file);
		
		stream.addAction(action1);
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(2, actions.size());
		Assert.assertEquals(actions.get(0), ((EditAction)actions.get(1)).getPrevisousAction());
		
	}
	
	
	@Test
	public void shouldCalculateTheDurationOfTheActions() throws Exception {
		
		Date referenceDate = new Date();
		
		EditAction action1 = new EditAction(new Clock(referenceDate), file);
		EditAction action2 = new EditAction(new Clock(new Date(referenceDate.getTime()+4000)), file);
		
		action2.setPreviousAction(action1);
		
		// We'r considering the 1st action with 0 duration
		Assert.assertEquals(0, action1.getDuration());
		Assert.assertEquals(4, action2.getDuration());
		
		
	}
	
	@Test
	public void shouldCalculateIncreasesInEditActions() throws Exception {
		
		EditAction action1 = new EditAction(clock, file);
		action1.setFileSize(50);
		
		EditAction action2 = new EditAction(clock, file);
		action2.setFileSize(150);
		
		action2.setPreviousAction(action1);
		
		Assert.assertEquals(0, action1.getFileSizeIncrease());
		Assert.assertEquals(100, action2.getFileSizeIncrease());
		
		
	}

}
