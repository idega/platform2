package com.idega.block.dataquery.data.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.dataquery.business.QueryEntityPart;
import com.idega.data.EntityControl;
import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDORelationshipException;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 8, 2003
 */
public class PathCriterionExpression implements DynamicExpression {
  
  protected QueryEntityPart queryEntityPart = null;
  protected QuerySQL querySQL = null;
  
  private List criteriaList = null;
  private List innerJoins = new ArrayList();
  
  public PathCriterionExpression(QueryEntityPart queryEntityPart, QuerySQL querySQL) {
    this.queryEntityPart = queryEntityPart;
    this.querySQL = querySQL;
    try {
    	initialize();
    }
    catch (ExpressionException ex)	{
    	criteriaList = null;
    } 
    catch (IDOCompositePrimaryKeyException e) {
    	criteriaList = null;
		}
  }
  
  protected void initialize() throws IDOCompositePrimaryKeyException, ExpressionException {
  	// the very first name is always a class
  	List pathElements = queryEntityPart.getPathNames();
    String className = (String) pathElements.get(0);
    IDOEntity entity = getInstance(className);
    IDOEntityDefinition definition = entity.getEntityDefinition();

    String tableName = definition.getSQLTableName();
    String targetPath = (String) pathElements.get(0);
		innerJoins.add(new InnerJoinExpression(tableName, targetPath, querySQL));			      

    if (pathElements.size() > 1) {
    	criteriaList = new ArrayList();
    	// start with the first element

    	getConditions(definition, targetPath, pathElements, 1, criteriaList);
    }
  }
    
	private void getConditions(IDOEntityDefinition sourceDefinition, String targetPath, List pathElements, int listIndex, List criteriaList)	
			throws ExpressionException, IDOCompositePrimaryKeyException {
		String pathElement = (String) pathElements.get(listIndex);
		String sourcePath = targetPath;
		// new target 
		targetPath = new StringBuffer(targetPath).append("#").append(pathElement).toString();
		IDOEntityDefinition targetDefinition =
			lookForTargetEntityAmongManyToManyRelations(sourceDefinition, pathElement);
		if (targetDefinition == null)	{
			// many to one relation
			targetDefinition = lookForTargetEntityAmongManyToOneRelations(sourceDefinition, pathElement);
			if (targetDefinition == null)  {
				String message = "[" + this.getClass().getName() + "] Path element could not be resolved.";
				throw new ExpressionException(message);
			}
			getConditionManyToOneRelation(sourceDefinition, sourcePath , targetDefinition, targetPath, pathElement, criteriaList);
		}
		else {
			getConditionManyToManyRelation(sourceDefinition, sourcePath, targetDefinition, targetPath, criteriaList);
		}
		if (pathElements.size() > ++listIndex)	{
			getConditions(targetDefinition, targetPath, pathElements, listIndex, criteriaList);
		}
	}

	protected void getConditionManyToManyRelation(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, List criteriaList) throws IDOCompositePrimaryKeyException {
		// many to many relation
		String sourcePrimaryKeyColumnName = sourceDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String targetPrimaryKeyColumnName = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String sourceTableName = sourceDefinition.getSQLTableName();
		String targetTableName = targetDefinition.getSQLTableName();
		
		// get aliases
		String aliasSourceTableName = querySQL.getUniqueNameForEntityByTableName(sourceTableName, sourcePath);
		String aliasTargetTableName = querySQL.getUniqueNameForEntityByTableName(targetTableName, targetPath);
		
		// retrieve name of middle table
		Class sourceClass = sourceDefinition.getInterfaceClass();
		Class targetClass = targetDefinition.getInterfaceClass();
		String middleTable = EntityControl.getManyToManyRelationShipTableName(sourceClass, targetClass);
		//String middleTable = StringHandler.concatAlphabetically(sourceTableName, targetTableName, "_");
		// just a decision: middle table gets the source path
		String aliasMiddleTable = querySQL.getUniqueNameForEntityByTableName(middleTable, sourcePath);
		
		// build the condition 
		StringBuffer buffer = new StringBuffer(" (");
		buffer.append(aliasSourceTableName);
		buffer.append('.').append(sourcePrimaryKeyColumnName);
		buffer.append("=");
		buffer.append(aliasMiddleTable);
		buffer.append('.').append(sourcePrimaryKeyColumnName);
		buffer.append(" AND ");
		buffer.append(aliasMiddleTable);
		buffer.append('.').append(targetPrimaryKeyColumnName);
		buffer.append("=");
		buffer.append(aliasTargetTableName);
		buffer.append('.').append(targetPrimaryKeyColumnName);
		buffer.append(") ");
		
		// add the middle table to the query
		innerJoins.add(new InnerJoinExpression(middleTable, sourcePath, querySQL));
		// add the target table to the query
		innerJoins.add(new InnerJoinExpression(targetTableName, targetPath, querySQL));
		
		criteriaList.add(buffer.toString());
	}

