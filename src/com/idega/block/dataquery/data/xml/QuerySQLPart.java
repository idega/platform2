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
	private Map keyDescriptionMap = new HashMap();
	
	private List  resultFieldOrder = new ArrayList();
	private Map resultFieldTypeMap = new HashMap();
	private Map resultDescriptionMap = new HashMap(); 
	
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
				statement = element.getTextTrim();
			}
			if (name.equals(QueryXMLConstants.SQL_POST_STATEMENT)) {
				postStatement = element.getTextTrim();
			}	
			if (name.equals(QueryXMLConstants.SQL_VARIABLE))	{
				String type = element.getChild(QueryXMLConstants.TYPE).getTextTrim();
				String key = element.getChild(QueryXMLConstants.SQL_VARIABLE_KEY).getTextTrim();
				String value = element .getChild(QueryXMLConstants.SQL_VARIABLE_VALUE).getTextTrim();
				String description = element.getChild(QueryXMLConstants.SQL_VARIABLE_DESCRIPTION).getTextTrim();
				setVariable(key, type, value, description);
			}
			if (name.equals(QueryXMLConstants.SQL_RESULT))	{
				String field = element.getChild(QueryXMLConstants.SQL_RESULT_FIELD).getTextTrim();
				String type = element.getChild(QueryXMLConstants.SQL_RESULT_TYPE).getTextTrim();
				String description = element.getChild(QueryXMLConstants.SQL_RESULT_DESCRIPTION).getTextTrim();
				setField(field, type, description);
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
		if (postStatement != null && postStatement.length() != 0) {
			XMLElement postStatementElement = new XMLElement(QueryXMLConstants.SQL_POST_STATEMENT);
			postStatementElement.setText(postStatement);
			sqlElement.addContent(postStatementElement);
		}
		// result
		Iterator fieldIterator = resultFieldOrder.iterator();
		while (fieldIterator.hasNext())	{
			String field = (String) fieldIterator.next();
			String type = (String) resultFieldTypeMap.get(field);
			String description = (String) resultDescriptionMap.get(field); 
			
			XMLElement resultFieldElement = new XMLElement(QueryXMLConstants.SQL_RESULT);
			
			XMLElement fieldElement = new XMLElement(QueryXMLConstants.SQL_RESULT_FIELD);
			fieldElement.setText(field);
			resultFieldElement.addContent(fieldElement);
			
			XMLElement typeElement = new XMLElement(QueryXMLConstants.SQL_RESULT_TYPE);
			typeElement.setText(type);
			resultFieldElement.addContent(typeElement);
			
			XMLElement descriptionElement = new XMLElement(QueryXMLConstants.SQL_RESULT_DESCRIPTION);
			descriptionElement.setText(description);
			resultFieldElement.addContent(descriptionElement);
			
			sqlElement.addContent(resultFieldElement);
		}
			
		
		// variable
		Iterator iterator = keyValueMap.entrySet().iterator();
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			String description = (String) keyDescriptionMap.get(key);
			String type = (String) keyTypeMap.get(key);
			
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
			descriptionElement.setText(description);
			variableElement.addContent(descriptionElement);
			
			sqlElement.addContent(variableElement);
		}
		return sqlElement;
	}

	public void setStatement(String statement)	{
		this.statement = statement;
	}
	
	public String getStatement()	{
		return statement;
	}
	
	public String getPostStatement()	{
		return postStatement;
	}
	
	public Map getVariableValueMap()	{
		return keyValueMap;
	}
	
	public Map getDescriptionValueMap()	{
		return keyDescriptionMap;
	}
	
	public void setVariable(String key, String type,  String value, String description) {
		keyTypeMap.put(key, type);
		keyValueMap.put(key, value);
		keyDescriptionMap.put(key, description);
	}
		
	public void setField(String field, String type, String description)	{
		resultFieldOrder.add(field);
		resultFieldTypeMap.put(field, type);
		resultDescriptionMap.put(field,description);
	}
	
	public List getFieldNames()	{
		return resultFieldOrder;
	}

	public List getFields(String queryName)	{
		List fields = new ArrayList();
		Iterator iterator = resultFieldOrder.iterator();
		while (iterator.hasNext())	{
			String field = (String) iterator.next();
			String type = (String) resultFieldTypeMap.get(field);
			QueryFieldPart fieldPart = new QueryFieldPart(field,queryName,queryName,field, null,field, type, false);
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
