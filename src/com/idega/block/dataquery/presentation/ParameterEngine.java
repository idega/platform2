/*
 * Created on Jul 7, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.presentation;

import java.io.InvalidClassException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.idega.presentation.IWContext;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class ParameterEngine {

	private String identifier;
	private HashMap parameters;

	/**
	 * 
	 */
	public ParameterEngine(String identifier) {
		this.identifier = identifier;
		this.parameters = new HashMap();
	}

	public void parse(IWContext iwc) throws InvalidClassException{
		Iterator iter = this.parameters.values().iterator();
		Parameter prm;
		String key;
		while (iter.hasNext()) {
			prm = (Parameter) iter.next();
			key = getPrefixedKeyName(prm.getName()); 
			if (iwc.isParameterSet(key)) {
				String value = iwc.getParameter(key);
				setObjecValue(prm,value);
			}
			else{
				prm.setAsDefaultValue();
			}
		}
	}

	private void setObjecValue(Parameter prm, String value) throws InvalidClassException{
		String className = prm.getType().getName();
		if (className.equals(Integer.class.getName())) {
			prm.setValue(new Integer(value));
		}
		else if (className.equals(Date.class.getName())) {
			prm.setValue(new Date(value));
		}
		else if(className.equals(String.class.getName())){
			prm.setValue(value);
		}
		else if(className.equals(Boolean.class.getName())){
			prm.setValue(new Boolean(value));
		}
		else if(className.equals(Float.class.getName())){
			prm.setValue(new Float(value));
		}
		else if(className.equals(Double.class.getName())){
			prm.setValue(new Double(value));
		}
		else {
			throw new InvalidClassException(className);
		}

	}

	public Object getParameterValue(String key) {
		if (this.parameters.containsKey(key)) {
			return ((Parameter) this.parameters.get(key)).getValue();
		}
		return null;
	}

	private String getPrefixedKeyName(String key) {
		return this.identifier + "_" + key;
	}
	
	public Parameter createParameter(String name, Class type,Object defaultValue){
		Parameter prm =new Parameter(name,null,defaultValue,type);
		this.parameters.put(name,prm);
		return prm;
	}
	
	public Parameter createParameter(String name,Class type){
		return createParameter(name,type,null);
	}
	
	

}
