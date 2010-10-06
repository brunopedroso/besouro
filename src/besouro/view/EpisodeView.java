package besouro.view;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.Openable;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.plugin.Activator;
import besouro.plugin.EpisodeListener;
import besouro.plugin.ListenersSet;
import besouro.stream.EpisodeClassifierStream;

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
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
		public void dispose() {}
		
		public Object[] getElements(Object parent) {
			EpisodeClassifierStream episodeClassifier = ListenersSet.getSingleton().getEpisodeClassifier();
			if (episodeClassifier!=null) {
				return episodeClassifier.getEpisodes();
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
		org.eclipse.jface.action.Action startAction = new org.eclipse.jface.action.Action() {
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
	            	
	            	File actionsFile = new File(besouroDir, "actions.txt");
	            	File episodesFile = new File(besouroDir, "episodes.txt");
	            	
//	            	ListenersSet.getSingleton().setActionsFile(actionsFile);
//	            	ListenersSet.getSingleton().setEpisodesFile(episodesFile);
	            	ListenersSet.getSingleton().start();
	            	ListenersSet.getSingleton().getEpisodeClassifier().addEpisodeListener(EpisodeView.this);
	            	viewer.setInput(ListenersSet.getSingleton().getEpisodeClassifier().getEpisodes());
	            }
			}
		};
		
		startAction.setText("Start");
		manager.add(startAction);
		
		org.eclipse.jface.action.Action stopAction = new org.eclipse.jface.action.Action() {
			public void run() {
				EpisodeClassifierStream episodeClassifier = ListenersSet.getSingleton().getEpisodeClassifier();
				if (episodeClassifier!=null) {
					episodeClassifier.removeEpisodeListener(EpisodeView.this);
				}
				viewer.setInput(null);
			}
		};
		
		stopAction.setText("Stop");
		manager.add(stopAction);
		
		
		
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
