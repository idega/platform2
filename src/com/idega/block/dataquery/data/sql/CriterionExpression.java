package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class CriterionExpression implements Expression {
  
  public static final char DOT = '.';
  public static final char WHITE_SPACE = ' ';
  public static final char APOSTROPHE = '\'';
  private static final String INTEGER = Integer.class.getName(); 
  
  private QueryConditionPart condition = null;
  private QuerySQL querySQL = null;

  private String valueField = null;
  private String firstColumnClass = null;
  private String pattern = null;
  private String comparison = null;
  
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
     
	public CriterionExpression(QueryConditionPart condition, QuerySQL querySQL)	{
		this.condition = condition;
		this.querySQL = querySQL;
		initialize();
	}	
  
  protected void initialize()	{
  	String field = condition.getField();
  	List fieldValueList = querySQL.getUniqueNameForField(field);
  	if (fieldValueList.size() != 1)	{
  		// something wrong
  		return;
  	}
  	valueField = (String) fieldValueList.get(0);
    String type = condition.getType();
    comparison = (String) typeSQL.get(type);
    pattern = condition.getPattern();
  }

  public String toSQLString() {
    StringBuffer expression = 
      new StringBuffer(valueField).append(WHITE_SPACE);
    expression.append(comparison);
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
  
  public boolean isValid() {
    return (
       StringHandler.isNotEmpty(valueField) &&
       StringHandler.isNotEmpty(pattern) &&
       StringHandler.isNotEmpty(comparison));
  } 
    

}
