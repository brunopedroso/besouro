package besouro.model;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.Rete;

import org.eclipse.core.resources.IResource;

public class ExecutionAction extends ResourceAction {

	public ExecutionAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		System.out.println("canot assert execs yet");
		return null;
	}

}
