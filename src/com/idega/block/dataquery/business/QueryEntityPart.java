/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import java.util.Iterator;

import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryEntityPart implements QueryPart {
	private String name ;
	private String beanClass;
	
	public QueryEntityPart(String name, String beanClass){
		this.name = name;
		this.beanClass = beanClass;
	}
	
	public QueryEntityPart(XMLElement xml){
		if(xml.hasChildren()){
			Iterator iter = xml.getChildren().iterator();
			while(iter.hasNext()){
				XMLElement el = (XMLElement) iter.next();
				if(el.getName().equals(QueryXMLConstants.NAME)){
					this.name = el.getTextTrim();
				}
				else if(el.getName().equals(QueryXMLConstants.BEANCLASS)){
					this.beanClass = el.getTextTrim();
				}
			}
			
		}
	}
	
	public String getBeanClassName(){
		return beanClass;
	}
	
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#toQueryXML()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.ENTITY);
		el.addContent(getNameElement());
		el.addContent(getBeanClassElement());
		return el;
	}
	
	private XMLElement getNameElement(){
		XMLElement xmlName = new XMLElement(QueryXMLConstants.NAME);
		xmlName.addContent(name);
		return xmlName;
	}
	
	private XMLElement getBeanClassElement(){
		XMLElement xmlBeanClass = new XMLElement(QueryXMLConstants.BEANCLASS);
		xmlBeanClass.addContent(beanClass);
		return xmlBeanClass;
	}
}
