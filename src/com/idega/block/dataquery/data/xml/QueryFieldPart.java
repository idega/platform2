/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import java.util.StringTokenizer;

import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.xml.XMLAttribute;
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
	
	private IDOEntityField idoField = null;
	private IDOEntityDefinition entityDef = null;
	private String name = null;
	private String aliasName = null;
	private String entity = null;
	private String path = null;
	private String columns = null;
	private String function = null;
	private String display = null;
	private String typeClass = null;
	private String handlerClass = null;
	private String handlerDescription = null;
	private boolean locked = false;
	private boolean hidden = false;
	
	
// not used at the moment because function concat is not used at the moment	
//	public QueryFieldPart(String name, String entity, String path, String[] columns,String function, String display,String typeClass, String handlerClass, String handlerDescription, String hidden){
//		this(name,entity, path, "",function,display,typeClass, handlerClass, handlerDescription, Boolean.getBoolean(hidden));
//		this.columns = stringArrayToCommaList(columns);
//	}
	
	public QueryFieldPart(String name, String aliasName, String entity,String path, String column,String function,String display,String typeClass, String handlerClass, String handlerDescription){
		this( name, aliasName, entity, path, column, function, display, typeClass, handlerClass, handlerDescription, false);
	}
	
	
	
	public QueryFieldPart(String name, String aliasName, String entity,String path, String column,String function,String display,String typeClass, String handlerClass , String handlerDescription, boolean hidden){
		this.name = convertNullStringToRealNull(name);
		this.aliasName = convertNullStringToRealNull(aliasName);
		this.entity = convertNullStringToRealNull(entity);
		this.path = convertNullStringToRealNull(path);
		this.columns = convertNullStringToRealNull(column);
		this.function = convertNullStringToRealNull(function);
		this.display = convertNullStringToRealNull(display);
		this.typeClass = convertNullStringToRealNull(typeClass);
		this.handlerClass = convertNullStringToRealNull(handlerClass);
		this.handlerDescription = convertNullStringToRealNull(handlerDescription);
		this.hidden = hidden;
	}
	
	public QueryFieldPart(XMLElement xml) {
		this.name = xml.getAttribute(QueryXMLConstants.NAME).getValue();
		this.entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		this.path = xml.getAttribute(QueryXMLConstants.PATH).getValue();
		this.columns = xml.getAttribute(QueryXMLConstants.PROPERTIES).getValue();
		XMLAttribute func =  xml.getAttribute(QueryXMLConstants.FUNCTION);
		if(func!=null) {
			this.function = func.getValue();
		}
		this.typeClass = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		if(xml.hasChildren()) {
			this.aliasName = xml.getTextTrim(QueryXMLConstants.ALIAS_NAME);
			this.handlerClass = xml.getTextTrim(QueryXMLConstants.HANDLER);
			this.handlerDescription = xml.getTextTrim(QueryXMLConstants.HANDLER_DESCRIPTION);
			this.display = xml.getTextTrim(QueryXMLConstants.DISPLAY);
			XMLElement xmlLock = xml.getChild(QueryXMLConstants.LOCK);
			this.locked = (xmlLock!=null);
			XMLElement hidden = xml.getChild(QueryXMLConstants.HIDDEN);
			this.hidden = (hidden!= null);
		}
	}
	

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.FIELD);
		el.setAttribute(QueryXMLConstants.NAME,this.name);
		el.setAttribute(QueryXMLConstants.ENTITY,this.entity);
		el.setAttribute(QueryXMLConstants.PATH, this.path);
		el.setAttribute(QueryXMLConstants.PROPERTIES,this.columns);
		if(this.function!=null && !this.function.equalsIgnoreCase("null")) {
			el.setAttribute(QueryXMLConstants.FUNCTION,this.function);
		}
  	if(this.typeClass!=null && !this.typeClass.equalsIgnoreCase("null")) {
		el.setAttribute(QueryXMLConstants.TYPE,this.typeClass);
	}
	  if (this.aliasName != null && !this.aliasName.equalsIgnoreCase("null")) {
	  		XMLElement xmlAliasName = new XMLElement(QueryXMLConstants.ALIAS_NAME);
	  		xmlAliasName.addContent(this.aliasName);
	  		el.addContent(xmlAliasName);
	  }
	  if (this.display != null && !this.display.equalsIgnoreCase("null")) {
	  	XMLElement xmlDisplay = new XMLElement(QueryXMLConstants.DISPLAY);
	  	xmlDisplay.addContent(this.display);
	  	el.addContent(xmlDisplay);
	  }
  	if (this.handlerClass != null && !this.handlerClass.equalsIgnoreCase("null"))	{
  		XMLElement xmlHandlerClass = new XMLElement(QueryXMLConstants.HANDLER);
  		xmlHandlerClass.addContent(this.handlerClass);
  		el.addContent(xmlHandlerClass);
  	}
  	if (this.handlerDescription != null && !this.handlerDescription.equalsIgnoreCase("null")) {
  		XMLElement xmlHandlerDescription = new XMLElement(QueryXMLConstants.HANDLER_DESCRIPTION);
  		xmlHandlerDescription.addContent(this.handlerDescription);
  		el.addContent(xmlHandlerDescription);
  	}
  	if(this.locked) {
			el.addContent(new XMLElement(QueryXMLConstants.LOCK));
  	}
		if (this.hidden) {
			el.addContent(new XMLElement(QueryXMLConstants.HIDDEN));
		}
		return el;
	}

	/**
	 * @return
	 */
	public String getDisplay() {
		return this.display;
	}

	/**
	 * @return
	 */
	public String getEntity() {
		return this.entity;
	}
	
	public String getPath() {
		return this.path;
	}

	/**
	 * @return
	 */
	public String getFunction() {
		return this.function;
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public String getAliasName() {
		return (this.aliasName == null) ? this.name : this.aliasName;
	}
	
	/**
	 * @return
	 */
	public String[] getColumns() {
		return commaListToArray(this.columns);
	}

	/**
	 * @param string
	 */
	public void setDisplay(String string) {
		this.display = string;
	}

	/**
	 * @param string
	 */
	public void setEntity(String string) {
		this.entity = string;
	}

	/**
	 * @param string
	 */
	public void setFunction(String string) {
		this.function = string;
	}

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public void setHandlerClass(String handlerClass)  {
		this.handlerClass = handlerClass;
	}
	
	public void setHandlerDescription(String handlerDescription) {
		this.handlerDescription = handlerDescription;
	}
	
	public String getHandlerClass()  {
		return this.handlerClass;
	}
	
	public String getHandlerDescription() {
		return this.handlerDescription;
	}
	
	/**
	 * @param strings
	 */
	public void setColumns(String[] columnStrings) {
		this.columns = stringArrayToCommaList(columnStrings);
	}
	
	public void addColumn(String column){
		if(getColumns()!=null && getColumns().length>0){
			this.columns+=","+column;
		}
		else {
			this.columns = column;
		}
	}
	
	public void addColumn(String[] columns){
		if(columns!=null){
			addColumn(stringArrayToCommaList(columns));
		}
	}
	
	public String stringArrayToCommaList(String[] array){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if(i>0) {
				buf.append(",");
			}
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
	
	public String encode(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.name).append(';');   // 1
		buffer.append("null").append(';');  // 2
		buffer.append(this.entity).append(';'); // 3
		buffer.append(this.path).append(';'); // 4
		buffer.append(this.columns).append(';');  // 5
		buffer.append(this.function).append(';'); // 6
		buffer.append(this.display).append(';'); // 7 
		buffer.append(this.typeClass).append(';'); // 8 
		buffer.append(this.handlerClass).append(';'); // 9
		buffer.append(this.handlerDescription); // 10
		// the property hidden is always set explicitly
		// do not add hidden (encode/decode is used for comparision) 
//		buffer.append(hidden);		
		return buffer.toString();
	}
	
	public static QueryFieldPart  decode(String encoded){
			StringTokenizer toker = new StringTokenizer(encoded,";");
			if(toker.countTokens()== 10){
				return new QueryFieldPart(toker.nextToken(),
						toker.nextToken(), 
						toker.nextToken(),
						toker.nextToken(), 
						toker.nextToken(),
						toker.nextToken(),
						toker.nextToken(),
						toker.nextToken(), 
						toker.nextToken(), 
//						toker.nextToken(),
						toker.nextToken());
				
			}
			return null;
	}

	/**
	 * @return
	 */
	public String getTypeClass() {
		return this.typeClass;
	}

	/**
	 * @param string
	 */
	public void setTypeClass(String string) {
		this.typeClass = string;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		return this.locked;
	}

	public boolean isHidden()	{
		return this.hidden;
	}
	
	public void setHidden(boolean hidden)	{
		this.hidden = hidden;
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	private IDOEntityDefinition getIDOEntityDefinition() throws IDOLookupException, ClassNotFoundException{
		if(this.entityDef==null){
			this.entityDef = IDOLookup.getEntityDefinitionForClass(RefactorClassRegistry.forName(this.entity));
		}
		return this.entityDef;
	}

	public IDOEntityField getIDOEntityField() throws IDOLookupException, ClassNotFoundException{
		if(this.idoField==null){
			IDOEntityDefinition def = getIDOEntityDefinition();
			if(def != null){
				IDOEntityField[] fields = def.getFields();
				for (int i = 0; i < fields.length; i++) {
					if(fields[i].getUniqueFieldName().equals(this.name)){
						this.idoField = fields[i];
						return this.idoField;
					}
				}
			}
		}
		return this.idoField;
	}

	private String convertNullStringToRealNull(String string)	{
		return (string ==  null || string.equalsIgnoreCase("null")) ? null : string;
	}
		
	
}
