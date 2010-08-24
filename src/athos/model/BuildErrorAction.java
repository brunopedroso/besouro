package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class BuildErrorAction extends FileAction {

	private String errorMessage;
	
	public BuildErrorAction(Clock clock, File workspaceFile, String errorMessage) {
		super(clock, workspaceFile);
		this.errorMessage = errorMessage;
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
//		throw new RuntimeException("do we need a build error action?");
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + errorMessage;
	}

}
