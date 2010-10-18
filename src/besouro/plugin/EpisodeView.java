package besouro.plugin;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.stream.EpisodeListener;

public class EpisodeView extends ViewPart implements EpisodeListener {

	public static final String ID = "besouro.view.EpisodeView";
	
	private TreeViewer viewer;
	private ProgrammingSession session;
	
	public static EpisodeView sharedInstance;

	private StartAction startAction;
	private StopAction stopAction;

	private Label statusLabel;

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
			setImageDescriptor(Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/nav_stop.gif"));
		}

		public void run() {
			if (session!=null) {
				session.close();
			}
			viewer.setInput(null);
			
			stopAction.setEnabled(false);
			startAction.setEnabled(true);
			
			statusLabel.setText("stopped");
			statusLabel.getParent().layout();

			
		}
	}

	private final class StartAction extends org.eclipse.jface.action.Action {
		
		public StartAction(){
			setText("Start");
			setImageDescriptor(Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/start_task.gif"));
		}
		
		public void run() {
			
			File projectRootDir = null;
			String projectName = null;
			
			IEditorPart editorPart = getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
			
			// try to get the project of the current editor, if it exists
			if(editorPart  != null) {
			    IFileEditorInput input = (IFileEditorInput)editorPart.getEditorInput();
			    IFile file = input.getFile();
			    IProject activeProject = file.getProject();
			    projectRootDir = activeProject.getLocation().toFile();
			    projectName = activeProject.getName();
			    
 
		    // else try to get it from the selected resource on package explorer
			} else {
				IViewPart part = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.eclipse.jdt.ui.PackageExplorer");
				PackageExplorerPart explorer = (PackageExplorerPart) part;
				
				StructuredSelection sel = (StructuredSelection)explorer.getTreeViewer().getSelection();
				
				if (!sel.isEmpty()) {
					JavaElement resource = (JavaElement)sel.getFirstElement();
					IJavaProject activeProject= resource.getJavaProject();
					projectRootDir = activeProject.getResource().getLocation().toFile();
					projectName = activeProject.getProject().getName();
				}
				
			}
			
			if (projectRootDir != null) {
				
				session = ProgrammingSession.newSession(projectRootDir);
				session.addEpisodeListeners(EpisodeView.this);
				viewer.setInput(session.getEpisodes());
				
				session.start();
				
				stopAction.setEnabled(true);
				startAction.setEnabled(false);
				
				statusLabel.setText("recording " + projectName);
				statusLabel.getParent().layout();
				
				
			} else {
				
				MessageDialog.openInformation(viewer.getControl().getShell(),
						"Warning", "Please, select a project or a resource in package explorer");

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
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode.png");
					
				} else if (episode.isTDD()) {
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_conformant.png");
					
				} else { // classified as non-conformant
					descriptor = Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_nonconformant.png");
					
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

		GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		
		GridData labelGridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		statusLabel = new Label(parent, SWT.NONE);
		statusLabel.setText("stopped              ");
		statusLabel.setLayoutData(labelGridData);

		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getControl().setLayoutData(gridData);
		
		viewer.getControl().setMenu(new DisagreementPopupMenu(viewer).getMenu());
		
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		
		startAction = new StartAction();
		stopAction = new StopAction();
		
		stopAction.setEnabled(false);
		startAction.setEnabled(true);
		
		manager.add(startAction);
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
