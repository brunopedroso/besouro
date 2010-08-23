package athos.stream;
import java.net.URI;
import java.util.Map;

import athos.model.Action;


public interface ActionOutputStream {

  static String DEVEVENT_EDIT = "Edit";
  static String SUBTYPE = "Subtype";
  static String UNIT_TYPE = "Unit-Type";
  static String UNIT_NAME = "Unit-Name";
  static String JAVA_EXT = ".java";
  static String FILE = "file";
  
  	//public void addAction(String string, URI resource, Map<String, String> devEventPMap, String string2);
	public void addAction(Action action);

}
