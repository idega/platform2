package com.idega.block.dataquery.data.sql;

import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 10, 2003
 */
public class InnerJoinExpression implements Expression {
	
	public static final String PREFIX=" , "; 
	
	private String alias;
	private String tableName;
	
  public InnerJoinExpression(QueryEntityPart queryEntityPart, SQLQuery sqlQuery) {
    String entity = queryEntityPart.getBeanClassName();
    String path = queryEntityPart.getPath();
    alias = sqlQuery.getUniqueNameForEntity(entity,path);
	tableName = sqlQuery.getTableName(entity, path);
  }
  
  public InnerJoinExpression(String tableName, String path, SQLQuery sqlQuery)	{
  	this.tableName = tableName;
  	alias = sqlQuery.getUniqueNameForEntityByTableName(tableName, path);
  }
  
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#toSQLString()
	 */
	public String toSQLString() {
		StringBuffer buffer = new StringBuffer(" ").append(tableName).append(" ").append(alias).append(" ");
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		return StringHandler.isNotEmpty(alias) && StringHandler.isNotEmpty(tableName);
	}

	public String getTable()	{
		return tableName;
	}
}
