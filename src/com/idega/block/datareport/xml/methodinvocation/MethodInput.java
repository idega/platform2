/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;

/**
 * Title:		MethodInput
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MethodInput extends XMLElement {

	static final String NAME = "input";
	private List _parameterClasses = new ArrayList();

	/**
	 * @param name
	 */
	public MethodInput() {
		super(NAME);
	}
	
	/**
	 * @param element
	 */
	public MethodInput(XMLElement element) throws XMLException {
		this();
		initialize(element);
	}

	private void initialize(XMLElement element) throws XMLException {
		List methodDescriptions = element.getChildren(ClassDescription.NAME);
		Iterator iter = methodDescriptions.iterator();
		if(iter != null){
			while (iter.hasNext()) {
				XMLElement localizedName = (XMLElement)iter.next();
				_parameterClasses.add(new ClassDescription(localizedName));
			}
		}
	}
	
	public void close(){
		
	}
	
	public List getClassDescriptions(){
		return _parameterClasses;
	}


}
