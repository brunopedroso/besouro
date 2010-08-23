package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class ExecutionAction extends FileAction{

	public ExecutionAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		throw new RuntimeException("not implemented yet");
	}

	
	
}
