package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.PropertyPage;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

public class ProjectFoldersPreferencePage extends PropertyPage {
	String modelsPath = "";
	String outputPath = "";
	String reqPath = "";
	String comPath = "";

	@Override
	protected Control createContents(Composite parent) {
		IProject project = null;
		IAdaptable element = getElement();
		if(element instanceof IProject) {
		    project = (IProject)element;
		} else {
		    Object resource = element.getAdapter(IResource.class);
		    if (resource instanceof IProject) {
		    	project = (IProject)resource;
		    } else {
		    	Activator.log("Unable to read project properties.", null);
		    }
		}
		try {
			modelsPath = project.getPersistentProperty(new QualifiedName("","eu.scasefp7.eclipse.core.ui.modelsFolder"));
			outputPath = project.getPersistentProperty(new QualifiedName("","eu.scasefp7.eclipse.core.ui.outputFolder"));
			reqPath = project.getPersistentProperty(new QualifiedName("","eu.scasefp7.eclipse.core.ui.rqsFolder"));
			comPath = project.getPersistentProperty(new QualifiedName("","eu.scasefp7.eclipse.core.ui.compFolder"));
		} catch (CoreException e4) {
			Activator.log("Unable to read project properties.", null);
		}

		initializeDialogUnits(parent);

		final Composite composite= new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.BEGINNING));
        
        Label a = new Label(composite, SWT.NULL);
        a.setText ("The models are stored in folder:");
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.horizontalSpan = 3;
        a.setLayoutData(gridData1);
        
        Label label = new Label(composite, SWT.NULL);
        Text models = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
       
        final Button button = new Button(composite, SWT.PUSH);
        
        label.setText("Models folder path: ");
        models.setText(modelsPath);
        models.setEnabled(false);
        button.setText("Browse...");
        models.setLayoutData(gridData);


        IContainer root = ResourcesPlugin.getWorkspace().getRoot();
        button.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null) {
            		   resPath = (Path ) res[0];
            		   models.setText(resPath.toString()); 
            		   modelsPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        
        //Empty row
        Label e1 = new Label(composite, SWT.NULL);
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL;
        gridData11.horizontalSpan = 3;
        e1.setLayoutData(gridData11);
        
        Label a2 = new Label(composite, SWT.NULL);
        a2.setText ("The generated code is stored in folder:");
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.horizontalSpan = 3;
        a2.setLayoutData(gridData2);
        
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = SWT.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        Label label2 = new Label(composite, SWT.NULL);
        Text output = new Text(composite,  SWT.BORDER);
        final Button button2 = new Button(composite, SWT.PUSH);
        
        label2.setText("Output folder path: ");
        output.setText(outputPath);
        output.setEnabled(false);
        button2.setText("Browse...");
        output.setLayoutData(gridData3);
        
        button2.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null) {
            		   resPath = (Path ) res[0];
            		   output.setText(resPath.toString()); 
            		   outputPath = resPath.toString();
            	   }
                break;
              }
            }

          });
        
        //Empty row
        Label e2 = new Label(composite, SWT.NULL);
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = GridData.FILL;
        gridData21.horizontalSpan = 3;
        e2.setLayoutData(gridData21);
        
        Label a3 = new Label(composite, SWT.NULL);
        a3.setText ("The requirements are stored in folder:");
        
        GridData gridData31 = new GridData();
        gridData31.horizontalAlignment = GridData.FILL;
        gridData31.horizontalSpan = 3;
        a3.setLayoutData(gridData31);
        
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = SWT.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        Label label3 = new Label(composite, SWT.NULL);
        Text requirements = new Text(composite, SWT.BORDER);
        final Button button3 = new Button(composite, SWT.PUSH);
        
        label3.setText("Requirements folder path: ");
        requirements.setText(reqPath);
        requirements.setEnabled(false);
        button3.setText("Browse...");
        requirements.setLayoutData(gridData4);
        
        button3.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   requirements.setText(resPath.toString()); 
            		   reqPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        
        //Empty row
        Label e3 = new Label(composite, SWT.NULL);
        GridData gridData32 = new GridData();
        gridData32.horizontalAlignment = GridData.FILL;
        gridData32.horizontalSpan = 3;
        e3.setLayoutData(gridData32);
        
        Label a4 = new Label(composite, SWT.NULL);
        a4.setText ("The storyboards used for composition are in folder:");
        GridData gridData41 = new GridData();
        gridData41.horizontalAlignment = GridData.FILL;
        gridData41.horizontalSpan = 3;
        a4.setLayoutData(gridData41);
        
        Label label4 = new Label(composite, SWT.NULL);
        Text compositions = new Text(composite, SWT.BORDER);
        final Button button4 = new Button(composite, SWT.PUSH);
        
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = SWT.FILL;
        gridData6.grabExcessHorizontalSpace = true;
        
        label4.setText("Compositions folder path: ");
        compositions.setText(comPath);
        compositions.setEnabled(false);
        button4.setText("Browse...");
        compositions.setLayoutData(gridData6);
        
        button4.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   compositions.setText(resPath.toString()); 
            		   comPath = resPath.toString();
            	   }

                break;
              }
            }

          });

		//setControl(composite);
		return composite;
	}
	@Override
	public boolean performOk() {
		IProject project = null;
		IAdaptable element = getElement();
		if(element instanceof IProject) {
		    project = (IProject)element;
		} else {
		    Object resource = element.getAdapter(IResource.class);
		    if (resource instanceof IProject) {
		    	project = (IProject)resource;
		    } else {
		    	Activator.log("Unable to set project properties.", null);
		    }
		}
		try{
			IFolder models   = project.getFolder("models");
			IFolder output   = project.getFolder("output");
			IFolder requirements = project.getFolder("requirements");
			IFolder compositions = project.getFolder("compositions");
						
			if(modelsPath.equals(""))
				modelsPath = models.getProjectRelativePath().toPortableString();
			if(outputPath.equals(""))
				outputPath = output.getProjectRelativePath().toPortableString();
			if(reqPath.equals(""))
				reqPath = requirements.getProjectRelativePath().toPortableString();
			if(comPath.equals(""))
				comPath = compositions.getProjectRelativePath().toPortableString();
			
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), modelsPath);
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), outputPath);
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), reqPath);
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), comPath);
			
		} catch  (CoreException e){
			Activator.log("Unable to set project properties.", e);
		}
	
		return super.performOk();
	}

}
