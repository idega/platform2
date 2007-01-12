/*
 * Created on Aug 6, 2003
 *
 */
package com.idega.block.dataquery.data.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.data.sql.InputDescription;
import com.idega.xml.XMLElement;

/**
 * QuerySQLPart
 * @author aron (extended by Thomas)
 * @version 1.0
 */

public class QuerySQLPart implements QueryPart {

	private String statement = null;
	// a statement that should be executed after the statement
	// for example: drop a view 
	private String postStatement = null;
	
	private Map keyValueMap = new HashMap();
	private Map keyTypeMap = new HashMap();
	private Map keyInputDescriptionMap = new HashMap();
	
	private List  resultFieldOrder = new ArrayList();
	private Map resultFieldTypeMap = new HashMap();
	private Map resultInputDescriptionMap = new HashMap(); 
	
	public QuerySQLPart(String statement) {
		this.statement = statement;
	}
	
	public QuerySQLPart(XMLElement xml){
		List children = xml.getChildren();
		Iterator childrenIterator = children.iterator();
		while (childrenIterator.hasNext())	{
			XMLElement element = (XMLElement) childrenIterator.next();
			String name = element.getName();
			if (name.equals(QueryXMLConstants.SQL_STATEMENT))	{
				this.statement = element.getTextTrim();
			}
			if (name.equals(QueryXMLConstants.SQL_POST_STATEMENT)) {
				this.postStatement = element.getTextTrim();
			}	
			if (name.equals(QueryXMLConstants.SQL_VARIABLE))	{
				String type = element.getChild(QueryXMLConstants.TYPE).getTextTrim();
				String key = element.getChild(QueryXMLConstants.SQL_VARIABLE_KEY).getTextTrim();
				String value = element .getChild(QueryXMLConstants.SQL_VARIABLE_VALUE).getTextTrim();
				String description = element.getChild(QueryXMLConstants.SQL_VARIABLE_DESCRIPTION).getTextTrim();
				String handler = element.getTextTrim(QueryXMLConstants.HANDLER);
				String handlerDescription = element.getTextTrim(QueryXMLConstants.HANDLER_DESCRIPTION);
				setVariable(key, type, value, description, handlerDescription, handler);
			}
			if (name.equals(QueryXMLConstants.SQL_RESULT))	{
				String field = element.getChild(QueryXMLConstants.SQL_RESULT_FIELD).getTextTrim();
				String type = element.getChild(QueryXMLConstants.SQL_RESULT_TYPE).getTextTrim();
				String description = element.getChild(QueryXMLConstants.SQL_RESULT_DESCRIPTION).getTextTrim();
				String handler = element.getTextTrim(QueryXMLConstants.HANDLER);
				String handlerDescription = element.getTextTrim(QueryXMLConstants.HANDLER_DESCRIPTION);
				setField(field, type, description, handlerDescription, handler);
			}	
		}
	}
		
