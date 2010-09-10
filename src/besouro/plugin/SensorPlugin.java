package besouro.plugin;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;

import besouro.listeners.JUnitListener;
import besouro.listeners.JavaStructureChangeListener;
import besouro.listeners.ResourceChangeListener;
import besouro.listeners.WindowListener;
import besouro.stream.ActionOutputStream;
import besouro.stream.EpisodeClassifierStream;


//TODO  test filenames when code has packages

//TODO  do not recognize test edits before the class have at least one anotated method

//TODO [rule] ive extracted a method and it didnt classify it as a refactoring.
//		probably its because of the substantial thing that in my case registered the prod-edit action
//[action] 09/04/2010 10:02:35 Romans.java REFACTOR ADD METHOD {static int translateDigit(String)}
//[action] 09/04/2010 10:02:35 Romans.java PRODUCTION {MI=+1(2), SI=+1(2), , FI=+89(332)}
//[action] 09/04/2010 10:02:39 RomansTest.java TEST OK
//[action] 09/04/2010 10:02:39 RomansTest
//[episode] production 2

//TODO [rule] its recognizing test-first many times


//TODO [rule] if i write a test, fail, comment the test out to refactor and test pass, it recognizes as tstAdd2, tstAdd1, tstAdd2, tstAdd1 


//TODO [rule] one more case where test-first was recognized as prod1
// if we edit test without increasing assertions or methods, its not recognized as test-adition
// what is modeled through this 'production' rules?!

//TODO [clean] remove unusefull classes

public class SensorPlugin implements IStartup {

	public static SensorPlugin plugin;

	public static SensorPlugin getInstance() {
		return plugin;
	}

	private static ActionOutputStream stream;

	public SensorPlugin() {
		super();
		SensorPlugin.plugin = this;

		try {
			stream = new EpisodeClassifierStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void earlyStartup() {

		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new ResourceChangeListener(stream),
				IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeListener(
				stream));
		// DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new
		// LaunchListener(stream));
		JUnitCore.addTestRunListener(new JUnitListener(stream));

		WindowListener windowListener = new WindowListener(stream);

		IWorkbench workbench = Activator.getDefault().getWorkbench();
		workbench.addWindowListener(windowListener);

		// makes the installation of the windows' listeners in case we have
		// already an opened document
		windowListener.windowOpened(null);

	}

}

