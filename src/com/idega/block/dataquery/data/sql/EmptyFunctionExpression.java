package com.idega.block.dataquery.data.sql;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 7, 2003
 */
public class EmptyFunctionExpression extends FunctionExpression {

  private String fieldValue = null;
  
  protected void initialize(SQLQuery sqlQuery) {
    fieldValue = sqlQuery.getUniqueNameForField(queryField);
  }
    
  
  public String toSQLString() {
  	return fieldValue;
  }
    
  public boolean isValid() {
    return (fieldValue != null && fieldValue.length() != 0);
  } 
  

}
