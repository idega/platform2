package com.idega.block.dataquery.data;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.dataquery.business.QueryConditionPart;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 4, 2003
 */
public class SQLCriterionExpression {
  
  public static final char DOT = '.';
  public static final char WHITE_SPACE = ' ';
  public static final char APOSTROPHE = '\'';
  private static final String INTEGER = Integer.class.getName(); 
  
  private String firstTable = null;
  private String firstColumn = null;
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
     
  
  
  public void add(String table, String column, String columnClass, String pattern, String type)  {
    firstTable = table;
    firstColumn = column;
    firstColumnClass = columnClass;
    this.pattern = pattern;
    comparison = (String) typeSQL.get(type);
  }
    
  public String toSQLString() {
    StringBuffer expression = 
      new StringBuffer(firstTable).append(DOT).append(firstColumn).append(WHITE_SPACE);
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
    return (isValid(firstTable) &&
            isValid(firstColumn) &&
            isValid(pattern) &&
            isValid(comparison));
  } 
    
  private boolean isValid(String string) {
    return (string != null && string.length() > 0);
  }

}
