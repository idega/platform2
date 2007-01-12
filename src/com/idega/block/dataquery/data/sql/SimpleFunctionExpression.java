package com.idega.block.dataquery.data.sql;

import java.util.HashMap;
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
  private final static String FIELD_VALUE = "FIELD_VALUE";
  
  private String sqlFunctionName = null;
  private String fieldValue = null;
  
  static { 
    FUNCTION_SQL = new HashMap();
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_COUNT, "COUNT("+FIELD_VALUE+")");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_COUNT_DISTINCT, "COUNT( DISTINCT "+FIELD_VALUE+")");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_MAX, "MAX("+FIELD_VALUE+")");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_MIN, "MIN("+FIELD_VALUE+")");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_SUM, "MAX("+FIELD_VALUE+")");
    FUNCTION_SQL.put(QueryXMLConstants.FUNC_AVG,   "AVG("+FIELD_VALUE+")"); 
  }

  protected void initialize(SQLQuery sqlQuery) {
    String function = this.queryField.getFunction();
    this.sqlFunctionName = (String) FUNCTION_SQL.get(function);
    this.fieldValue = sqlQuery.getUniqueNameForField(this.queryField);
  }
    
  
  public String toSQLString() {
  	return StringHandler.replace(this.sqlFunctionName, FIELD_VALUE, this.fieldValue);
  }
    
  public boolean isValid() {
    return ( StringHandler.isNotEmpty(this.sqlFunctionName) && StringHandler.isNotEmpty(this.fieldValue));
  }


}
