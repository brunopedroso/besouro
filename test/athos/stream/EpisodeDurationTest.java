package athos.stream;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import athos.model.Clock;
import athos.model.EditAction;
import athos.model.Episode;
import athos.model.UnitTestCaseAction;
import athos.model.UnitTestSessionAction;

public class EpisodeDurationTest {
	
	private EpisodeClassifierStream stream;
	
	private File file1;
	private File file2;
	
	private EditAction action1;
	private EditAction action2;
	private UnitTestCaseAction action3;
	private UnitTestSessionAction action4;


	@Before
	public void setup() throws Exception {

		stream = new EpisodeClassifierStream();

		Date referenceDate = new Date();
		
		file1 = new File("afile.any");
		file2 = new File("atestfile.any");
		
		action1 = new EditAction(new Clock(new Date(referenceDate.getTime()+10000)), file2);
		
		action2 = new EditAction(new Clock(new Date(referenceDate.getTime()+20000)), file1);
		
		action3 = new UnitTestCaseAction(new Clock(new Date(referenceDate.getTime()+30000)), file2);
		action3.setSuccessValue(true);
		
		action4 = new UnitTestSessionAction(new Clock(new Date(referenceDate.getTime()+35000)), file2);
		action4.setSuccessValue(true);

	}

	
	@Test
	public void durationIsTheTimeFromFirtToLastActionOfTheEpisode() {
		
		stream.addAction(action1);
		stream.addAction(action2);
		stream.addAction(action3);
		stream.addAction(action4);
		
		List<Episode> recognizedEpisodes = stream.getRecognizedEpisodes();
		Assert.assertEquals(1, recognizedEpisodes.size());
		Assert.assertEquals(25, recognizedEpisodes.get(0).getDuration());
		
	}
	
	
}