	/** 
	 * 
	 * @see com.idega.block.dataquery.business.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		XMLElement sqlElement = new XMLElement(QueryXMLConstants.SQL);
		
		XMLElement statementElement = new XMLElement(QueryXMLConstants.SQL_STATEMENT);
		statementElement.setText(this.statement);
		sqlElement.addContent(statementElement);
		
		// post statement is not mandatory
		if (this.postStatement != null && this.postStatement.length() != 0) {
			XMLElement postStatementElement = new XMLElement(QueryXMLConstants.SQL_POST_STATEMENT);
			postStatementElement.setText(this.postStatement);
			sqlElement.addContent(postStatementElement);
		}
		// result
		Iterator fieldIterator = this.resultFieldOrder.iterator();
		while (fieldIterator.hasNext())	{
			String field = (String) fieldIterator.next();
			String type = (String) this.resultFieldTypeMap.get(field);
			InputDescription inputDescription = (InputDescription) this.resultInputDescriptionMap.get(field); 
			
			XMLElement resultFieldElement = new XMLElement(QueryXMLConstants.SQL_RESULT);
			
			XMLElement fieldElement = new XMLElement(QueryXMLConstants.SQL_RESULT_FIELD);
			fieldElement.setText(field);
			resultFieldElement.addContent(fieldElement);
			
			XMLElement typeElement = new XMLElement(QueryXMLConstants.SQL_RESULT_TYPE);
			typeElement.setText(type);
			resultFieldElement.addContent(typeElement);
			
			XMLElement descriptionElement = new XMLElement(QueryXMLConstants.SQL_RESULT_DESCRIPTION);
			descriptionElement.setText(inputDescription.getDescription());
			resultFieldElement.addContent(descriptionElement);
			
			String inputHandler = inputDescription.getInputHandler();
			if (inputHandler != null) {
				XMLElement inputHandlerElement = new XMLElement(QueryXMLConstants.HANDLER);
				inputHandlerElement.setText(inputHandler);
				resultFieldElement.addContent(inputHandlerElement);
			}
			
			String handlerDescription = inputDescription.getHandlerDescription();
			if (handlerDescription != null) {
				XMLElement handlerDescriptionElement = new XMLElement(QueryXMLConstants.HANDLER_DESCRIPTION);
				handlerDescriptionElement.setText(handlerDescription);
				resultFieldElement.addContent(handlerDescriptionElement);
			}
			sqlElement.addContent(resultFieldElement);
		}
			
		
		// variable
		Iterator iterator = this.keyValueMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			InputDescription inputDescription = (InputDescription) this.keyInputDescriptionMap.get(key);
			String type = (String) this.keyTypeMap.get(key);
			
			XMLElement variableElement = new XMLElement(QueryXMLConstants.SQL_VARIABLE);
			
			XMLElement typeElement = new XMLElement(QueryXMLConstants.TYPE);
			typeElement.setText(type);
			variableElement.addContent(typeElement);

			XMLElement keyElement = new XMLElement(QueryXMLConstants.SQL_VARIABLE_KEY);
			keyElement.setText(key);
			variableElement.addContent(keyElement);

			XMLElement valueElement = new XMLElement(QueryXMLConstants.SQL_VARIABLE_VALUE);
			valueElement.setText(value);
			variableElement.addContent(valueElement);
			
			XMLElement descriptionElement = new XMLElement(QueryXMLConstants.SQL_VARIABLE_DESCRIPTION);
			descriptionElement.setText(inputDescription.getDescription());
			variableElement.addContent(descriptionElement);
			
			String inputHandler = inputDescription.getInputHandler();
			if (inputHandler != null) {
				XMLElement inputHandlerElement = new XMLElement(QueryXMLConstants.HANDLER);
				inputHandlerElement.setText(inputHandler);
				variableElement.addContent(inputHandlerElement);
			}
			
			String handlerDescription = inputDescription.getHandlerDescription();
			if (handlerDescription != null) {
				XMLElement handlerDescriptionElement = new XMLElement(QueryXMLConstants.HANDLER_DESCRIPTION);
				handlerDescriptionElement.setText(handlerDescription);
				variableElement.addContent(handlerDescriptionElement);
			}

			sqlElement.addContent(variableElement);
		}
		return sqlElement;
	}

	public void setStatement(String statement)	{
		this.statement = statement;
	}
	
	public String getStatement()	{
		return this.statement;
	}
	
	public String getPostStatement()	{
		return this.postStatement;
	}
	
	public Map getVariableValueMap()	{
		return this.keyValueMap;
	}
	
	public Map getInputDescriptionValueMap()	{
		return this.keyInputDescriptionMap;
	}
	
	public void setVariable(String key, String type,  String value, String description, String handlerDescription, String handler) {
		this.keyTypeMap.put(key, type);
		this.keyValueMap.put(key, value);
		this.keyInputDescriptionMap.put(key, new InputDescription(description, handler, handlerDescription));
	}
		
	public void setField(String field, String type, String description, String handlerDescription, String handler)	{
		this.resultFieldOrder.add(field);
		this.resultFieldTypeMap.put(field, type);
		this.resultInputDescriptionMap.put(field, new InputDescription(description, handler, handlerDescription));
	}
	
	public List getFieldNames()	{
		return this.resultFieldOrder;
	}

	public List getFields(String queryName)	{
		List fields = new ArrayList();
		Iterator iterator = this.resultFieldOrder.iterator();
		while (iterator.hasNext())	{
			String field = (String) iterator.next();
			String type = (String) this.resultFieldTypeMap.get(field);
			InputDescription inputDescription = (InputDescription) this.resultInputDescriptionMap.get(field);
			// name, aliasName, entity, path, column,function, display, typeClass, handlerClass, handlerDescription
			QueryFieldPart fieldPart = new QueryFieldPart(field, null, queryName,queryName,field, null, null, type, inputDescription.getInputHandler(), inputDescription.getHandlerDescription());
			fields.add(fieldPart);
		}
		return fields;
	}
			

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#encode()
	 */
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {

	}

}
