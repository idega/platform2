package com.idega.block.dataquery.data.sql;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 6, 2003
 */
public class ConcatenationExpression extends FunctionExpression {
  
   protected void initialize(SQLQuery sqlQuery) {
   }
  
  public String toSQLString() {
    return "";
  } 
    
  public boolean isValid() {
    return false;
  } 


}
