package crossing.e1.configurator.wizard;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import crossing.e1.configurator.ReadConfig;
import crossing.e1.featuremodel.clafer.ClaferModel;
import org.clafer.ast.*;

public class MyWizard extends Wizard {

	protected TaskSelectionPage taskSelectionPage;
	protected MyPageTwo two;
	private ClaferModel claferModel;


	public MyWizard() {
		super();
		setNeedsProgressMonitor(true);
		
	}

	@Override
	public String getWindowTitle() {
		return "Export My Data";
	}

	@Override
	public void addPages() {
		this.claferModel=new ClaferModel(new ReadConfig().getClaferPath());
		List<AstConcreteClafer> tasks = claferModel.getModel().getChildren();
		taskSelectionPage = new TaskSelectionPage(tasks,claferModel);
		two = new MyPageTwo();
		addPage(taskSelectionPage);
		addPage(two);
	}

	@Override
	public boolean performFinish() {
		// Print the result to the console
		System.out.println(taskSelectionPage.isPageComplete());
		System.out.println(two.getText1());

		return true;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
	    if (currentPage == taskSelectionPage) {
	    	// TODO
	    	if( ((TaskSelectionPage) currentPage).isSecure()){
//	    	two.setTitle(selectedTask.getName());
//	    	selectedTask.getChildren().forEach(child -> two.addField(child.getName()));
	    		
	    	addPage(two);
	    	}
	    	return two;
	    }else{
	    	return super.getNextPage(currentPage);
	    }
	}

}