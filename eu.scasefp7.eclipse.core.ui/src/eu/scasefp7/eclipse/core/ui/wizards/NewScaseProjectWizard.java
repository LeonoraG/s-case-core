package eu.scasefp7.eclipse.core.ui.wizards;

import java.net.URI;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import eu.scasefp7.eclipse.core.ui.preferences.ProjectDomainPropertyAndWizardPage;
import eu.scasefp7.eclipse.core.ui.preferences.ProjectDomainWizardPage;

/**
 * @author Leonora Gašpar
 * @author Marin Orlić
 *
 */
public class NewScaseProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private WizardNewProjectCreationPage _pageOne;
	private ProjectDomainWizardPage _pageTwo;
	private static final String PAGE_NAME = "Project name";
	private static final String WIZARD_NAME = "New S-CASE Project"; 

	/**
	 * Constructs the wizard
	 */
	public NewScaseProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addPages() {
	    super.addPages();
	 
	    _pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
	    _pageOne.setTitle("Create a S-CASE Project");
	    _pageOne.setDescription("Enter project name.");
	    
	    _pageTwo = new ProjectDomainWizardPage();
//	    _pageTwo = new PropertyWizardPage("S-Case project domain");
	    _pageTwo.setTitle("Select project domain");
	    
	    addPage(_pageOne);
	    addPage(_pageTwo);
	}
	
	@Override
	public boolean performFinish() {
		
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    IResource res = ScaseProjectSupport.createProject(name, location);
//	    int k;
//	    org.eclipse.swt.widgets.Label domainLabel = _pageTwo.getDomainLabel();
//	    DomainEntry de = (DomainEntry) domainLabel.getData();
//	    if (de == null)
//	    	k = -1;
//	    else
//	    	k =  de.getId();
//	    try {
//			res.setPersistentProperty(new QualifiedName("", "eu.scasefp7.eclipse.core.projectDomain"), Integer.toString(k));
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	    //_pageTwo.setElement(res);
	    _pageTwo.performOk();
	    
		return true;
	}
	

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		
	}

}