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

public class ActionStreamTest {
	
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
	public void shouldCalculateTheDurationOfTheActions() throws Exception {
		
		//TODO [in] might be a better way to test it... (and to implement too)
		//should be the duration of the first action
		Thread.sleep(1000);
		
		Date referenceDate = new Date();
		
		EditAction action1 = new EditAction(new Clock(referenceDate), file);
		EditAction action2 = new EditAction(new Clock(new Date(referenceDate.getTime()+4000)), file);
		
		stream.addAction(action1);
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(2, actions.size());
		// We'r considering the 1st action with 0 duration
		Assert.assertEquals(0, actions.get(0).getDuration());
		Assert.assertEquals(4, actions.get(1).getDuration());
		
		
	}
	
	@Test
	public void shouldCalculateIncreasesInEditActions() throws Exception {
		
		EditAction action1 = new EditAction(clock, file);
		action1.setFileSize(50);
		
		EditAction action2 = new EditAction(clock, file);
		action2.setFileSize(150);
		
		stream.addAction(action1);
		stream.addAction(action2);
		
		List<Action> actions = stream.getActions();
		Assert.assertEquals(2, actions.size());
		
		// We'r considering the 1st action with 0 increase
		Assert.assertEquals(0, ((EditAction) actions.get(0)).getFileSizeIncrease());
		Assert.assertEquals(100, ((EditAction) actions.get(1)).getFileSizeIncrease());
		
		
	}

}
