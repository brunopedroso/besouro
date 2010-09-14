package besouro.model;

import java.util.Date;

import org.eclipse.core.resources.IResource;

public class ExecutionAction extends ResourceAction {

	public ExecutionAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

}
