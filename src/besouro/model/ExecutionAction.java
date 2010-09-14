package besouro.model;

import java.io.File;

import org.eclipse.core.resources.IResource;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class ExecutionAction extends ResourceAction {

	public ExecutionAction(Clock clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		System.out.println("canot assert execs yet");
		return null;
		// throw new RuntimeException("not implemented yet");
	}

}
