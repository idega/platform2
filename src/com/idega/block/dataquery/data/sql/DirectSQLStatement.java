package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.idega.block.dataquery.data.xml.QuerySQLPart;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Nov 4, 2003
 */
public class DirectSQLStatement implements DynamicExpression {
	
	static public final String UNIQUE_IDENTIFIER = "UNIQUE_IDENTIFIER"; 
	
	private String sqlStatement;
	
	private String uniqueIdentifier;
	
	private String postStatement; 
	
	private Map identifierValueMap = new HashMap();
  private Map identifierInputDescriptionMap = new HashMap(); 
  
  private Set keys;
  
  public DirectSQLStatement(QuerySQLPart sqlPart, Object identifier, String uniqueIdentifier, SQLQuery sqlQuery)	{
  	sqlStatement = sqlPart.getStatement();
  	Map variableValueMap = sqlPart.getVariableValueMap();
  	identifierValueMap.putAll(variableValueMap);
  	identifierInputDescriptionMap.putAll(sqlPart.getInputDescriptionValueMap());
  	keys = variableValueMap.keySet();
  	this.uniqueIdentifier = uniqueIdentifier.toString();
  	postStatement = sqlPart.getPostStatement();
  }

	public void setSQLStatement(String sqlStatement) 	{
		this.sqlStatement = sqlStatement;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#isDynamic()
	 */
	public boolean isDynamic() {
		return ! keys.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#getIdentifierValueMap()
	 */
	public Map getIdentifierValueMap() {
		return identifierValueMap;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#getIdentifierDescriptionMap()
	 */
	public Map getIdentifierInputDescriptionMap() {
		return identifierInputDescriptionMap;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#setIdentifierValueMap(java.util.Map)
	 */
	public void setIdentifierValueMap(Map identifierValueMap) {
		this.identifierValueMap = identifierValueMap;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#toSQLString()
	 */
	public String toSQLString() {
		Iterator iterator = identifierValueMap.entrySet().iterator();
		String result = StringHandler.replace(sqlStatement, UNIQUE_IDENTIFIER, uniqueIdentifier);
		while (iterator.hasNext())	{
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			if (keys.contains(key)) {
				String value = (String) entry.getValue();
				result = StringHandler.replace(result, key, value);
			}
		}
		return result;
	}
	
	public String getPostStatement() {
		return (postStatement == null) ? null : StringHandler.replace(postStatement, UNIQUE_IDENTIFIER, uniqueIdentifier);
	}
		
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		// TODO finish implementation
		return true;
	}

}
