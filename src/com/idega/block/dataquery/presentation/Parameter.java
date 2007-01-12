/*
 * Created on Jul 7, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.presentation;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class Parameter {

	private String name;
	private Object value;
	private Class type;
	private Object defaultValue;

	/**
	 * 
	 */
	public Parameter(String name, Object value, Object defaultValue,Class type) {
		this.name = name;
		this.value = value;
		this.type = type;
		this.defaultValue = defaultValue;

	}
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return
	 */
	public Class getType() {
		return this.type;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		this.name = string;
	}

	/**
	 * @param class1
	 */
	public void setType(Class class1) {
		this.type = class1;
	}

	/**
	 * @param object
	 */
	public void setValue(Object object) {
		this.value = object;
	}

	/**
	 * @return
	 */
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @param object
	 */
	public void setDefaultValue(Object object) {
		this.defaultValue = object;
	}
	
	public void setAsDefaultValue(){
		this.value = this.defaultValue;
	}

}
