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

	private IDOEntityDefinition sourceDefinition = null;
	private IDOEntityDefinition targetDefinition = null;
	private QuerySQL querySQL = null;

	private String sourceAlias;
	private String sourceKey;
	
	private String targetAlias;
	private String targetKey;
	private String targetTableName;
	
  public LeftOuterJoinExpression(IDOEntityDefinition sourceDefinition, IDOEntityDefinition targetDefinition, QuerySQL querySQL) throws IDOCompositePrimaryKeyException {
    this.querySQL = querySQL;
    sourceKey = sourceDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
    initialize(sourceDefinition, targetDefinition);
  }
  
  public LeftOuterJoinExpression(IDOEntityDefinition sourceDefinition, String sourceKey, IDOEntityDefinition targetDefinition, QuerySQL querySQL) throws IDOCompositePrimaryKeyException {
    this.querySQL = querySQL;
    this.sourceKey = sourceKey;
    initialize(sourceDefinition, targetDefinition);
  }
  
  public LeftOuterJoinExpression(String sourceTableName, String sourceKey, String targetTableName, String targetKey, QuerySQL querySQL)	{
  	this.querySQL = querySQL;
  	this.sourceKey = sourceKey;
  	this.targetKey = targetKey;
  	this.targetTableName = targetTableName;
		initialize(sourceTableName);
  }
  	
  	
  
  private void initialize(IDOEntityDefinition sourceDefinition, IDOEntityDefinition targetDefinition) throws IDOCompositePrimaryKeyException	{
  	targetKey = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
 	 	String sourceTableName = sourceDefinition.getSQLTableName();
		targetTableName = targetDefinition.getSQLTableName();
  	initialize(sourceTableName);
  } 
  	
  private void initialize(String sourceTableName)	{
  	sourceAlias = querySQL.getUniqueNameForEntityByTableName(sourceTableName);
  	targetAlias = querySQL.getUniqueNameForEntityByTableName(targetTableName);
  }

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#toSQLString()
	 */
	public String toSQLString() {
		StringBuffer buffer = new StringBuffer(" LEFT JOIN ");
		buffer.append(targetTableName).append(" ").append(targetAlias);
		buffer.append(" ON (");
		buffer.append(sourceAlias).append(".").append(sourceKey);
		buffer.append("=");
		buffer.append(targetAlias).append(".").append(targetKey);
		buffer.append(") ");		
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		return 
			StringHandler.isNotEmpty(sourceAlias) &&
			StringHandler.isNotEmpty(targetAlias) &&
			StringHandler.isNotEmpty(sourceKey) &&
			StringHandler.isNotEmpty(targetKey)	&&
			StringHandler.isNotEmpty(targetTableName) ;
	}
	
	public String getTable()	{
		return targetTableName;
	} 

}
