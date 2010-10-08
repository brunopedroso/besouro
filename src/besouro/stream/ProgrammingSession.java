package besouro.stream;

import java.io.File;

import besouro.model.Action;

public class ProgrammingSession implements ActionOutputStream {

	private FileStorageActionStream actionStorage;

	public ProgrammingSession(File basedir) {
		File actionsFile = new File(basedir, "actions.txt");
		actionStorage = new FileStorageActionStream(actionsFile);
	}

	public static ProgrammingSession newSession(File basedir) {
		ProgrammingSession session = new ProgrammingSession(basedir);
		
		return session;
	}

	public void addAction(Action action) {
		actionStorage.addAction(action);
	}

}
