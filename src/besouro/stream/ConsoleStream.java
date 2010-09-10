package besouro.stream;

import besouro.model.Action;

public class ConsoleStream implements ActionOutputStream {

	public void addAction(Action action) {
		System.out.println("[action] " + action);
	}

}
