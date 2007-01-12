package com.idega.block.dataquery.data.sql;

import java.util.ArrayList;
import java.util.List;

import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.data.EntityControl;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntityDefinition;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 10, 2003
 */
public class PathLeftOuterJoinExpression extends PathCriterionExpression {
	
	private List outerJoins;
	
  public PathLeftOuterJoinExpression(QueryEntityPart queryEntityPart, SQLQuery sqlQuery) {
  	super(queryEntityPart, sqlQuery);
  }
	
	protected void initialize() throws IDOCompositePrimaryKeyException, ExpressionException	{
		this.outerJoins = new ArrayList();
		super.initialize();
	}

	protected void getConditionManyToManyRelation(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, List criteriaList) throws IDOCompositePrimaryKeyException {
		// many to many relation
		String sourcePrimaryKeyColumnName = sourceDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String targetPrimaryKeyColumnName = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String sourceTableName = sourceDefinition.getSQLTableName();
		String targetTableName = targetDefinition.getSQLTableName();

		// retrieve name of middle table
		Class sourceClass = sourceDefinition.getInterfaceClass();
		Class targetClass = targetDefinition.getInterfaceClass();
		String middleTableName = EntityControl.getManyToManyRelationShipTableName(sourceClass, targetClass);
		
		// just a decision: middle table gets source path
		LeftOuterJoinExpression middleTableOuterJoin = 
			new LeftOuterJoinExpression(sourceTableName, sourcePrimaryKeyColumnName, sourcePath, middleTableName, sourcePrimaryKeyColumnName, sourcePath, this.sqlQuery);
		LeftOuterJoinExpression  targetTableOuterJoin =
			new LeftOuterJoinExpression(middleTableName, targetPrimaryKeyColumnName, sourcePath, targetTableName, targetPrimaryKeyColumnName, targetPath, this.sqlQuery);
		this.outerJoins.add(middleTableOuterJoin);
		this.outerJoins.add(targetTableOuterJoin);
	}

	protected void getConditionManyToOneRelation(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, String pathElement, List criteriaList) throws IDOCompositePrimaryKeyException {
		LeftOuterJoinExpression outerJoin = new LeftOuterJoinExpression(sourceDefinition, pathElement, sourcePath, targetDefinition, targetPath, this.sqlQuery);
		this.outerJoins.add(outerJoin);
	}
	
	public List getOuterJoins()	{
		return this.outerJoins;
	}


}
