package com.idega.block.dataquery.data.xml;

import java.util.StringTokenizer;

import com.idega.block.dataquery.data.xml.*;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Nov 13, 2003
 */
public class QueryOrderConditionPart implements QueryPart {
	
	private String entity = null;
	private String path = null;
	private String field = null;
	private int orderPriority = 0;
	private String orderType = null;
	
	/** used for first initializing (default values for order type is ascendant, order priority is set to one) */
	public QueryOrderConditionPart(String entity, String path, String[] fields) {
		this(entity, path, fields[0], QueryXMLConstants.TYPE_ASCENDANT, 1);
	}

	public QueryOrderConditionPart(String entity, String path, String field, String orderType, String orderPriority)	{
		this(entity,path,field, orderType,  Integer.parseInt(orderPriority));
	}

		
	public QueryOrderConditionPart(String entity, String path, String field, String orderType, int orderPriority)	{
		this.entity = entity;
		this.path = path;
		this.field = field;
		this.orderType = orderType;
		this.orderPriority = orderPriority;
	}
		
	
	public QueryOrderConditionPart(XMLElement xml){
		entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		path = xml.getAttribute(QueryXMLConstants.PATH).getValue();
		field = xml.getAttribute(QueryXMLConstants.FIELD).getValue();
		if(xml.hasChildren()){
			XMLElement xmlOrderType = xml.getChild(QueryXMLConstants.ORDER_TYPE);
			orderType = xmlOrderType.getTextTrim();
			XMLElement xmlPattern = xml.getChild(QueryXMLConstants.ORDER_PRIORITY);
			orderPriority =Integer.parseInt(xmlPattern.getTextTrim());
		}
	}

	public XMLElement getQueryElement()	{
		XMLElement el = new XMLElement(QueryXMLConstants.ORDER_CONDITION);
		el.setAttribute(QueryXMLConstants.ENTITY,entity);
		el.setAttribute(QueryXMLConstants.PATH, path);
		el.setAttribute(QueryXMLConstants.FIELD,field);
		XMLElement xmlOrderType = new XMLElement(QueryXMLConstants.ORDER_TYPE);
		xmlOrderType.addContent(orderType);
		el.addContent(xmlOrderType);
		XMLElement xmlPriority = new XMLElement(QueryXMLConstants.ORDER_PRIORITY);
		xmlPriority.addContent(Integer.toString(orderPriority));
		el.addContent(xmlPriority);
		return el;
	}

		
	

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#encode()
	 */
	public String encode(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(entity).append(';');
		buffer.append(path).append(';');
		buffer.append(field).append(';');
		buffer.append(orderType).append(';');
		buffer.append(orderPriority);
		return buffer.toString();
	}

	public static QueryOrderConditionPart decode(String encoded){
		StringTokenizer toker = new StringTokenizer(encoded,";");
		if(toker.countTokens()==5){
			return new QueryOrderConditionPart(toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken());
		}
		return null;
	}

	public String getField()	{
		return this.field;
	}
	
	public String getPath()	{
		return this.path;
	}
	
	/** Returns true, if the type is not set to descendant else false, that is ascendant is the default value */
	public boolean isAscendant()	{
		return ! QueryXMLConstants.TYPE_DESCENDANT.equalsIgnoreCase(orderType);
	}
	
	public boolean isDescendant()	{
		return ! isAscendant();
	}
	
	public void setAscendant(boolean isAscendant)	{
		orderType = (isAscendant) ? QueryXMLConstants.TYPE_ASCENDANT : QueryXMLConstants.TYPE_DESCENDANT;
	}
	
	public void setDescendant(boolean isDescendant)	{
		setAscendant(! isDescendant);
	}
	
	public int getOrderPriority()	{
		return orderPriority;
	}
	
	public void setOrderPriority(int orderPriority)	{
		this.orderPriority = orderPriority;
	}
		
	public String getEntity()	{
		return entity;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub

	}
	
 // for use in comparators
  public int compare(QueryOrderConditionPart part) {
  	if (part.orderPriority < orderPriority) {
  		return 1;
  	}
  	else if (part.orderPriority > orderPriority) {
  		return -1;
  	}
  	return 0;
  }


}
