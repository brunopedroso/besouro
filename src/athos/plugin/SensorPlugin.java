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

//what do we have so far:
//- java structure changes
//- file open
//- resource changed with metrics (statements, methods, is_test? (is it working?),  ...)

//TODO z rename project to silver


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
