package com.idega.block.dataquery.data;

import java.util.List;

import com.idega.block.dataquery.business.QueryFieldPart;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 2, 2003
 */
public class SQLFunctionExpression {
	
	private QueryFieldPart queryField = null;
	private QuerySQL querySQL = null;
	
	public SQLFunctionExpression(QueryFieldPart queryField, QuerySQL querySQL)	{
		this.queryField = queryField;
		this.querySQL = querySQL;
	}
	
  public String toSQLString() {
  	List result = querySQL.getUniqueNameForField(queryField);
  	return (String) result.get(0);
  }
  	
  public boolean isValid() {
    return true;
  } 
    
  private boolean isValid(String string) {
    return (string != null && string.length() > 0);
  }



}
