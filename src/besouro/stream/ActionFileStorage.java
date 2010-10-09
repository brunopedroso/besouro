package besouro.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.model.ResourceAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;

public class ActionFileStorage implements ActionOutputStream {

	private File file;
	private FileWriter writer;

	public ActionFileStorage(File f) {
		try {
			
			this.file = f;
			
			if (!file.exists()) {
				file.createNewFile();
			}
			
			writer = new FileWriter(file,true);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addAction(Action action) {
		try {
			
			writer.write(action.toString());
			writer.write("\n");
			writer.flush();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	public static Action[] loadFromFile(File file) {
		
		if (!file.exists()) {
			return null;
		}
		
		try {
			
			List<Action> list = new ArrayList<Action>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				
				if (line != null) {
					list.add(Action.fromString(line));
				}
				
			};
			
			return list.toArray(new Action[list.size()]);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
