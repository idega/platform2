/*
 * Created on 17.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;

import com.idega.business.InputHandler;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;

/**
 * Title:		ClassHandler
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ClassHandler extends XMLElement {
	
	static final String NAME = "class_handler";
	
	private static final String ATTRIBUTE_CLASS = "class"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_VALUE = "value"; // NMTOKEN
	private Class _handlerClass = null;
	/**
	 * @param name
	 */
	public ClassHandler(Class c) {
		super(NAME);
		setClass(c);
	}
	
	private ClassHandler(){
		super(NAME);
	}
	
	/**
	 * @param element
	 */
	public ClassHandler(XMLElement element) throws XMLException {
		this();
		initialize(element);
	}

	private void initialize(XMLElement element) throws XMLException {
		try {
			XMLAttribute className = element.getAttribute(ATTRIBUTE_CLASS);
			this.setClass(Class.forName(className.getValue()));
		} catch (ClassNotFoundException e) {
			XMLException xmlE = new XMLException("Required attribute '"+ATTRIBUTE_CLASS+"' does not define valid Class",e);
			throw xmlE;
		} catch (NullPointerException e) {
			XMLException xmlE = new XMLException("Required attribute '"+ATTRIBUTE_CLASS+"' does not exist",e);
			throw xmlE;
		}
		
		XMLAttribute value = element.getAttribute(ATTRIBUTE_VALUE);
		if(value != null){
			this.setValue(value.getValue());
		}	
	}

	
	public void setClass(Class c){
		setAttribute(ATTRIBUTE_CLASS,c.getName());
		_handlerClass = c;
	}
	
	public Class getClassObject(){
		return _handlerClass;
	}
	
	
	public void setValue(String value){
		setAttribute(ATTRIBUTE_VALUE,value);
	}
	
	public String getValue(){
		XMLAttribute value = this.getAttribute(ATTRIBUTE_VALUE);
		if(value != null){
			return value.getValue();
		}
		return null;
	}
	
	public InputHandler getHandler() throws InstantiationException, IllegalAccessException{
		return (InputHandler)getClassObject().newInstance();
	}

}
