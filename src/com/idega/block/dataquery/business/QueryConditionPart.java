/*
 * Created on May 27, 2003
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

public class QueryConditionPart implements QueryPart {

	private String field = null;
	private String entity = null;
	private String type = null;
	private String pattern = null;
	private boolean lock = false;
	private boolean dynamic = false;
	
	public static final String TYPE_LIKE = "like";
	public static final String TYPE_EQ = "equal";
	public static final String TYPE_NEQ = "not-equal";
	public static final String TYPE_LT = "less";
	public static final String TYPE_GT ="greater";
	public static final String TYPE_GEQ = "greater-than-or-equals";
	public static final String TYPE_LEQ = "less-than-or-equals";
	private static final String[] TYPES = { TYPE_LIKE,TYPE_EQ,TYPE_NEQ,TYPE_LT,TYPE_GT,TYPE_GEQ,TYPE_LEQ};
	
	public static String[] getConditionTypes(){
		return   TYPES;
	}
	
	public QueryConditionPart(String entity,String field, String type, String pattern){
		this.entity = entity;
		this.field = field;
		this.type = type;
		this.pattern = pattern;
	}
	
	public QueryConditionPart(XMLElement xml){
		entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		field = xml.getAttribute(QueryXMLConstants.FIELD).getValue();
		type = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		if(xml.hasChildren()){
			XMLElement xmlPattern = xml.getChild(QueryXMLConstants.PATTERN);
			pattern = xmlPattern.getTextTrim();
			XMLElement xmlLock = xml.getChild(QueryXMLConstants.LOCK);
			lock = xmlLock!=null;
			XMLElement xmlDyna = xml.getChild(QueryXMLConstants.DYNAMIC);
			dynamic = xmlDyna!=null;
		}
	}
	
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.CONDITION);
		el.setAttribute(QueryXMLConstants.ENTITY,entity);
		el.setAttribute(QueryXMLConstants.FIELD,field);
		el.setAttribute(QueryXMLConstants.TYPE,type);
		XMLElement xmlPattern = new XMLElement(QueryXMLConstants.PATTERN);
		xmlPattern.addContent(pattern);
		el.addContent(xmlPattern);
		if(lock){
			el.addContent(new XMLElement(QueryXMLConstants.LOCK));
		}
		if(dynamic)
			el.addContent(new XMLElement(QueryXMLConstants.DYNAMIC));
		return el;
	}
	
	/**
	 * @return
	 */
	public String getEntity() {
		return field;
	}

	/**
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
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
	public void setField(String string) {
		field = string;
	}

	/**
	 * @param string
	 */
	public void setPattern(String string) {
		pattern = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}
	
	public String encode(){
		return this.entity+";"+this.field+";"+this.type+";"+this.pattern;
	}
	
	public static QueryConditionPart decode(String encoded){
		StringTokenizer toker = new StringTokenizer(encoded,";");
		if(toker.countTokens()==4){
			return new QueryConditionPart(toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken());
		}
		return null;
	}
	

	/* (non-Javadoc)
	* @see com.idega.block.dataquery.business.QueryPart#isLocked()
	*/
	public boolean isLocked() {
		return lock;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		this.lock = locked;
	}


	/**
	 * @return
	 */
	public boolean isDynamic() {
		return dynamic;
	}

	/**
	 * @param b
	 */
	public void setDynamic(boolean b) {
		dynamic = b;
	}

}
