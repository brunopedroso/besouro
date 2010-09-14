package besouro.model;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.Rete;

import org.eclipse.core.resources.IResource;

public class UnitTestSessionAction extends UnitTestAction {

	public UnitTestSessionAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

	@Override
	public String toString() {
		return getClock() + " TEST SESSION - " + (this.isSuccessful()?"OK":"FAIL") + " " + getResource();
	}
	
}
