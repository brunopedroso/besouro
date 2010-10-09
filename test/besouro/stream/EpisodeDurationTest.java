package besouro.stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.junit.Before;
import org.junit.Test;

import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.measure.JavaStatementMeter;
import besouro.model.EditAction;
import besouro.model.Episode;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.zorro.ZorroEpisodeClassifierStream;


public class EpisodeDurationTest {
	
	private ZorroEpisodeClassifierStream stream;
	
	private String file1;
	private String file2;
	
	private EditAction action1;
	private EditAction action2;
	private UnitTestCaseAction action3;
	private UnitTestSessionAction action4;
	
	private EditAction action5;
	private UnitTestCaseAction action6;
	private UnitTestSessionAction action7;


	@Before
	public void setup() throws Exception {

		stream = new ZorroEpisodeClassifierStream();
		
		// strange, i know
		JavaStatementMeter measurer = mock(JavaStatementMeter.class);
		JavaStatementMeter metric = mock(JavaStatementMeter.class);
		when(measurer.measureJavaFile(any(IFile.class))).thenReturn(metric);
		
		Date referenceDate = new Date();
		
		file1 = "afile.java";
		file2 = "atestfile.any";
		
		long time = 10000;
		
		action1 = new EditAction(new Date(referenceDate.getTime()+time), file2);
		
		time+=10000;
		action2 = new EditAction(new Date(referenceDate.getTime()+time), file1);
		
		time+=15000;
		action3 = new UnitTestCaseAction(new Date(referenceDate.getTime()+time), file2);
		action3.setSuccessValue(true);
		
		action4 = new UnitTestSessionAction(new Date(referenceDate.getTime()+time), file2);
		action4.setSuccessValue(true);

		// first episode ends here
		
		time+=5000;
		action5 = new EditAction(new Date(referenceDate.getTime()+time), file1);
		
		time+=6000;
		action6 = new UnitTestCaseAction(new Date(referenceDate.getTime()+time), file2);
		action6.setSuccessValue(true);
		
		action7 = new UnitTestSessionAction(new Date(referenceDate.getTime()+time), file2);
		action7.setSuccessValue(true);
		
	}

	
	@Test
	public void durationIShouldBeTimeFromFirtToLastActionOfTheEpisode() {
		
		stream.addAction(action1);
		stream.addAction(action2);
		stream.addAction(action3);
		stream.addAction(action4);
		
		List<Episode> recognizedEpisodes = stream.getTDDMeasure().getRecognizedEpisodes();
		Assert.assertEquals(1, recognizedEpisodes.size());
		Assert.assertEquals(25, recognizedEpisodes.get(0).getDuration());
		
	}
	
	@Test
	public void shouldConsiderThePreviousEpisodeLastAction() {
		
		stream.addAction(action1);
		stream.addAction(action2);
		stream.addAction(action3);
		stream.addAction(action4);
		// first episode ends here
		stream.addAction(action5);
		stream.addAction(action6);
		stream.addAction(action7);
		
		List<Episode> recognizedEpisodes = stream.getTDDMeasure().getRecognizedEpisodes();
		Assert.assertEquals(2, recognizedEpisodes.size());
		Assert.assertEquals(25, recognizedEpisodes.get(0).getDuration());
		
		// here! should consider the 5s between episodes 4 and 5
		Assert.assertEquals(11, recognizedEpisodes.get(1).getDuration());
		
	}
	
	
}
