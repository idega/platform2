/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;


import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;

/**
 * Title:		MethodDescription
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MethodDescription extends XMLElement {
	
	static final String NAME = "method_description";
	private static final String ATTRIBUTE_NAME = "name"; //NMTOKEN #REQUIRED
	//private static final String ATTRIBUTE_VOID = "void"; //(true | false) "true"
	
	private ClassDescription _classDescription = null;
	private MethodInput _input = null;
	private MethodOutput _output = null;

	/**
	 * @param name
	 */
	public MethodDescription(String name) {
		super(NAME);
		setName(name);
	}
	
	private MethodDescription(XMLAttribute name){
		this(name.getValue());
	}
	
	/**
	 * @param element
	 */
	public MethodDescription(XMLElement element) throws XMLException {
		this(element.getAttribute(ATTRIBUTE_NAME));
		initialize(element);
	}

	private void initialize(XMLElement element) throws XMLException {
		XMLElement clDesc = element.getChild(ClassDescription.NAME);
		if(clDesc != null){
			_classDescription = new ClassDescription(clDesc);	
		}
		
		XMLElement input = element.getChild(MethodInput.NAME);
		if(clDesc != null){
			_input = new MethodInput(input);	
		}
		
		XMLElement output = element.getChild(MethodOutput.NAME);
		if(clDesc != null){
			_output = new MethodOutput(output);	
		}
	}

	/**
	 * 
	 */
	public void close() {
		if(_classDescription!=null){
			this.addContent(_classDescription);
		}
		
		if(_input!=null){
			this.addContent(_input);
		}
		
		if(_output!=null){
			this.addContent(_output);
		}
	}
	
	public void setName(String name){
		setAttribute(ATTRIBUTE_NAME,name);
	}
	
	public String getName(){
		XMLAttribute name = getAttribute(ATTRIBUTE_NAME);
		if(name != null){
			return name.getValue();
		} else {
			return (null);
		}
	}
	
	public ClassDescription getClassDescription(){
		return _classDescription;
	}
	
	public MethodInput getInput(){
		return _input;
	}

	public MethodOutput getOutput(){
		return _output;
	}
}
