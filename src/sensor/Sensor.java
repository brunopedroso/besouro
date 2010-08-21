package sensor;
import java.net.URI;
import java.util.Map;


//TODO [rec] rename sensor to ... printer? sender? register?
public class Sensor implements ISensor {

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
