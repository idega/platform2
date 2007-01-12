package com.idega.block.dataquery.data.sql;

import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntityDefinition;
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
public class LeftOuterJoinExpression implements Expression {

	private String sourceAlias;
	private String sourceKey;
	
	private String targetAlias;
	private String targetKey;
	private String targetTableName;
	
  public LeftOuterJoinExpression(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, SQLQuery sqlQuery) throws IDOCompositePrimaryKeyException {
    this.sourceKey = sourceDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
    initialize(sourceDefinition, sourcePath, targetDefinition, targetPath, sqlQuery);
  }
  
  public LeftOuterJoinExpression(IDOEntityDefinition sourceDefinition, String sourceKey, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, SQLQuery sqlQuery) throws IDOCompositePrimaryKeyException {
    this.sourceKey = sourceKey;
    initialize(sourceDefinition, sourcePath, targetDefinition, targetPath, sqlQuery);
  }
  
  public LeftOuterJoinExpression(String sourceTableName, String sourceKey, String sourcePath, String targetTableName, String targetKey, String targetPath, SQLQuery sqlQuery)	{
  	this.sourceKey = sourceKey;
  	this.targetKey = targetKey;
  	this.targetTableName = targetTableName;
		initialize(sourceTableName, sourcePath, targetPath, sqlQuery);
  }
  	
  	
  
  private void initialize(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, SQLQuery sqlQuery) throws IDOCompositePrimaryKeyException	{
  	this.targetKey = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
 	 	String sourceTableName = sourceDefinition.getSQLTableName();
		this.targetTableName = targetDefinition.getSQLTableName();
  	initialize(sourceTableName, sourcePath, targetPath, sqlQuery);
  } 
  	
  private void initialize(String sourceTableName, String sourcePath, String targetPath, SQLQuery sqlQuery)	{
  	this.sourceAlias = sqlQuery.getUniqueNameForEntityByTableName(sourceTableName, sourcePath);
  	this.targetAlias = sqlQuery.getUniqueNameForEntityByTableName(this.targetTableName, targetPath);
  }

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#toSQLString()
	 */
	public String toSQLString() {
		StringBuffer buffer = new StringBuffer(" LEFT JOIN ");
		buffer.append(this.targetTableName).append(" ").append(this.targetAlias);
		buffer.append(" ON (");
		buffer.append(this.sourceAlias).append(".").append(this.sourceKey);
		buffer.append("=");
		buffer.append(this.targetAlias).append(".").append(this.targetKey);
		buffer.append(") ");		
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		return 
			StringHandler.isNotEmpty(this.sourceAlias) &&
			StringHandler.isNotEmpty(this.targetAlias) &&
			StringHandler.isNotEmpty(this.sourceKey) &&
			StringHandler.isNotEmpty(this.targetKey)	&&
			StringHandler.isNotEmpty(this.targetTableName) ;
	}
	
	public String getTable()	{
		return this.targetTableName;
	} 

}
