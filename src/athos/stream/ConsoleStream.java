package athos.stream;
import athos.model.Action;


public class ConsoleStream implements ActionOutputStream {

	public void addAction(Action action){
		System.out.println("[action] " + action);
	}

}
