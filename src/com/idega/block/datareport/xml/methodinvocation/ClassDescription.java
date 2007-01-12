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
import java.util.Locale;


import com.idega.repository.data.RefactorClassRegistry;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;

/**
 * Title:		ClassDescription
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ClassDescription extends XMLElement {
	
	static final String NAME = "class_description";
	
	private static final String ATTRIBUTE_NAME = "name"; // NMTOKEN
	private static final String ATTRIBUTE_CLASS = "class"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_DEFAULT_DISPLAY_NAME = "default_display_name";  // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_TYPE = "type"; // ( Class | IDOEntityHome | IBOSessionBean | IBOServeceBean) "Class"	
	public static final String VALUE_TYPE_CLASS = "Class";
	public static final String VALUE_TYPE_IDO_ENTITY_HOME = "IDOEntityHome";
	public static final String VALUE_TYPE_IDO_SESSION_BEAN = "IBOSessionBean";
	public static final String VALUE_TYPE_IDO_SERVICE_BEAN = "IBOServiceBean";	
	private List _localizedNames = new ArrayList();
	private ClassHandler _handler = null;
	
	/**
	 * @param name
	 */
	public ClassDescription(Class classOjb, String defaultDisplayName) {
		super(NAME);
		this.setClassObject(classOjb);
		this.setDefaultDisplayName(defaultDisplayName);
	}
	
	/**
	 * @param name
	 */
	private ClassDescription(String classOjb, String defaultDisplayName) {
		super(NAME);
		this.setClassObject(classOjb);
		this.setDefaultDisplayName(defaultDisplayName);
	}
	
	private ClassDescription(XMLAttribute classObj, XMLAttribute defaultDisplayName){
		this((classObj==null)?"":classObj.getValue(),(defaultDisplayName==null)?"":defaultDisplayName.getValue());
	}


	/**
	 * @param element
	 */
	public ClassDescription(XMLElement element) throws XMLException {
		this(element.getAttribute(ATTRIBUTE_CLASS),element.getAttribute(ATTRIBUTE_DEFAULT_DISPLAY_NAME));
		initialize(element);
	}

	private void initialize(XMLElement element) throws XMLException {
		XMLAttribute type = element.getAttribute(ATTRIBUTE_TYPE);
		if(type != null){
			this.setType(type.getValue());
		}
		
		XMLAttribute name = element.getAttribute(ATTRIBUTE_NAME);
		if(name != null){
			this.setName(name.getValue());
		}
		
		XMLElement handler = element.getChild(ClassHandler.NAME);
		if(handler != null){
			this._handler = new ClassHandler(handler);
		}
		
		List methodDescriptions = element.getChildren(LocalizedName.NAME);
		Iterator iter = methodDescriptions.iterator();
		if(iter != null){
			while (iter.hasNext()) {
				XMLElement localizedName = (XMLElement)iter.next();
				this._localizedNames.add(new LocalizedName(localizedName));
			}
		}
	}
	
	public void close(){
		
	}
	
	public void setName(String name){
		setAttribute(ATTRIBUTE_NAME,name);
	}
	
	public void setClassObject(Class classObj){
		setAttribute(ATTRIBUTE_CLASS,classObj.getName());
	}
	
	private void setClassObject(String classObj){
		setAttribute(ATTRIBUTE_CLASS,classObj);
	}
	
	

	public void setDefaultDisplayName(String name){
		setAttribute(ATTRIBUTE_DEFAULT_DISPLAY_NAME,name);
	}
	
	public void setType(String type){
		setAttribute(ATTRIBUTE_TYPE,type);
	}

	public String getName(){
		XMLAttribute name = this.getAttribute(ATTRIBUTE_NAME);
		if(name != null){
			return name.getValue();
		}
		return null;
	}
	
	public String getDefaultDisplayName(){
		return this.getAttribute(ATTRIBUTE_DEFAULT_DISPLAY_NAME).getValue();
	}
	
	public String getType(){
		XMLAttribute type = this.getAttribute(ATTRIBUTE_TYPE);
		if(type != null){
			return type.getValue();
		} else {
			return VALUE_TYPE_CLASS;
		}
	}
	
	public Class getClassObject() throws ClassNotFoundException{
		return RefactorClassRegistry.forName(this.getAttribute(ATTRIBUTE_CLASS).getValue());
	}
	
	public String getLocalizedName(Locale locale){
		if(this._localizedNames != null && locale != null){
			Iterator iter = this._localizedNames.iterator();
			while (iter.hasNext()) {
				LocalizedName lName = (LocalizedName)iter.next();
				if(locale.equals(lName.getLocale())){
					return lName.getName();
				}
			}
		}
		return this.getDefaultDisplayName();
	}
	
	public ClassHandler getClassHandler(){
		return this._handler;
	}
	
}
