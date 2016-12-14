package besouro.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import besouro.model.Jacoco;
import besouro.stream.JacocoOutputStream;

public class JacocoFileStorage implements JacocoOutputStream {

	private File file;
	private FileWriter writer;

	public JacocoFileStorage(File f) {
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

	public void addAction(Jacoco jacoco) {
		try {

			writer.write(jacoco.toString());
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
}