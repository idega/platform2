/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLElement;

/**
 * Title:		Field
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class Field extends XMLElement {
	private static final String ATTRIBUTE_NAME = "name"; //#REQUIRED
	private static final String ATTRIBUTE_CLASS = "class";

	/**
	 * @param name
	 */
	public Field(String name) {
		super("field");
		setName(name);
	}
	
	public void setName(String name){
		setAttribute(ATTRIBUTE_NAME,name);
	}
	
	public void setClassType(Class c){
		setAttribute(ATTRIBUTE_CLASS,c.getName());
	}
	
	public void setClassType(String c){
		setAttribute(ATTRIBUTE_CLASS,c);
	}

}
