package besouro.view;

//tasks
//TODO   button to start session

//TODO   button to disagree with classification
//		 - icon diffs disagred classification

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.plugin.Activator;
import besouro.plugin.EpisodeListener;
import besouro.plugin.ListenersSet;

public class EpisodeView extends ViewPart implements EpisodeListener {

	public static final String ID = "besouro.view.EpisodeView";
	
	private TreeViewer viewer;
	
	public static EpisodeView sharedInstance;

	public static EpisodeView getInstance() {
		return sharedInstance;
	}

	public EpisodeView() {
		super();
		EpisodeView.sharedInstance = this;
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return ListenersSet.getSingleton().getEpisodes();
		}
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Episode){
				return ((Episode)parentElement).getActions().toArray();
			} else if (parentElement instanceof Action) {
				return ((Action)parentElement).getActionDetails().toArray();
				
			}
			return null;
		}
		public Object getParent(Object element) {
			return null;
		}
		public boolean hasChildren(Object element) {
			if (element instanceof Episode){
				return ((Episode)element).getActions().size()>0;
			} else if (element instanceof Action){
				return ((Action)element).getActionDetails().size()>0;
			}
			return false;
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			
			ImageDescriptor descriptor = null;
			
			if (obj instanceof Episode) {
				descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode.gif");
				
			} else if (obj instanceof Action) {
				descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/action.gif");
				
			} else if (obj instanceof String) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
				
			} else {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
				
			}

		   //obtain the cached image corresponding to the descriptor
		   Image image = descriptor.createImage();
		   return image;

		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
//		viewer.setSorter(new ViewerSorter());
		viewer.setInput(ListenersSet.getSingleton().getEpisodes());
		
		ListenersSet.getSingleton().addEpisodeListener(this);
		
	}

	@Override
	public void setFocus() {
	}

	public void episodeRecognized(final Episode e) {
		Display.getDefault().asyncExec(new Runnable() {
            public void run() {
            	// refreshes the entire list
            	viewer.refresh();
            }
         });
	}

}
