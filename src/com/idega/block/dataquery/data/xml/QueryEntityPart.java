/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.idega.core.data.IWTreeNode;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryEntityPart extends IWTreeNode implements QueryPart {
	//private String name ;
	private String beanClass;
	private boolean locked = false;
	//private String path;
	
	public QueryEntityPart(String name, String beanClass){
		super(name);
		//this.name = name;
		setBeanClass( beanClass);
		//this.path = name;
	}
	
	public QueryEntityPart(String name, String beanClass,String path){
			this(name,beanClass);
			setPath(path);
		}
		
	public QueryEntityPart(XMLElement xml){
		this("","");
		if(xml.hasChildren()){
			Iterator iter = xml.getChildren().iterator();
			while(iter.hasNext()){
				XMLElement el = (XMLElement) iter.next();
				if(el.getName().equals(QueryXMLConstants.NAME)){
					//this.name = el.getTextTrim();
					setName(el.getTextTrim());
				}
				else if(el.getName().equals(QueryXMLConstants.BEANCLASS)){
					setBeanClass(el.getTextTrim());
				}
				else if(el.getName().equals(QueryXMLConstants.PATH)){
					//this.path = el.getTextTrim();
					setPath(el.getTextTrim());
				}
				else if(el.getName().equals(QueryXMLConstants.LOCK)){
					this.locked = true;
				}
			}
			
		}
	}
	
	public String getBeanClassName(){
		return this.beanClass;
	}
	
	public String getName(){
		return getNodeName();
	}
	
  public List getPathNames()  {
    List list = new ArrayList();
    String path = getPath();
    StringTokenizer tokenizer = new StringTokenizer(path, IWTreeNode.PATH_DELIMITER);
    while (tokenizer.hasMoreTokens()) {
      String element = tokenizer.nextToken();
      list.add(element);
    }
    return list;
  }
      
  
	public String getPath(){
		return getNodePath();
	}
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#toQueryXML()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.ENTITY);
		el.addContent(getNameElement());
		el.addContent(getBeanClassElement());
		if(!"".equals(getPath())) {
			el.addContent(getPathElement());
		}
		if(this.locked){
			el.addContent(new XMLElement(QueryXMLConstants.LOCK));
		}
		return el;
	}
	
	private XMLElement getNameElement(){
		XMLElement xmlName = new XMLElement(QueryXMLConstants.NAME);
		xmlName.addContent(getName());
		return xmlName;
	}
	
	private XMLElement getBeanClassElement(){
		XMLElement xmlBeanClass = new XMLElement(QueryXMLConstants.BEANCLASS);
		xmlBeanClass.addContent(getBeanClassName());
		return xmlBeanClass;
	}
	
	private XMLElement getPathElement(){
		XMLElement xmlBeanClass = new XMLElement(QueryXMLConstants.PATH);
		xmlBeanClass.addContent(getPath());
		return xmlBeanClass;
	}
	
	public String encode(){
		return this.getName()+";"+this.getBeanClassName() +(getPath()!=null?";"+getPath():"");
	}
	
	public static QueryEntityPart decode(String encoded){
		StringTokenizer toker = new StringTokenizer(encoded,";");
		if(toker.countTokens()==2){
			return new QueryEntityPart( toker.nextToken(), toker.nextToken());
		}
		else if(toker.countTokens()==3){
			QueryEntityPart part =  new QueryEntityPart( toker.nextToken(), toker.nextToken(),toker.nextToken());
			return part;
		}		
		return null;
	}
	/**
	 * @param string
	 */
	public void setBeanClass(String string) {
		this.beanClass = string;
	}


	/**
	 * @param string
	 */
	public void setPath(String string) {
		super.setNodePath(string);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
