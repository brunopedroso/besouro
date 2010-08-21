package listeners;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;


/**
 * Provides IDocuementListener-implemented class to set an active buffer size when a document is
 * being edited.
 *
 * @author Takuya Yamashita
 * @version $Id: EclipseSensor.java,v 1.1.1.1 2005/10/20 23:56:56 johnson Exp $
 */
public class DocumentListenerAdapter implements IDocumentListener {
	
	private int activeBufferSize;

	public int getActiveBufferSize() {
		return activeBufferSize;
	}

	public DocumentListenerAdapter(){
	}
	
  /**
   * Do nothing right now. Just leave it due to implementation of IDocumentationListener.
   *
   * @param event An event triggered when a document is about to be changed.
   */
  public void documentAboutToBeChanged(DocumentEvent event) {
    // not supported in Eclipse Sensor.
  }

  /**
   * Provides the invocation of DeltaResource.setFileSize(long fileSize) method in order to get
   * buffer size. This method is called every document change since this EclipseSensorPlugin
   * instance was added to IDocumentLister. Since this method, the current buffer size of an
   * active file could be grabbed.
   *
   * @param event An event triggered when a document is changed.
   */
  public void documentChanged(DocumentEvent event) {
    activeBufferSize = event.getDocument().getLength();
  }
}