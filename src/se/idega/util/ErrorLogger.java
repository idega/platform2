package se.idega.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Object to improve storing and logging error and warning messages.
 * 
 * This object was created to replace a similar tool implemented as a StringBuffer, hence the 
 * function names are similar to StringBuffer, even though the object behind it is an ArrrayList.
 * 
 * The general idea is to store data here so that it is available do be displayed if an error occurs.
 * As an extension it is also possible to let this object do some logging to the console.
 * 
 * It is possible to set the loggin level at creation and then all new loggings with same or lower
 * logging level will be outputed to the console at the same time
 *  
 * @author Joakim
 * 
 */
public class ErrorLogger {
	private ArrayList message = new ArrayList();
	private int loggingLevel;
	private Logger log = Logger.getLogger("ErrorLogger");
	
	/**
	 * The new ErrorLogger is a copy of el
	 * @param el
	 */
	public ErrorLogger(ErrorLogger el){
		this.message = new ArrayList(el.getMessage());
		loggingLevel = el.getLoggingLevel();
	}
	
	/**
	 * Create an empty error logger with logging level set to 5
	 */
	public ErrorLogger(){
		loggingLevel = 5;
	}

	/**
	 * Create a new error logger with initial value of string s
	 * @param s
	 */	
	public ErrorLogger(String s){
		message.add(s);
		loggingLevel = 4;
	}
	
	/**
	 * Create an empty error logger with logging level set to l
	 * @param l
	 */
	public ErrorLogger(int l){
		loggingLevel = l;
	}

	/**
	 * Create a new error logger with initial value of string s and logging level of l
	 * @param s
	 * @param l
	 */	
	public ErrorLogger(String s, int l){
		message.add(s);
		loggingLevel = l;
	}

	/**
	 * Appends a logging string
	 * @param s
	 */	
	public void append(String s){
		message.add(s);
		if(loggingLevel>=5){
			log.info(s);
		}
	}

	/**
	 * Appends a logging string and outputs it to the console if the logging level is greater than 
	 * or equal to i
	 * @param s
	 * @param i
	 */	
	public void append(String s, int i){
		message.add(s);
		if(loggingLevel>=i){
			log.info(s);
		}
	}

	/**
	 * Returns all the loggging strings, with linebreak between each line
	 */
	public String toString(){
		StringBuffer ret = new StringBuffer();
		Iterator iter = message.iterator();
		while (iter.hasNext()) {
			ret.append((String)iter.next()+"\n");
		}
		return ret.toString(); 
	}

	/**
	 * Returns all the loggging strings, with ';' between each line
	 */
	public String toStringCompact(){
		StringBuffer ret = new StringBuffer();
		Iterator iter = message.iterator();
		while (iter.hasNext()) {
			ret.append((String)iter.next()+" ; ");
		}
		return ret.toString(); 
	}

	/**
	 * Returns all the loggging strings, with '<br>' between each line
	 * @return String
	 */
	public String toStringForWeb(){
		StringBuffer ret = new StringBuffer();
		Iterator iter = message.iterator();
		while (iter.hasNext()) {
			ret.append((String)iter.next()+"<br>");
		}
		return ret.toString(); 
	}
	
	/**
	 * logs the content to the console
	 */
	public void logToConsole(){
		log.info(toString());
	}
	
	/**
	 * logs the content to the console with compact format
	 */
	public void logToConsoleCompact(){
		log.info(toStringCompact());
	}
	
	public Object clone(){
		ErrorLogger el = new ErrorLogger(this);
		return el;
	}


	/**
	 * @return
	 */
	public int getLoggingLevel() {
		return loggingLevel;
	}

	/**
	 * @return
	 */
	public ArrayList getMessage() {
		return message;
	}

}
