package besouro.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

//TODO [data] is it necessary to agregate continuous edits in documents?


//TODO [rule]  if i write a test, fail, comment the test out to refactor and test pass, it recognizes as tstAdd2, tstAdd1, tstAdd2, tstAdd1 



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "test_plugin";

	// The shared instance
	private static Activator plugin;
	public static Activator getDefault() {
		return plugin;
	}
	

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
