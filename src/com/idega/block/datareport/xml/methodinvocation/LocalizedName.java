/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;

import java.util.Locale;

import com.idega.util.LocaleUtil;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLElement;

/**
 * Title:		LocalizedName
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class LocalizedName extends XMLElement {

	static final String NAME = "localized_name";
	private static final String ATTRIBUTE_NAME = "name";  // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_LOCALE = "locale"; // NMTOKEN #REQUEIRED


	/**
	 * @param name
	 */
	public LocalizedName(String name, Locale locale) {
		this(name,locale.toString());
		
	}
	
	/**
	 * @param name
	 */
	public LocalizedName(String name, String locale) {
		super(NAME);
		this.setName(name);
		this.setLocale(locale);
	}
	
	/**
	 * @param element
	 */
	public LocalizedName(XMLElement element) {
		this(element.getAttribute(ATTRIBUTE_NAME).getValue(),element.getAttribute(ATTRIBUTE_LOCALE).getValue());
		initialize(element);
	}

	private void initialize(XMLElement element){

	}
	
	public void close(){
		
	}
	
	public void setLocale(Locale locale){
		this.setAttribute(ATTRIBUTE_LOCALE,locale.toString());
	}
	
	public void setLocale(String locale){
		this.setAttribute(ATTRIBUTE_LOCALE,locale);
	}
	
	public void setName(String name){
		this.setAttribute(ATTRIBUTE_NAME,name);
	}
	
	public String getName(){
		return getAttribute(ATTRIBUTE_NAME).getValue();
	}
	
	public Locale getLocale(){
		XMLAttribute locale = getAttribute(ATTRIBUTE_LOCALE);
		if(locale != null){
			return LocaleUtil.getLocale(locale.getValue());
		} else {
			return null;
		}
	}

}
