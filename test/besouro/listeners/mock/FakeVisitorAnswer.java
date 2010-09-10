package besouro.listeners.mock;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings("rawtypes")
public class FakeVisitorAnswer implements Answer {
	
	private IResourceDelta delta;

	public FakeVisitorAnswer(IResourceDelta delta) {
		this.delta = delta;
	}

	public Object answer(InvocationOnMock invocation) throws Throwable {
		IResourceDeltaVisitor visitor = (IResourceDeltaVisitor) invocation.getArguments()[0];
		visitor.visit(delta);
		return null;
	}
}
