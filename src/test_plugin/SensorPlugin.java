package test_plugin;
import listeners.JavaStructureChangeListener;
import listeners.ResourceChangeListener;
import listeners.JUnitListener;
import listeners.WindowListener;

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

import sensor.ISensor;
import sensor.Sensor;



//what do we have so far:
//- java structure changes
//- file open
//- resource changed with metrics (statements, methods, is_test? (is it working?),  ...)


//TODO [log] do we register compilation?
//TODO [log] do we register executions? (i'd like)
//TODO [log] are we collecting timestamps?

//TODO [int] unify projects: plugin and analyser
//TODO [int] rename project

public class SensorPlugin implements IStartup {
	
	public static SensorPlugin plugin;
	public static SensorPlugin getInstance() {
		return plugin;
	}
	

	private ISensor sensor = new Sensor();
	
	public SensorPlugin() {
		super();
		SensorPlugin.plugin = this;
		// Note that this is a non-standard way to initialize a singleton
		// instance due to Eclipse's auto startup nature.
	}
	
	
	public void earlyStartup() {

		System.out.println("Registering first-time listeners...");
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceChangeListener(sensor), IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeListener(sensor));

		WindowListener windowListener = new WindowListener(sensor);
		Activator.getDefault().getWorkbench().addWindowListener(windowListener);
		
		JUnitCore.addTestRunListener(new JUnitListener());
		
		// makes the installation of the windows' listeners
		windowListener.windowOpened(null);
		
		
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new ILaunchesListener2() {
			
			public void launchesRemoved(ILaunch[] launches) {
				// TODO Auto-generated method stub
				
			}
			
			public void launchesChanged(ILaunch[] launches) {
				// TODO Auto-generated method stub
				
			}
			
			public void launchesAdded(ILaunch[] launches) {
				// TODO Auto-generated method stub
				
			}
			
			public void launchesTerminated(ILaunch[] launches) {
				
				for (ILaunch launch: launches) {
					System.out.println("LAUNCH TERMINATED!" + launch.getLaunchConfiguration().getName());
				}				
			}
			
		});
		
		
	}

		
}
