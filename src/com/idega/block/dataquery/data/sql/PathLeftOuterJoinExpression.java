package com.idega.block.dataquery.data.sql;

import java.util.ArrayList;
import java.util.List;

import com.idega.block.dataquery.business.QueryEntityPart;
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
	
  public PathLeftOuterJoinExpression(QueryEntityPart queryEntityPart, QuerySQL querySQL) {
  	super(queryEntityPart, querySQL);
  }
	
	protected void initialize() throws IDOCompositePrimaryKeyException, ExpressionException	{
		outerJoins = new ArrayList();
		super.initialize();
	}

	protected void getConditionManyToManyRelation(IDOEntityDefinition sourceDefinition, IDOEntityDefinition targetDefinition, List criteriaList) throws IDOCompositePrimaryKeyException {
		// many to many relation
		String sourcePrimaryKeyColumnName = sourceDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String targetPrimaryKeyColumnName = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String sourceTableName = sourceDefinition.getSQLTableName();
		String targetTableName = targetDefinition.getSQLTableName();

		// retrieve name of middle table
		Class sourceClass = sourceDefinition.getInterfaceClass();
		Class targetClass = targetDefinition.getInterfaceClass();
		String middleTableName = EntityControl.getManyToManyRelationShipTableName(sourceClass, targetClass);
		
		// retrieve name of middle table
		// String middleTableName = StringHandler.concatAlphabetically(sourceTableName, targetTableName, "_");
		
		LeftOuterJoinExpression middleTableOuterJoin = 
			new LeftOuterJoinExpression(sourceTableName, sourcePrimaryKeyColumnName, middleTableName, sourcePrimaryKeyColumnName, querySQL);
		LeftOuterJoinExpression  targetTableOuterJoin =
			new LeftOuterJoinExpression(middleTableName, targetPrimaryKeyColumnName, targetTableName, targetPrimaryKeyColumnName, querySQL);
		outerJoins.add(middleTableOuterJoin);
		outerJoins.add(targetTableOuterJoin);
	}

	protected void getConditionManyToOneRelation(IDOEntityDefinition sourceDefinition, IDOEntityDefinition targetDefinition, String pathElement, List criteriaList) throws IDOCompositePrimaryKeyException {
		LeftOuterJoinExpression outerJoin = new LeftOuterJoinExpression(sourceDefinition, pathElement, targetDefinition, querySQL);
		outerJoins.add(outerJoin);
	}
	
	public List getOuterJoins()	{
		return outerJoins;
	}


}
