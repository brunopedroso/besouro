package athos.integration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import athos.listeners.JavaStatementMeter;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.JavaElementsFactory;
import athos.listeners.mock.ResourceFactory;
import athos.model.Action;
import athos.model.Clock;
import athos.model.EditAction;
import athos.stream.EpisodeClassifierStream;

public class IntegrationTest {

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


	private EditAction createEditActionWithFileSize(int fileSize) {
		Clock clock = new Clock(new Date());
		File file = new File("afile.any");
		EditAction action1 = new EditAction(clock, file);
		action1.setFileSize(fileSize);
		return action1;
	}

	
	@Test 
	public void testTDDEpisodeCategory1() throws Exception {
	
		
		EpisodeClassifierStream stream = new EpisodeClassifierStream();
		JavaStructureChangeListener javaListener = new JavaStructureChangeListener(stream);
		ResourceChangeListener resourceListener = new ResourceChangeListener(stream);
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		javaListener.setTestCounter(meter);
		resourceListener.setTestCounter(meter);
		
		ElementChangedEvent event = JavaElementsFactory.createAddMethodAction();
		javaListener.elementChanged(event);
		// unaryAction.setSubjectName("void testEquilateral()");

		
		IResourceChangeEvent resourceEvent = ResourceFactory.createTestEditAction();
		resourceListener.resourceChanged(resourceEvent);
//	    EditAction editAction = new EditAction(clock, testFile, 123);

		//
//	    // Compile error on test
//	    CompilationAction compilationAction = new CompilationAction(clock, testFile);
//	    compilationAction.setErrorMessage("Unknown data type");
//	    compilationAction.assertJessFact(3, engine);
//
//	    // Work on production code
//	    editAction = new EditAction(clock, productionFile, 200);
//	    editAction.setIsTestEdit(false);
//	    editAction.setFileSizeIncrease(10);
//	    editAction.assertJessFact(4, engine);
//
//	    // Unit test failue
//	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
//	    unitTestAction.setFailureMessage("Failed to import");
//	    unitTestAction.assertJessFact(5, engine);
//	 
//	    // Edit on prodction code
//	    editAction = new EditAction(clock, productionFile, 199);
//	    editAction.setIsTestEdit(false);
//	    editAction.setFileSizeIncrease(30);
//	    editAction.assertJessFact(6, engine);
//
//	    // Unit test pass
//	    unitTestAction = new UnitTestAction(clock, testFile); 
//	    unitTestAction.assertJessFact(7, engine);

		  
	  }
	
}
