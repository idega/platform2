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
	
	private QuerySQL querySQL = null;
	
	private String alias;
	private String tableName;
	
  public InnerJoinExpression(QueryEntityPart queryEntityPart, QuerySQL querySQL) {
    this.querySQL = querySQL;
    String entity = queryEntityPart.getBeanClassName();
    String path = queryEntityPart.getPath();
    alias = querySQL.getUniqueNameForEntity(entity, path);
		tableName = querySQL.getTableName(entity);
  }
  
  public InnerJoinExpression(String tableName, String path, QuerySQL querySQL)	{
  	this.querySQL = querySQL;
  	this.tableName = tableName;
  	alias = querySQL.getUniqueNameForEntityByTableName(tableName, path);
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
