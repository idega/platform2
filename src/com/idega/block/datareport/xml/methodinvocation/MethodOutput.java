/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;

import com.idega.xml.XMLElement;

/**
 * Title:		MethodOutput
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MethodOutput extends XMLElement {

	static final String NAME = "output";
	private XMLElement _returningObject = null;
	
	/**
	 * @param name
	 */
	public MethodOutput() {
		super(NAME);
	}
	
	/**
	 * @param element
	 */
	public MethodOutput(XMLElement element) {
		this();
		initialize(element);
	}

	private void initialize(XMLElement element){
		XMLElement childElement = element.getChild(ClassDescription.NAME);
		if(childElement != null){
			_returningObject = new ClassDescription(childElement);
		}
	}
	
	public void close(){
		if(_returningObject != null){
			this.addContent(_returningObject);
		}
	}


}
