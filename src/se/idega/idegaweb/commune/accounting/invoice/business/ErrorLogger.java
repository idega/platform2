package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Joakim
 * Does Error logging... Will comment better later
 */
public class ErrorLogger {
	private ArrayList message = new ArrayList();
	private int loggingLevel;
	private Logger log = Logger.getLogger("ErrorLogger");
	
	public ErrorLogger(ErrorLogger el){
		this.message = new ArrayList(el.getMessage());
		loggingLevel = el.getLoggingLevel();
	}
	
	public ErrorLogger(){
		loggingLevel = 5;
	}
	
	public ErrorLogger(String s){
		message.add(s);
		loggingLevel = 4;
	}
	
	public ErrorLogger(int l){
		loggingLevel = l;
	}
	
	public ErrorLogger(String s, int l){
		message.add(s);
		loggingLevel = l;
	}
	
	public void append(String s){
		message.add(s);
		if(loggingLevel>=5){
			log.info(s);
		}
	}
	
	public void append(String s, int i){
		message.add(s);
		if(loggingLevel>=i){
			log.info(s);
		}
	}
	
	public String toString(){
		StringBuffer ret = new StringBuffer();
		Iterator iter = message.iterator();
		while (iter.hasNext()) {
			ret.append((String)iter.next()+"\n");
		}
		return ret.toString(); 
	}

	public String toStringForWeb(){
		StringBuffer ret = new StringBuffer();
		Iterator iter = message.iterator();
		while (iter.hasNext()) {
			ret.append((String)iter.next()+"<br>");
		}
		return ret.toString(); 
	}
	
	public void logToConsole(){
		log.info(toString());
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
