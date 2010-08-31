package athos.listeners;

import java.io.File;
import java.util.Date;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

import athos.model.Clock;
import athos.model.ExecutionAction;
import athos.stream.ActionOutputStream;

public class LaunchListener implements ILaunchesListener2 {

	private ActionOutputStream stream;

	public LaunchListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	public void launchesRemoved(ILaunch[] launches) {

	}

	public void launchesChanged(ILaunch[] launches) {

	}

	public void launchesAdded(ILaunch[] launches) {

	}

	public void launchesTerminated(ILaunch[] launches) {
		// for (ILaunch launch: launches) {
		// stream.addAction(new ExecutionAction(new Clock(new Date()), new
		// File("dunno yet")));
		// }
	}

}
