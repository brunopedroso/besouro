package athos.stream;
import java.net.URI;
import java.util.Map;


//TODO [int] rename sensor to ... printer? sender? register?
public class ConsoleLogger implements ActionOutputStream {

	public void addDevEvent(String name, URI resource,
			Map<String, String> map, String string2) {
		
		System.out.println("[dev event] " + name);
		System.out.println("\t" + resource);
		System.out.print("\t[");
		for (String key: map.keySet()) {
			System.out.println("\t " + key + " - " + map.get(key) );
			
		}
		System.out.println("\t]");
		System.out.println("\t" + string2);
		
	}

}
