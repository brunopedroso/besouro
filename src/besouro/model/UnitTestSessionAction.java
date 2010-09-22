package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IResource;

public class UnitTestSessionAction extends UnitTestAction {

	public UnitTestSessionAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public String toString() {
		return new SimpleDateFormat("HH:mm:ss").format(getClock()) + " TEST SESSION - " + (this.isSuccessful()?"OK":"FAIL") + " " + getResource();
	}
	
}
