package besouro.view;

import java.io.File;

import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.plugin.Activator;
import besouro.plugin.EpisodeListener;
import besouro.stream.ProgrammingSession;

public class EpisodeView extends ViewPart implements EpisodeListener {

	public static final String ID = "besouro.view.EpisodeView";
	
	private TreeViewer viewer;
	private ProgrammingSession session;
	
	public static EpisodeView sharedInstance;

	public static EpisodeView getInstance() {
		return sharedInstance;
	}

	public EpisodeView() {
		super();
		EpisodeView.sharedInstance = this;
	}

	private final class StopAction extends org.eclipse.jface.action.Action {
		
		public StopAction(){
			setText("Stop");
		}

		public void run() {
			if (session!=null) {
				session.close();
			}
			viewer.setInput(null);
		}
	}

	private final class StartAction extends org.eclipse.jface.action.Action {
		
		public StartAction(){
			setText("Start");
		}
		
		public void run() {
			IViewPart part = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.eclipse.jdt.ui.PackageExplorer");
			PackageExplorerPart explorer = (PackageExplorerPart) part;
			
		    StructuredSelection sel = (StructuredSelection)explorer.getTreeViewer().getSelection();
		    
		    if (sel.isEmpty()) {
		    	MessageDialog.openInformation(viewer.getControl().getShell(),
		    			"Warning", "Please, select a project or a resource in package explorer");
		    	
		    } else {
		    	JavaElement resource = (JavaElement)sel.getFirstElement();
		    	File projectRootDir = resource.getJavaProject().getResource().getLocation().toFile();
		    	
		    	File besouroDir = new File(projectRootDir, ".besouro");
		    	besouroDir.mkdir();
		    	
		    	session = ProgrammingSession.newSession(besouroDir);
		    	session.addEpisodeListeners(EpisodeView.this);
		    	viewer.setInput(session.getEpisodes());
		    }
		}
	}

	class ViewContentProvider implements ITreeContentProvider {
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
		public void dispose() {}
		
		public Object[] getElements(Object parent) {
			if (session!=null) {
				return session.getEpisodes();
			} else {
				return null;
			}
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
				Episode episode = (Episode)obj;
				if (episode.isTDD()==null) {
					// unclassified
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode.gif");
					
				} else if (episode.isTDD()) {
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_conformant.gif");
					
				} else { // classified as non-conformant
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_nonconformant.gif");
					
				}
				
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
//		viewer.setInput(ListenersSet.getSingleton().getEpisodeClassifier().getEpisodes());
		
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		manager.add(new StartAction());
		manager.add(new StopAction());
		
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
