/*
 * Created on 14.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.travel.block.search.business;

import java.util.List;
import java.util.Vector;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InvalidSearchException extends Exception {
	
	
	private List errorFields;
	/**
	 * 
	 */
	public InvalidSearchException() {
		super();
	}
	/**
	 * @param arg0
	 */
	public InvalidSearchException(String arg0) {
		super(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public InvalidSearchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	/**
	 * @param arg0
	 */
	public InvalidSearchException(Throwable arg0) {
		super(arg0);
	}
	
	public void addErrorField(String fieldName) {
		if (errorFields == null) {
			errorFields = new Vector();
		}
		errorFields.add(fieldName);
	}
	
	public List getErrorFields() {
		return errorFields;
	}
}
