/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLCDATA;
import com.idega.xml.XMLElement;

/**
 * Title:		AbstractBandContainer
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
abstract class AbstractBandContainer extends XMLElement {
	
	private Band _band;
	
		
	private static final String ATTRIBUTE_HEIGHT = "height"; // NMTOKEN "0"
	private static final String ATTRIBUTE_IS_SPLIT_ALLOWED = "isSplitAllowed"; // (true | false) "true"
	
	/**
	 * @param name
	 */
	public AbstractBandContainer(String name) {
		super(name);
		this._band = new Band();
		super.addContent(this._band);
	}
	
	protected Band getBand(){
		return this._band;
	}
	
	public void setHeight(int height){
		this._band.setAttribute(ATTRIBUTE_HEIGHT,Integer.toString(height));
	}
	
	public void setIsSplitAllowed(boolean value){
		this._band.setAttribute(ATTRIBUTE_IS_SPLIT_ALLOWED,String.valueOf(value));
	}
	
	public XMLElement addContent(String text){
		return this._band.addContent(text);
	}
	
	public XMLElement addContent(XMLCDATA data){
		return this._band.addContent(data);

	}
	
	public XMLElement addContent(XMLElement element){
		return this._band.addContent(element);
	}
	
	
	

}
