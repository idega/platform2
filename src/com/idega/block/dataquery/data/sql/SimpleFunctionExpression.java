package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.data.xml.QueryXMLConstants;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 6, 2003
 */
public class SimpleFunctionExpression extends FunctionExpression {
  
  private final static Map FUNCTION_SQL;
  
  private String sqlFunctionName = null;
  private String fieldValue = null;
  
  static { 
    FUNCTION_SQL = new HashMap();
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_COUNT, "COUNT");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_MAX, "MAX");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_MIN, "MIN");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_SUM, "MAX");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_AVG,   "AVG"); 
  }

  protected void initialize(SQLQuery sqlQuery) {
    String function = queryField.getFunction();
    sqlFunctionName = (String) FUNCTION_SQL.get(function);
    List fieldValueList = sqlQuery.getUniqueNameForField(queryField);
    if (fieldValueList.size() != 1) {
      // something wrong
      return;
    }
    fieldValue = (String) fieldValueList.get(0);
  }
    
  
  public String toSQLString() {
    StringBuffer buffer = new StringBuffer(sqlFunctionName);
    buffer.append('(').append(fieldValue).append(')');
    return buffer.toString();
  }
    
  public boolean isValid() {
    return ( StringHandler.isNotEmpty(sqlFunctionName) && StringHandler.isNotEmpty(fieldValue));
  }


}
