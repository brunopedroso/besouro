package besouro.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

//TODO [data] is it necessary to agregate continuous edits in documents?

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
//if we edit test without increasing assertions or methods, its not recognized as test-adition
//what is modeled through this 'production' rules?!



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
