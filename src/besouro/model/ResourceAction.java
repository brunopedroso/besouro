package besouro.model;

import java.util.Date;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;

/**
 * Defines generic software development actions, which is an independent
 * activity taken by software developer. For instance, unit test creation,
 * invocation, file edit, refactoring etc are all actions in Zorro system.
 * 
 * @author Hongbing Kou
 */
public abstract class ResourceAction extends Action {
	
	private String resource;

	public ResourceAction(Date clock, String resourceName) {
		super(clock);
		this.resource = resourceName;
	}

	public ResourceAction(StringTokenizer tok) {
		this(new Date(Long.parseLong(tok.nextToken())), tok.nextToken());
	}
	
	public String getResource() {
		return this.resource;
	}

	public String toString() {
		return super.toString() + " " + this.resource;
	}

	public String getActionValue() {
		return this.resource;
	}
}