	protected void getConditionManyToOneRelation(IDOEntityDefinition sourceDefinition, String sourcePath, IDOEntityDefinition targetDefinition, String targetPath, String pathElement, List criteriaList) throws IDOCompositePrimaryKeyException {
		String targetPrimaryKeyColumnName = targetDefinition.getPrimaryKeyDefinition().getField().getSQLFieldName();
		String targetTableName = targetDefinition.getSQLTableName();
		String sourceTableName = sourceDefinition.getSQLTableName();
		
		// get aliases
		String aliasSourceTableName = querySQL.getUniqueNameForEntityByTableName(sourceTableName, sourcePath);
		String aliasTargetTableName = querySQL.getUniqueNameForEntityByTableName(targetTableName, targetPath);
					
		// build the condition
		StringBuffer buffer = new StringBuffer(aliasSourceTableName);
		buffer.append('.').append(pathElement);
		buffer.append("=");
		buffer.append(aliasTargetTableName).append('.');
		buffer.append(targetPrimaryKeyColumnName);
		
		// add the target table to the query
		innerJoins.add(new InnerJoinExpression(targetTableName, targetPath, querySQL));			
		
		criteriaList.add(buffer.toString());
	}

	private IDOEntityDefinition lookForTargetEntityAmongManyToManyRelations(IDOEntityDefinition definition, String pathElement)	{
		List manyToManyEntities = Arrays.asList(definition.getManyToManyRelatedEntities());
		Iterator manyToManyEntitiesIterator = manyToManyEntities.iterator();
		while (manyToManyEntitiesIterator.hasNext())	{
			IDOEntityDefinition def = (IDOEntityDefinition) manyToManyEntitiesIterator.next();
			String className = def.getInterfaceClass().getName();
			if (pathElement.equals(className)) {
				return def;
			}
		}
		return null;
	}
	
	private IDOEntityDefinition lookForTargetEntityAmongManyToOneRelations(IDOEntityDefinition definition, String pathElement) 
			throws ExpressionException {
		List fields = Arrays.asList(definition.getFields());
		Iterator fieldsIterator = fields.iterator();
		while (fieldsIterator.hasNext())	{
			IDOEntityField field = (IDOEntityField) fieldsIterator.next();
			if (field.isPartOfManyToOneRelationship() && field.getSQLFieldName().equalsIgnoreCase(pathElement))	{
				IDOEntityDefinition def = null;
				try {
					def = field.getManyToOneRelated();
					return def;
				}
				catch (IDORelationshipException ex)	{
					String message = "[" + this.getClass().getName() + "] Problem occured during investigation of the following field: " + 
						pathElement  + " Message is: " + ex.getMessage();
					System.err.println(message);
					ex.printStackTrace(System.err);
					String text = "[" + this.getClass().getName() + "] Path element could not be resolved.";
						throw new ExpressionException(text);
				}
			}
		}
		return null;
	}


  private IDOEntity getInstance(String className) throws ExpressionException {
    Class entityClass = null;
    try {
      entityClass = Class.forName(className);
    }
    catch (ClassNotFoundException ex) {
    //TODO: thi add exception handling
    String message = "[" + this.getClass().getName() + "] Class could not be found";
    	throw new ExpressionException(message);
    }
    IDOEntity idoEntity = GenericEntity.getStaticInstanceIDO(entityClass);
    return idoEntity;
  }    
    
	
	public List getInnerJoins()	{
		return innerJoins;
	}
	
	public List getCriteria()	{
		return criteriaList;
	}
	
	public boolean hasCriteria()	{
		return !(criteriaList == null || criteriaList.isEmpty());
	}
		    
  
  public String toSQLString()  {
  	StringBuffer buffer = new StringBuffer();
  	Iterator criteriaListIterator = criteriaList.iterator();
  	boolean addAnd = false;
  	while (criteriaListIterator.hasNext())	{
  		if (addAnd) {
  			buffer.append(" AND ");
  		}
  		else {
  			addAnd = true;
  		}
  		String criteria = (String) criteriaListIterator.next();
  		buffer.append(criteria);
  	}
  	return buffer.toString();
  }
  
  public boolean isValid()  {
  	return true;
  	// think about that
  	//return  (criteriaList != null && innerJoins != null);
  }

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#isDynamic()
	 */
	public boolean isDynamic() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.DynamicExpression#getIdentifier()
	 */
	public Map getIdentifierValueMap() {
		// never used because isDynamic returns always false
		return null;
	}
	
	public Map getIdentifierDescriptionMap()	{
		// never used because isDynamic returns always false
		return null;
	}

	public void setIdentifierValueMap(Map identifierValueMap)	{
		// never used because isDynamic returns always false
	}	
     

}
