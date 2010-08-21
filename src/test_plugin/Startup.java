package test_plugin;
import listeners.JavaStructureChangeDetector;
import listeners.ResourceChangeAdapter;
import listeners.windows.WindowListener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;

import sensor.ISensor;
import sensor.Sensor;



//what do we have so far:
//- java structure changes
//- file open
//- resource changed with metrics (statements, methods, is_test? (is it working?),  ...)


//TODO - Junit listeners
//TODO - do we register compilation?
//TODO - do we register executions? (i'd like)
//TODO - compilation errors (not to much descriptions, i think)
//TODO - is the buffer size calculation correct?


//TODO - what window and part listeners get?
//TODO - rename of listeners
//TODO - unify projects: plugin and analyser


//TODO - rename project
//TODO - rename startup to plugin

public class Startup implements IStartup {
	
	public static Startup plugin;
	public static Startup getInstance() {
		return plugin;
	}
	

	private ISensor sensor = new Sensor();
	
	public Startup() {
		super();
		Startup.plugin = this;
		// Note that this is a non-standard way to initialize a singleton
		// instance due to Eclipse's auto startup nature.
	}
	
	
	public void earlyStartup() {

		System.out.println("Registering first-time listeners...");
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceChangeAdapter(sensor), IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeDetector(sensor));

		WindowListener windowListener = new WindowListener(sensor);
		Activator.getDefault().getWorkbench().addWindowListener(windowListener);
		
		// makes the installation of the windows' listeners
		windowListener.windowOpened(null);
		
	}

		
}
