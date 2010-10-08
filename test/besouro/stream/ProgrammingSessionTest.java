package besouro.stream;

import java.io.File;
import java.util.Date;

import org.junit.Test;

import besouro.model.FileOpenedAction;

import junit.framework.Assert;

public class ProgrammingSessionTest {

	
	//TODO  should store actions
	//TODO  should store episodes
	// 			should register EpisodeStorage in EpisodeClassifier
	
	//TODO  should allow EpisodeListeners and notify them
	//			view should listen
	
	//TODO  should register listener set
	//TODO  should register itself as actionListener of listener set
	//TODO  should unregister itself as actionListener in listenerSet on close
	//TODO  should close previous session on newSession

	@Test
	public void shouldStoreActions() {
		
		File basedir = new File("testDir");
		basedir.mkdir();
		
		File file = new File(basedir, "actions.txt");
		Assert.assertFalse("actionsFile should not exist upfront", file.exists());
		
		ProgrammingSession session = ProgrammingSession.newSession(basedir);
		
		session.addAction(new FileOpenedAction(new Date(), "afile"));
		
		Assert.assertTrue("should have created the file", file.exists());
		file.delete();
		
	}
	
}
