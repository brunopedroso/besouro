package listeners;

import java.util.ArrayList;

import athos.model.Action;
import athos.stream.ActionOutputStream;

public class FakeActionStream implements ActionOutputStream {
	
	private final ArrayList<Action> generatedActions;

	public FakeActionStream(ArrayList<Action> generatedActions) {
		this.generatedActions = generatedActions;
	}

	public void addAction(Action action) {
		generatedActions.add(action);
		
	}
}