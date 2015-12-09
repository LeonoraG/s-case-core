package eu.scasefp7.eclipse.core.ui.handlers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.DynamicVariableResolver;

public class CreateServerHandler extends AbstractHandler implements ILaunchesListener2, IStreamListener {

	private Set<ILaunch> allLaunches = new HashSet<ILaunch>();
	ILaunchManager manager;
	String consoleOutput = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			manager = DebugPlugin.getDefault().getLaunchManager();
			manager.addLaunchListener(this);
			
			ILaunchConfigurationType type = manager
			  .getLaunchConfigurationType("org.eclipse.ui.externaltools.ProgramLaunchConfigurationType");
			
			ILaunchConfiguration[] configurations = manager
			  .getLaunchConfigurations(type);
			
			for (int i = 0; i < configurations.length; i++) {
				 ILaunchConfiguration configuration = configurations[i];
				 if (configuration.getName().equals("Gen doc")) {
					  configuration.delete();
					  break;
				 }
			}
			
			String cust_uuid = null, username = null, password = null, ssh_key = null;
			IStringVariable[] variables = VariablesPlugin.getDefault().getStringVariableManager().getVariables();
			
			for (int i = 0; i < variables.length; i++) {
					IDynamicVariable currVar = ((IDynamicVariable)variables[i]);
					DynamicVariableResolver resolver = new DynamicVariableResolver();
					String name = currVar.getName();
					
					if(name.equals("scase_fco_cust_uuid" ) )
						cust_uuid = resolver.resolveValue(currVar, name);
					
					if(name.equals( "scase_fco_cust_name"))
						username = resolver.resolveValue(currVar, name);
					
					if(name.equals("scase_fco_cust_pwd" ))
						password = resolver.resolveValue(currVar, name);
					
					if(name.equals("scase_fco_ssh_key" ))
						ssh_key = resolver.resolveValue(currVar, name);
				
			}
			
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance( null, "Gen doc");
			
			//replace with location of python.exe
			String location  ="C:\\Python27\\python.exe";
			
			//replace with location of script
			String arguments ="C:\\Users\\sc2015\\Desktop\\SCase\\YouREST-backend\\SCase-FCO\\createServer.py "; 
			
			arguments  += cust_uuid + " " + username + " " + password + " " + ssh_key; 
			String workingDir = "";
			
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_LOCATION", location);
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS", arguments);	
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY", workingDir);
			
			ILaunch launch = workingCopy.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());

			allLaunches.add(launch);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void launchesRemoved(ILaunch[] launches) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesAdded(ILaunch[] launches) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void launchesTerminated(ILaunch[] launches) {
		
		//Fetching serverUUID from scripts' output
		String serverUUID = "";
		int i = consoleOutput.indexOf("serverUUID");
		
		while(consoleOutput.charAt(i) != '"') ++i;
		++i;

		while(consoleOutput.charAt(i) != '"') ++i;
		++i;
		
		while(consoleOutput.charAt(i) != '"')
		{
			serverUUID+=consoleOutput.charAt(i);
			++i;
		}
		
		Activator plugin = Activator.getDefault();
		IPreferenceStore prefStore = plugin.getPreferenceStore();
		prefStore.setValue("scase_fco_server_uuid", serverUUID);
		
		for (ILaunch launch : launches) {
			   allLaunches.remove(launch);
			   Display.getDefault().asyncExec(new Runnable() {
			    @Override
			    public void run() { }
			   });

			 }
	}

	@Override
	public void launchesChanged(ILaunch[] launches) {
		
		   IProcess[]  processes = launches[0].getProcesses();
		   IStreamsProxy streams = processes[0].getStreamsProxy();

		   IStreamMonitor streamsOut = streams.getOutputStreamMonitor();
		   streamsOut.addListener(this);
	}

	@Override
	public void streamAppended(String text, IStreamMonitor monitor) {
		consoleOutput += text;
	}
}
