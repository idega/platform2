package com.idega.block.dataquery.business;

import java.util.StringTokenizer;

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
	
	private String name = null;
	private String entity = null;
	private String path = null;
	private String field = null;
	private String type = null;
	private String display = null;
	private int orderPriority = 0;
	private String orderType = null;
	
	/** used for first initializing (default values for order type is ascendant, order priority is set to one) */
	public QueryOrderConditionPart(String name, String entity, String path, String field, String display, String type) {
		this(name, entity, path, field, display, type, QueryXMLConstants.TYPE_ASCENDANT, 1);
	}

	public QueryOrderConditionPart(String name, String entity, String path, String field, String display, String type, String orderType, String orderPriority)	{
		this(name, entity,path,field, display, type, orderType,  Integer.parseInt(orderPriority));
	}

		
	public QueryOrderConditionPart(String name, String entity, String path, String field, String display, String type, String orderType, int orderPriority)	{
		this.name = name;
		this.entity = entity;
		this.path = path;
		this.field = field;
		this.display = display;
		this.type = type;
		this.orderType = orderType;
		this.orderPriority = orderPriority;
	}
		
	
	public QueryOrderConditionPart(XMLElement xml){
		name = xml.getAttribute(QueryXMLConstants.NAME).getValue();
		entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		path = xml.getAttribute(QueryXMLConstants.PATH).getValue();
		field = xml.getAttribute(QueryXMLConstants.FIELD).getValue();
		type = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		orderType = xml.getAttribute(QueryXMLConstants.ORDER_TYPE).getValue();
		if(xml.hasChildren()){
			XMLElement xmlPattern = xml.getChild(QueryXMLConstants.ORDER_PRIORITY);
			orderPriority =Integer.parseInt(xmlPattern.getTextTrim());
		}
	}

	public XMLElement getQueryElement()	{
		XMLElement el = new XMLElement(QueryXMLConstants.ORDER_CONDITION);
		el.setAttribute(QueryXMLConstants.NAME, name);
		el.setAttribute(QueryXMLConstants.ENTITY,entity);
		el.setAttribute(QueryXMLConstants.PATH, path);
		el.setAttribute(QueryXMLConstants.FIELD,field);
		el.setAttribute(QueryXMLConstants.TYPE,type);
		el.setAttribute(QueryXMLConstants.ORDER_TYPE, orderType);
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
		buffer.append(name).append(';');
		buffer.append(entity).append(';');
		buffer.append(path).append(';');
		buffer.append(field).append(';');
		buffer.append(display).append(';');
		buffer.append(type).append(';');
		buffer.append(orderType).append(';');
		buffer.append(orderPriority);
		return buffer.toString();
	}

	public static QueryOrderConditionPart decode(String encoded){
		StringTokenizer toker = new StringTokenizer(encoded,";");
		if(toker.countTokens()==8){
			return new QueryOrderConditionPart(toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken(), toker.nextToken(), toker.nextToken(), toker.nextToken());
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
	
	public String getName()	{
		return name;
	}
	
	public String getDisplay()	{
		return name;
	}
	
	public QueryFieldPart getCorrespondingField()	{
		return new QueryFieldPart(name, entity, path, field, null, display, type, true);
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
