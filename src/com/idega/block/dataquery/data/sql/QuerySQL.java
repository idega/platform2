package com.idega.block.dataquery.data.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.block.dataquery.business.QueryEntityPart;
import com.idega.block.dataquery.business.QueryFieldPart;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 30, 2003
 */
public class QuerySQL {
  
  private final String DOT = ".";
  private final String ALIAS_PREFIX = "A_";
  
  // bean class name (String)
  private Set entitiesUsedByField = new HashSet();
  // bean class name (String)
  private Set entitiesUsedByCriterion = new HashSet();
  
  // bean class name (String) : (QueryEntityPart)
  private Map entityQueryEntity= new HashMap();
  
  // field name (String) : (QueryFieldPart)
  private Map fieldNameQueryField = new HashMap();
  
  private List fieldOrder = new ArrayList();
  // conditions
  private List conditions = new ArrayList();
  
  private SelectStatement query = null;
  // caching of table names
  private Map beanClassNameTableNameMap = new HashMap();
  
  public void initialize(QueryHelper queryHelper) {
    try {
      query = createQuery(queryHelper);
    }
    catch (IOException ex)  {
      query = null;
      System.err.println(ex.getMessage());
    }
  }
  
  public String getSQLStatement() {
    if (query == null)  {
      return "";
    }
    else
    {
      return query.toSQLString();
    }
  }
  
  public List getDisplayNames() {
    List displayNames = new ArrayList();
    Iterator fieldOrderIterator = fieldOrder.iterator();
    while (fieldOrderIterator.hasNext())  {
      QueryFieldPart queryField = (QueryFieldPart) fieldOrderIterator.next();
      String display = queryField.getDisplay(); 
      displayNames.add(display);
    }
    return displayNames;
  }

  private void setSourceEntity(QueryHelper queryHelper) {
    QueryEntityPart queryEntity = queryHelper.getSourceEntity();
    if (queryEntity == null)  {
      return;
    }
    String name = queryEntity.getBeanClassName();
    entityQueryEntity.put(name, queryEntity);
  }
    
