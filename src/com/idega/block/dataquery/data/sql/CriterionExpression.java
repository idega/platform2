package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.data.xml.QueryConditionPart;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class CriterionExpression implements DynamicExpression {
  
  public static final char DOT = '.';
  public static final char WHITE_SPACE = ' ';
  public static final char APOSTROPHE = '\'';
  private static final String INTEGER = Integer.class.getName(); 
  

  private Object identifier = null;
  private String id = null;

  private String valueField = null;
  private String firstColumnClass = null;
  private String comparison = null;
  
  private boolean isDynamic = false;
  private Map identifierValueMap = null;
  private Map identifierDescriptionMap = null;
  
  private Map typeSQL = null;
  
  { 
    typeSQL = new HashMap();
    typeSQL.put(QueryConditionPart.TYPE_LIKE, "LIKE");
    typeSQL.put(QueryConditionPart.TYPE_EQ, "=");
    typeSQL.put(QueryConditionPart.TYPE_NEQ, "<>");
    typeSQL.put(QueryConditionPart.TYPE_LT, "<");
    typeSQL.put(QueryConditionPart.TYPE_GT, ">");    
    typeSQL.put(QueryConditionPart.TYPE_GEQ, ">=");
    typeSQL.put(QueryConditionPart.TYPE_LEQ, "<=");
  }
     
	public CriterionExpression(QueryConditionPart condition, Object identifier, SQLQuery sqlQuery)	{
		this.id = condition.getId();
		this.identifier = identifier;
		initialize(condition, sqlQuery);
	}	
  
  protected void initialize(QueryConditionPart condition, SQLQuery sqlQuery)	{
  	String field = condition.getField();
  	String path = condition.getPath();
  	List fieldValueList = sqlQuery.getUniqueNameForField(path,field);
  	if (fieldValueList.size() != 1)	{
  		// something wrong
  		return;
  	}
  	//String firstColumnClass = querySQL.getTypeClassForField(field);
  	valueField = (String) fieldValueList.get(0);
    String type = condition.getType();
    comparison = (String) typeSQL.get(type);
    String pattern = condition.getPattern();
    isDynamic = condition.isDynamic();
  	identifierValueMap = new HashMap();
   	identifierValueMap.put(identifier, pattern);
   	String description = condition.getDescription();
   	identifierDescriptionMap = new HashMap();
   	if (description == null || description.length() == 0)	{
  		StringBuffer buffer = new StringBuffer(valueField).append(" ").append(type);
  		description =  buffer.toString();
   	}
		identifierDescriptionMap.put(identifier, description);
  }

  public String toSQLString() {
    StringBuffer expression = 
      new StringBuffer(valueField).append(WHITE_SPACE);
    expression.append(comparison);
    String pattern = (String) identifierValueMap.get(identifier);
    if (pattern != null)  {
      expression.append(WHITE_SPACE);
      if (INTEGER.equals(firstColumnClass)) {
        expression.append(pattern);
      }
      else {
        expression.append(APOSTROPHE).append(pattern).append(APOSTROPHE);
      }
    }
    return expression.toString();
  }
  
  public boolean isDynamic() {
  	return isDynamic;
  }
  
  public String getId()	{
  	return id;
  }
  
  public Map getIdentifierValueMap() {
  	return identifierValueMap;
  }
  
  public Map getIdentifierDescriptionMap()	{
  	return identifierDescriptionMap;
  }
  
  public void setIdentifierValueMap(Map identifierValueMap)	{
  	this.identifierValueMap = identifierValueMap;
  }
  
  public boolean isValid() {
    return true;
//    (
//       StringHandler.isNotEmpty(valueField) &&
//       StringHandler.isNotEmpty(pattern) &&
//       StringHandler.isNotEmpty(comparison));
  } 
    

}
