/*
 * Created on 14.2.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.beanshell.presentation;

import java.rmi.RemoteException;

import com.idega.block.beanshell.business.BSHEngine;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
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
 * @version 0.5
 */
public class BeanShellScript extends Block {


	private static final String IW_BUNDLE_IDENTIFIER="com.idega.block.beanshell";
	private boolean showEditor = false;
	private String scriptString;
	private static final String PARAM_SCRIPT_STRING = "bsh_scr_str";
	
	public BeanShellScript() {
		super();
	}
	
	
	public void main(IWContext iwc) throws RemoteException{
		//TODO Eiki make safe to execute from parameter
		//TODO Eiki allow multiple scripts per page and support ordering scripts
		//TODO Eiki Put editor in a window and support syntax coloring
		//TODO Eiki script from an icfile, filesystem and bundle
		BSHEngine engine;
		try {
			engine = (BSHEngine) IBOLookup.getServiceInstance(iwc,BSHEngine.class);
		
			if(scriptString==null && iwc.isSuperAdmin()){
				scriptString = iwc.getParameter(PARAM_SCRIPT_STRING);
			}
			
			if(showEditor){
				Form editorForm = new Form();
				Table table = new Table(1,3);
				
				TextArea scriptArea = new TextArea(PARAM_SCRIPT_STRING,( (scriptString!=null)? scriptString : ""));
				scriptArea.setWidth("640");
				scriptArea.setHeight("480");
				
				table.add(scriptArea ,1,2);
				table.add(new SubmitButton(),1,3);
				
				if(scriptString!=null){
					BeanShellScript script = new BeanShellScript();
					script.setScriptString(scriptString);
					table.add(script,1,1);
				}
				
				editorForm.add(table);
				add(editorForm);
				
			}
			else{
				if(scriptString!=null){
					Object obj = engine.runScript(scriptString,iwc);
					if(obj instanceof PresentationObject){
						add(obj);
					}
				}
			}
		
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
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
	
	
	
	


}
