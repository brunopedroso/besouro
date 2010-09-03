package athos.plugin;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;

import athos.listeners.JUnitListener;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.WindowListener;
import athos.stream.ActionOutputStream;
import athos.stream.EpisodeClassifierStream;



//TODO  do not recognize testAssertionAdd + junitFail + producion + junitPass as test first

//TODO  not recognizing test edits
//(MAIN::initial-fact)
//(MAIN::UnaryRefactorAction (index 2) (file "RomanNumeralsTest.java") (operation "ADD") (type "FIELD") (data "void fr"))
//(MAIN::UnaryRefactorAction (index 3) (file "RomanNumeralsTest.java") (operation "RENAME") (type "FIELD") (data "fr => void firstTest"))
//(MAIN::UnaryRefactorAction (index 4) (file "RomanNumeralsTest.java") (operation "RENAME") (type "METHOD") (data "firstTest => void firstTest()"))
//(MAIN::ProductionEditAction (index 5) (file "RomanNumeralsTest.java") (duration 0) (byteChange 37) (methodChange 1) (statementChange 0))
//(MAIN::UnitTestEditAction (index 6) (file "RomanNumeralsTest.java") (duration 0) (byteChange 5) (testChange 1) (assertionChange 0))

//TODO  make tostrings look easier to recognize


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

//TODO z rename project. Silver?