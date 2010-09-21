package besouro.view;

//tasks
//TODO   (1) what is the right way of integrating view with listeners?   
//TODO   use tree instead of table
//TODO   show episodes
//TODO   use custom icons
//TODO   button to start session

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import besouro.model.Episode;
import besouro.plugin.EpisodeListener;
import besouro.plugin.ListenersSet;

public class EpisodeView extends ViewPart implements EpisodeListener {

	private TableViewer viewer;
	
	public static EpisodeView sharedInstance;

	public static EpisodeView getInstance() {
		return sharedInstance;
	}

	public EpisodeView() {
		super();
		EpisodeView.sharedInstance = this;
	}

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
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
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(getViewSite());
		
		IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
		Action action1 = new Action() {
			public void run() {
				Episode episode = new Episode();
				viewer.add(episode);
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		manager.add(action1);
		
		ListenersSet.getSingleton().addEpisodeListener(this);
	}

	@Override
	public void setFocus() {
		
	}

	public void add(Object o) {
		this.viewer.add(o);
	}

	public void episodeRecognized(Episode e) {
		viewer.add(e);
		
	}

}
