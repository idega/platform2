package com.idega.block.beanshell.business;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import bsh.servlet.BshServlet;

import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.util.caching.Cache;

/**
 * 
 * An adaptor for running Beanshell scripts (http://www.beanshell.org) within an idegaWeb application.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */
public class BSHEngineBean extends IBOServiceBean implements BSHEngine{

	private String bshVersion;

	public BSHEngineBean() {
	}


	/**
	 * A method that gets an Interpreter and runs the supplied bsh script.
	 * 
	 * @param theScript a string containing a bsh script to run
	 * @return the object result, might be null
	 */
	public Object runScript(String theScript) throws EvalError {
		Object obj = null;

		Interpreter engine = getBSHInterpreter();
		printBSHVersionNumber(engine);
		obj = engine.eval(theScript);

		return obj;
	}

	protected void printBSHVersionNumber(Interpreter engine) throws EvalError {
		engine.eval("print(\"[IdegaWeb Beanshell engine] - Beanshell version is : "+getBshVersion()+"\")");
	}


	/**
	 * A method that gets an Interpreter and runs the supplied bsh script WITH all request parameters initialized as String variables
	 * 
	 * @param theScript a string containing a bsh script to run
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScript(String theScript, IWContext iwc) throws EvalError,TargetError {
		Object obj = null;

		Interpreter engine = getInterpreterWithRequestParametersAndContextSet(iwc);
		//run the script
		printBSHVersionNumber(engine);
		obj = engine.eval(theScript);


		return obj;
	}
	
	/**
	 * A method that gets an Interpreter and runs the supplied bsh script WITH all request parameters initialized as String variables
	 * 
	 * @param bundle The bundle the script file is in
	 * @param scriptFileName The name of the bsh script file to run
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScriptFromBundle(IWBundle bundle, String scriptFileName, IWContext iwc) throws FileNotFoundException, EvalError {
		Object obj = null;		
		obj = runScriptFromFileWithPath(bundle.getRealPathWithFileNameString(scriptFileName),iwc);
		return obj;
	}
	
	/**
	 * A method that gets an Interpreter and runs the supplied bsh script
	 * 
	 * @param bundle The bundle the script file is in
	 * @param scriptFileName The name of the bsh script file to run
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScriptFromBundle(IWBundle bundle, String scriptFileName) throws FileNotFoundException, EvalError {
		Object obj = null;		
		obj = runScriptFromFileWithPath(bundle.getRealPathWithFileNameString(scriptFileName));
		return obj;
	}
	
	/**
	 * A method that gets an Interpreter and runs the supplied bsh script
	 * 
	 * @param scriptFileNameWithPath The name of the bsh script file to run with its path PREFIXED
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScriptFromFileWithPath(String scriptFileNameWithPath) throws FileNotFoundException, EvalError {
		Object obj = null;

		Interpreter engine = getBSHInterpreter();
		//run the script
		printBSHVersionNumber(engine);
		
		obj = engine.eval(new FileReader(scriptFileNameWithPath));

		return obj;
	}
	
	/**
	 * A method that gets an Interpreter and runs the supplied bsh script WITH all request parameters initialized as String variables
	 * 
	 * @param scriptFileNameWithPath The name of the bsh script file to run with its path PREFIXED
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScriptFromFileWithPath(String scriptFileNameWithPath, IWContext iwc) throws FileNotFoundException, EvalError {
		Object obj = null;

		Interpreter engine = getInterpreterWithRequestParametersAndContextSet(iwc);
		//run the script
		printBSHVersionNumber(engine);
		
		obj = engine.eval(new FileReader(scriptFileNameWithPath));

		return obj;
	}
	
	/**
	 * A method that gets an Interpreter and runs the supplied bsh script WITH all request parameters initialized as String variables
	 * 
	 * @param ICFile The bsh script file in the database to run
	 * @param iwc
	 * @return the object result, might be null
	 */
	public Object runScriptFromICFile(ICFile file, IWContext iwc) throws FileNotFoundException, EvalError {
		Object obj = null;

		Interpreter engine = getInterpreterWithRequestParametersAndContextSet(iwc);
		//run the script
		printBSHVersionNumber(engine);
		
		Cache fileInfo =  MediaBusiness.getCachedFileInfo( ((Integer)file.getPrimaryKey()).intValue(),iwc.getIWMainApplication());
		obj = engine.eval(new FileReader(fileInfo.getRealPathToFile()));

		return obj;
	}
	

	private Interpreter getInterpreterWithRequestParametersAndContextSet(IWContext iwc) throws EvalError {
		Enumeration enumer = iwc.getParameterNames();
		Interpreter engine = getBSHInterpreter();

		if (enumer != null) {
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();

				String[] values = iwc.getParameterValues(key);

				if (values != null && values.length > 1) {
					engine.set(key, values); //an array
				}
				else {
					engine.set(key, values[0]); //a string
				}

			}

		}

		engine.set("iwc", iwc);
		return engine;
	}


	public Interpreter getBSHInterpreter() {
		Interpreter engine = new bsh.Interpreter();
		return engine;
	}


	public String getBshVersion() {
		if (bshVersion != null)
			return bshVersion;

		/*
		 * We have included a getVersion() command to detect the version of bsh. If bsh is packaged in the WAR file it could access it directly as a
		 * bsh command. But if bsh is in the app server's classpath it won't see it here, so we will source it directly.
		 * 
		 * This command works around the lack of a coherent version number in the early versions.
		 */
		Interpreter bsh = new Interpreter();
		try {
			bsh.eval(new InputStreamReader(BshServlet.class.getResource("getVersion.bsh").openStream()));
			bshVersion = (String) bsh.eval("getVersion()");
		}
		catch (Exception e) {
			bshVersion = "BeanShell: unknown version";
		}

		return bshVersion;
	}

	/*
	 * Interpeter bsh = new Interpreter();
	 *  // Evaluate statements and expressions bsh.eval("foo=Math.sin(0.5)"); 5; bar=Math.cos(bar);"); bsh.eval("for(i=0; i
	 * <10; i++) { print(\"hello\"); }"); // same as above using java syntax and apis only bsh.eval("for(int i=0; i
	 * <10; i++) { System.out.println(\"hello\"); }");
	 *  // Source from files or streams bsh.source("myscript.bsh"); // or bsh.eval("source(\"myscript.bsh\")");
	 *  // Use set() and get() to pass objects in and out of variables bsh.set( "date", new Date() ); Date date = (Date)bsh.get( "date" ); // This
	 * would also work: Date date = (Date)bsh.eval( "date" );
	 * 
	 * bsh.eval("year = date.getYear()"); Integer year = (Integer)bsh.get("year"); // primitives use wrappers
	 *  // With Java1.3+ scripts can implement arbitrary interfaces... // Script an awt event handler (or source it from a file, more likely)
	 * bsh.eval( "actionPerformed( e ) { print( e ); }"); // Get a reference to the script object (implementing the interface) ActionListener
	 * scriptedHandler = (ActionListener)bsh.eval("return (ActionListener)this"); // Use the scripted event handler normally... new
	 * JButton.addActionListener( script );
	 *  
	 */

}