/*
 * Created on 14.2.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.beanshell.presentation;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

import bsh.EvalError;
import bsh.TargetError;

import com.idega.block.beanshell.business.BSHEngine;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;

/**
 * WORK IN PROGRESS, see todo list in eclipse
 * This Block is both an editor for a <a href="http://www.beanshell.org">Beanshell</a> script and a bsh script runner.<br>
 * Add the BeanShellScript to a page and it will run the script (if one is set) when its main method is called.<br>
 * You will have all the request parameters available to you in your script and the current instance of IWContext.<br>
 * An example usage might be to handle Form validation, adding presentation object to a page if certain parameters are in the request, debugging code, live code testing etc.<br>
 * The possibilities are endless!<br>
 * Object you can use in your script without initializing them are: iwc (IWContext), any request parameter by their name (e.g. ib_page)<br>
 * Examples: <br>
 * 1. "print(iwc.getParameter("X"));" - Would print to the stdout the value of the request parameter X <br>
 * 2. "return new com.idega.block.news.presentation.NewsReader()" - Would add a News block to the page where the script was added <br>
 * 3. "import com.idega.presentation.*; <br>Table table = new Table(10,10);<br>table.setColor("red");<br>return  table;" - Adds a red table to the page where the script was added <br>
 * 
 * For now a script from the live editor can only be run as the super admin for security reasons. This will be a role later on...
 * 
 * See <a href="http://www.beanshell.org">www.beanshell.org</a> for a tutorial in the Beanshell scripting language and some examples.<br>
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 0.7
 */
public class BeanShellScript extends Block {


	private static final String IW_BUNDLE_IDENTIFIER="com.idega.block.beanshell";
	private boolean showEditor = false;
	private String scriptString;
	private static final String PARAM_SCRIPT_STRING = "bsh_scr_str";
	private ICFile icFileScript;
	private IWBundle bundle;
	private String scriptInBundleFileName;
	private String fileNameWithPath;
	
	
	public BeanShellScript() {
		super();
	}
	
	
	public void main(IWContext iwc) throws RemoteException{
		//TODO Eiki make safe to execute from parameter
		//TODO Eiki allow multiple scripts per page and support ordering scripts
		//TODO Eiki Put editor in a window and support syntax coloring
		BSHEngine engine;
		try {
			engine = (BSHEngine) IBOLookup.getServiceInstance(iwc,BSHEngine.class);
		
			if(scriptString==null && iwc.isSuperAdmin()){
				scriptString = iwc.getParameter(PARAM_SCRIPT_STRING);
			}
			
			if(showEditor){
				addEditorAndRunScript(iwc,engine);
			}
			else{
				Object obj = runScript(iwc,engine);
				if(obj!=null){
					if(obj instanceof PresentationObject){
						add((PresentationObject)obj);
					}
					else{
						System.out.println("[IW BeanShellScript result (obj.toString()): +"+obj.toString());
					}
				}
			}
		
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
	}
	

	private Object runScript(IWContext iwc, BSHEngine engine) throws RemoteException {
		Object obj = null;
		
		try{
			if(scriptString!=null){
				//run script from scriptstring
				obj = engine.runScript(scriptString,iwc);
			}
			else if(scriptInBundleFileName!=null){
				//run from a file within a bundle
				if(bundle==null){
					bundle = this.getBundle(iwc);
				}
				
				obj = engine.runScriptFromBundle(bundle,scriptInBundleFileName,iwc);
			}
			else if(icFileScript!=null){
				//run from a script file in the db
				obj = engine.runScriptFromICFile(icFileScript,iwc);
			}
			else if(fileNameWithPath!=null){
				//run from a script file from anywhere on the server
				obj = engine.runScriptFromFileWithPath(fileNameWithPath);
			}
		}
		catch (TargetError e) {
			System.err.println("[IW BeanShellScript] - The script or code called by the script threw an exception: " + e.getTarget());
			obj = new Text("The script or code called by the script threw an exception: " + e.getTarget());
			e.printStackTrace();
		}
		catch (EvalError e2) {
			System.err.println("[IW BeanShellScript] - There was an error in evaluating the script:" + e2);
			obj = new Text("There was an error in evaluating the script:" + e2);
			e2.printStackTrace();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			
			if(scriptInBundleFileName!=null){
				obj = new Text("Script file was not found: "+bundle.getRealPathWithFileNameString(scriptInBundleFileName));
			}
			else if(fileNameWithPath!=null){
				obj = new Text("Script file was not found: "+fileNameWithPath);
			}
		}
		
		return obj;
	}


	private void addEditorAndRunScript(IWContext iwc, BSHEngine engine) throws RemoteException {
		Form editorForm = new Form();
		Table table = new Table(1,3);
		
		TextArea scriptArea = new TextArea(PARAM_SCRIPT_STRING,( (scriptString!=null)? scriptString : ""));
		scriptArea.setWidth("640");
		scriptArea.setHeight("480");
		
		table.add(scriptArea ,1,2);
		table.add(new SubmitButton(),1,3);
		
		Object obj = runScript(iwc,engine);
		if(obj!=null){
			if(obj instanceof PresentationObject){
				table.add((PresentationObject)obj,1,1);
			}
			else{
				System.out.println("[IW BeanShellScript result (obj.toString()): +"+obj.toString());
			}
		}
		
		editorForm.add(table);
		add(editorForm);
	}


	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setToShowScriptEditor(boolean showEditor){
		this.showEditor = showEditor;
	}
	
	public boolean isScriptEditorVisible(){
		return showEditor;
	}
	
	public void setScriptString(String scriptString){
		this.scriptString = scriptString;
	}
	
	public String getScriptString(){
		return scriptString;
	}
	
	public void setScriptFromICFile(ICFile script){
		this.icFileScript = script;
	}
	
	public void setScriptFileNameWithPath(String fileNameWithPath){
		this.fileNameWithPath = fileNameWithPath;
	}
	
	public void setBundleAndScriptFileName(IWBundle bundle, String fileName){
		scriptInBundleFileName = fileName;
		this.bundle = bundle;
	}
	
	public void setScriptFileNameAndUseDefaultBundle(String fileName){
		setBundleAndScriptFileName(null,fileName);
	}
	
	


}
