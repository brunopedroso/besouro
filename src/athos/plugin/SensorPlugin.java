package athos.plugin;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.internal.core.LaunchManager;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;

import athos.listeners.JUnitListener;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.LaunchListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.WindowListener;
import athos.stream.ActionOutputStream;
import athos.stream.ConsoleStream;
import athos.stream.EpisodeClassifierStream;

//what do we have so far:
//- java structure changes
//- file open
//- resource changed with metrics (statements, methods, is_test? (is it working?),  ...)


//TODO z rename project to silver

//TODO [0] automate integration tests (listeners -> classification)

//TODO z do we need all that File and URI stuff?


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
		
		// Note that this is a non-standard way to initialize a singleton
		// instance due to Eclipse's auto startup nature.
	}
	
	
	public void earlyStartup() {

//		System.out.println("Registering first-time listeners...");
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceChangeListener(stream), IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeListener(stream));
//		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener(stream));
		JUnitCore.addTestRunListener(new JUnitListener(stream));

		WindowListener windowListener = new WindowListener(stream);
		Activator.getDefault().getWorkbench().addWindowListener(windowListener);

		// makes the installation of the windows' listeners
		windowListener.windowOpened(null);
		
		
		
		
	}

		
}
