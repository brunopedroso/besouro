package besouro.plugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Menu;

import besouro.model.Episode;

public class DisagreementPopupMenu {

	private TreeViewer viewer;
	private MenuManager mngr;
	
	private Action nonConformAction;
	private Action conformAction;
	private Action testFirstAction;
	private Action testLastAction;
	private Action testAdditionAction;
	private Action refactoringAction;
	private Action productionAction;
	private Action regressionAction;

	public DisagreementPopupMenu(TreeViewer view) {
		this.viewer = view;
		
		mngr = new MenuManager();
		mngr.setRemoveAllWhenShown(true);
		
		createActions();
		
		mngr.addMenuListener(new IMenuListener() {
			
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				if (!selection.isEmpty()) {
					
					Episode episode = (Episode) selection.getFirstElement();
					
					if (episode.isTDD()) {
						mngr.add(nonConformAction);
					} else {
						mngr.add(conformAction);
					}
					
					if (!"test-first".equals(episode.getCategory())) {
						mngr.add(testFirstAction);
					}
					
					if (!"test-last".equals(episode.getCategory())) {
						mngr.add(testLastAction);
					}
					
					if (!"test-addition".equals(episode.getCategory())) {
						mngr.add(testAdditionAction);
					}
					
					if (!"refactoring".equals(episode.getCategory())) {
						mngr.add(refactoringAction);
					}
					
					if (!"production".equals(episode.getCategory())) {
						mngr.add(productionAction);
					}
					
					if (!"regression".equals(episode.getCategory())) {
						mngr.add(regressionAction);
					}
				}
			}
		});
		
	}
	
	private void createActions() {
		nonConformAction = new Action(){
			public void run() {
				System.out.println("non confrmant: " + viewer.getSelection());
			}
		};
		nonConformAction.setText("non-conformant");
		nonConformAction.setImageDescriptor(Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_nonconformant.png"));
		
		conformAction = new Action(){
			public void run() {
				System.out.println("confrmant: " + viewer.getSelection());
			}
		};
		conformAction.setText("conformant");
		conformAction.setImageDescriptor(Activator.imageDescriptorFromPlugin("besouro_plugin", "icons/episode_conformant.png"));
		
		testFirstAction  = new Action(){
			public void run() {
				System.out.println("test-fisrt: " + viewer.getSelection());
			}
		};
		testFirstAction.setText("test-first");
		
		testLastAction = new Action(){
			public void run() {
				System.out.println("test-last: " + viewer.getSelection());
			}
		};
		testLastAction.setText("test-last");
		
		testAdditionAction = new Action(){
			public void run() {
				System.out.println("test-addition: " + viewer.getSelection());
			}
		};
		testAdditionAction.setText("test-addition");
		
		refactoringAction = new Action(){
			public void run() {
				System.out.println("refactoring: " + viewer.getSelection());
			}
		};
		refactoringAction.setText("refactoring");
		
		productionAction = new Action(){
			public void run() {
				System.out.println("production" + viewer.getSelection());
			}
		};
		productionAction.setText("production");

		regressionAction = new Action(){
			public void run() {
				System.out.println("regression" + viewer.getSelection());
			}
		};
		regressionAction.setText("regression");
		
	}
	
	public Menu getMenu() {
		return mngr.createContextMenu(viewer.getControl());
	}
	
}