  private void setRelatedEntities(QueryHelper queryHelper)  {
    List entities = queryHelper.getListOfRelatedEntities();
    if (entities == null) {
      return;
    }
    Iterator iterator = entities.iterator();
    while (iterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) iterator.next();
      String name = queryEntity.getBeanClassName();
      entityQueryEntity.put(name, queryEntity);
      
    }
  }
  
  private void setFields(QueryHelper queryHelper) throws IOException {
    List fields = queryHelper.getListOfFields();
    if (fields == null) {
      return;
    }
    Iterator fieldIterator = fields.iterator();
    while (fieldIterator.hasNext())  {
      QueryFieldPart field = (QueryFieldPart) fieldIterator.next();
      String name = field.getName();
      String entity = field.getEntity();
      fieldNameQueryField.put(name, field);
      // mark that this entity is used
      fieldOrder.add(field);
    }
  }
  
  private void setConditions(QueryHelper queryHelper) {
    List list = queryHelper.getListOfConditions();
    if (list == null) {
      return;
    }
    conditions.addAll(list);
  }
    
      
  private SelectStatement createQuery(QueryHelper queryHelper) throws IOException {
    
    SelectStatement query = new SelectStatement();
    // prepare everything
    setSourceEntity(queryHelper);
    setRelatedEntities(queryHelper);
    setFields(queryHelper);
    setConditions(queryHelper);

    // set fields (select clause)
    Iterator fieldIterator = fieldOrder.iterator(); 
    while (fieldIterator.hasNext()) {
      QueryFieldPart queryField = (QueryFieldPart) fieldIterator.next();
      String entity = queryField.getEntity();
      // test if entity is supported
      if (! entityQueryEntity.containsKey(entity))  {
        throw new IOException("[QuerySQL] criteria could not be created, table is unknown");
      }
   		// create expression
   		FunctionExpression functionExpression = FunctionExpression.getInstance(queryField, this);
   		if (functionExpression.isValid()) {
   			// mark used entity
   			entitiesUsedByField.add(entity);
    		query.addSelectClause(functionExpression);
   		}
    }
    // set conditions (where clause)
    Iterator conditionsIterator = conditions.iterator();
    while (conditionsIterator.hasNext())  {
      QueryConditionPart condition = (QueryConditionPart) conditionsIterator.next();
      CriterionExpression criterion = new CriterionExpression(condition, this);
      if (criterion.isValid()) {
      	// mark used entities
      	String fieldName = condition.getField();
      	String entityName = getEntityForField(fieldName);
      	entitiesUsedByCriterion.add(entityName);
        query.addWhereClause(criterion);
      }
    }
    // set tables (from clause)
    Iterator entityIterator = entityQueryEntity.values().iterator();
    List innerJoins = new ArrayList();
    List outerJoins = new ArrayList();
    while (entityIterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) entityIterator.next();
      String entity = queryEntity.getBeanClassName();
      // add only entities that are actually used
      if (entitiesUsedByCriterion.contains(entity))	{
      	// if an entity is used by a criterion use strong conditions, that is do not use left outer join
      	PathCriterionExpression pathCriterionExpression = new PathCriterionExpression(queryEntity, this);
        if (pathCriterionExpression.isValid())	{
      		List joins = pathCriterionExpression.getInnerJoins();
      		innerJoins.addAll(joins);
      		if (pathCriterionExpression.hasCriteria())	{
      			// if the path contains only one entity there are't any criterias 
	        	query.addWhereClause(pathCriterionExpression);
      		}
        }
      }
      else if (entitiesUsedByField.contains(entity))	{
      	// if an entity is used by a select field use weak conditions, that is use left outer join
      	PathLeftOuterJoinExpression pathLeftOuterJoinExpression = new PathLeftOuterJoinExpression(queryEntity, this);
      	if (pathLeftOuterJoinExpression.isValid())	{
      		List outJoins = pathLeftOuterJoinExpression.getOuterJoins();
      		outerJoins.addAll(outJoins);
      		List inJoins = pathLeftOuterJoinExpression.getInnerJoins();
      		innerJoins.addAll(inJoins);
      	}
      }
    }
    // iterate over inner and outer joins
    List addedTables = new ArrayList();
    Iterator inner = innerJoins.iterator();
    while (inner.hasNext()) {
			InnerJoinExpression element = (InnerJoinExpression) inner.next();
			String table = element.getTable();
			if (! addedTables.contains(table)) {
				addedTables.add(table);
				query.addInnerJoin(element);
			}
    }		
    Iterator outer = outerJoins.iterator();
    while (outer.hasNext()) {
			LeftOuterJoinExpression element = (LeftOuterJoinExpression) outer.next();
			String table = element.getTable();
			if (! addedTables.contains(table)) {
				addedTables.add(table);
				query.addOuterJoin(element);
			}
    }		
    return query;
  }
      
    
 
  protected String getTableName(String beanClassName) {
    // performance improvement
    String tableName = (String) beanClassNameTableNameMap.get(beanClassName);
    if (tableName == null)  {
      tableName = 
        ((IDOEntity) GenericEntity.getStaticInstance(beanClassName)).getEntityDefinition().getSQLTableName();
      beanClassNameTableNameMap.put(beanClassName, tableName);
    }
    return tableName;
  }
  
  protected List getUniqueNameForField(QueryFieldPart queryFieldPart)	{
  	List result = new ArrayList();
  	if (queryFieldPart == null) 	{
  		return result;
  	}
		String entityName = queryFieldPart.getEntity();
		String uniqueName = getUniqueNameForEntity(entityName);
		String[] columns = queryFieldPart.getColumns();
		for (int i = 0; i < columns.length; i++) {
			String columnName = columns[i];
			StringBuffer buffer = new StringBuffer(uniqueName);
			buffer.append(DOT).append(columnName);
			String uniqueColumnName = buffer.toString();
			result.add(uniqueColumnName);
		}
		return result;
  }

  protected List getUniqueNameForField(String fieldName)  {
    QueryFieldPart field = (QueryFieldPart) fieldNameQueryField.get(fieldName);
    return getUniqueNameForField(field);
  }
    
  protected String getUniqueNameForEntityByTableName(String tableName)	{
		StringBuffer buffer = new StringBuffer(ALIAS_PREFIX);
		//TODO: thi add something else perhaps the name of that query to handle subqueries
		// buffer.append....
		buffer.append(tableName);		
    return buffer.toString();
  }

	protected String getUniqueNameForEntity(String entity)	{
		String tableName = getTableName(entity);
		return getUniqueNameForEntityByTableName(tableName);
	}		
	
	protected String getEntityForField(String fieldName)	{
		QueryFieldPart field = (QueryFieldPart) fieldNameQueryField.get(fieldName);
		return field.getEntity();
	}
}
