/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import java.util.StringTokenizer;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryFieldPart implements QueryPart {
	
	private String name = null;
	private String entity = null;
	private String[] properties = null;
	private String function = null;
	private String display = null;
	
	public QueryFieldPart(String name, String entity,String[] properties,String function, String display){
		this.name = name;
		this.entity = entity;
		this.properties = properties;
		this.function = function;
		this.display = display;
	}
	
	public QueryFieldPart(XMLElement xml){
		name = xml.getAttribute(QueryXMLConstants.NAME).getValue();
		entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		properties = commaListToArray(xml.getAttribute(QueryXMLConstants.PROPERTIES).getValue());
		function = xml.getAttribute(QueryXMLConstants.FUNCTION).getValue();
		if(xml.hasChildren()){
			XMLElement xmlDisplay = xml.getChild(QueryXMLConstants.DISPLAY);
			display = xmlDisplay.getTextTrim();
		}
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.FIELD);
		el.setAttribute(QueryXMLConstants.NAME,name);
		el.setAttribute(QueryXMLConstants.ENTITY,entity);
		el.setAttribute(QueryXMLConstants.PROPERTIES,stringArrayToCommaList(this.properties));
	  	if(this.function!=null)
	  		el.setAttribute(QueryXMLConstants.FUNCTION,function);
	  	XMLElement xmlDisplay = new XMLElement(QueryXMLConstants.DISPLAY);
	  	xmlDisplay.addContent(this.display);
	  	el.addContent(display);
		return el;
	}

	/**
	 * @return
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @return
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @return
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String[] getProperties() {
		return properties;
	}

	/**
	 * @param string
	 */
	public void setDisplay(String string) {
		display = string;
	}

	/**
	 * @param string
	 */
	public void setEntity(String string) {
		entity = string;
	}

	/**
	 * @param string
	 */
	public void setFunction(String string) {
		function = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param strings
	 */
	public void setProperties(String[] strings) {
		properties = strings;
	}
	
	public String stringArrayToCommaList(String[] array){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if(i>0)
				buf.append(",");
			buf.append(array[i]);				
		}
		return buf.toString();
	}
	
	public String[] commaListToArray(String commaList){
		StringTokenizer tokenizer = new StringTokenizer(commaList,",");
		String[] array = new String[tokenizer.countTokens()];
		for (int i = 0; i < array.length; i++) {
			array[i] = tokenizer.nextToken();
		}
		return array;
	}

}
