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
import besouro.model.UnitTestCaseAction;

public class FileStorageActionStream implements ActionOutputStream {

	private File file;
	private FileWriter writer;

	public FileStorageActionStream(File f) {
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
			
			if (action instanceof EditAction)
				writer.write("EditAction");
			
			else if (action instanceof UnitTestCaseAction)
				writer.write("UnitTestCaseAction");
			
			else
				writer.write("RefactoringAction");
			
			writer.write("\n");
			writer.flush();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Action[] loadFromFile(File file) {
		
		if (!file.exists()) {
			return new Action[0];
		}
		
		try {
			
			List<Action> list = new ArrayList<Action>();
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			for (int i=0 ; i<file.length() ; i++) {
				
				String a = reader.readLine();
				
				if (a != null) {
					
					StringTokenizer tok = new StringTokenizer(a," ");
					String token1 = tok.nextToken();
					
					if (token1.equals("EditAction"))
						list.add(new EditAction(new Date(), null));
					
					else if (token1.equals("UnitTestCaseAction"))
						list.add(new UnitTestCaseAction(new Date(), null));
					
					else if (token1.equals("RefactoringAction"))
						list.add(new RefactoringAction(new Date(), null));
					
				}
				
			}
			
			return list.toArray(new Action[list.size()]);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
