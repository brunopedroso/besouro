package besouro.plugin;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IWorkbench;

import besouro.listeners.JUnitListener;
import besouro.listeners.JavaStructureChangeListener;
import besouro.listeners.ResourceChangeListener;
import besouro.listeners.WindowListener;
import besouro.stream.EpisodeClassifierStream;

public class ListenersSet {

	private static ListenersSet singleton;
	
	public static ListenersSet getSingleton() {
		if (singleton==null) {
			singleton = new ListenersSet();
			singleton.registerEclipseListeners();
		}
		return singleton;
	}

	
	private EpisodeClassifierStream episodeClassifier;
	
	private ListenersSet() {
		try {
			episodeClassifier = new EpisodeClassifierStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	private void registerEclipseListeners() {

		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceChangeListener(episodeClassifier), IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeListener(episodeClassifier));
		
		// DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener(stream));
		JUnitCore.addTestRunListener(new JUnitListener(episodeClassifier));

		WindowListener windowListener = new WindowListener(episodeClassifier);

		IWorkbench workbench = Activator.getDefault().getWorkbench();
		workbench.addWindowListener(windowListener);

		// registers open events for the already opened files
		windowListener.windowOpened(null);

	}
	
	public void addEpisodeListener(EpisodeListener listener) {
		episodeClassifier.addEpisodeListener(listener);
	}

	public Object[] getEpisodes() {
		return episodeClassifier.getEpisodes();
	}
	
}
