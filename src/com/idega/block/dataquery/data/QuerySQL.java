package com.idega.block.dataquery.data;

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
  private Set usedEntities = new HashSet();
  
  // bean class name (String) : (QueryEntityPart)
  private Map entityQueryEntity= new HashMap();
  
  // field name (String) : (QueryFieldPart)
  private Map fieldNameQueryField = new HashMap();
  
  private List fieldOrder = new ArrayList();
  // conditions
  private List conditions = new ArrayList();
  
  private SQLQueryExpression query = null;
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
    
      
  private SQLQueryExpression createQuery(QueryHelper queryHelper) throws IOException {
    
    SQLQueryExpression query = new SQLQueryExpression();
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
    	// mark that this entity is used
    	usedEntities.add(entity);
   		// create expression
   		SQLFunctionExpression functionExpression = new SQLFunctionExpression(queryField, this);
   		if (functionExpression.isValid()) {
    		query.addSelectClause(functionExpression);
   		}
    }
    // set conditions (where clause)
    Iterator conditionsIterator = conditions.iterator();
    while (conditionsIterator.hasNext())  {
      QueryConditionPart condition = (QueryConditionPart) conditionsIterator.next();
      SQLCriterionExpression criterion = new SQLCriterionExpression(condition, this);
      if (criterion.isValid()) {
        query.addWhereClause(criterion);
      }
    }
    // set tables (from clause)
    Iterator entityIterator = entityQueryEntity.values().iterator();
    while (entityIterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) entityIterator.next();
      String entity = queryEntity.getBeanClassName();
      // add only entities that are actually used
      if (usedEntities.contains(entity))  {
        // use alias name
        String alias = getUniqueNameForEntity(entity);
        String table = getTableName(entity);
        query.addFromClause(table, alias);
      }
    }    
    return query;
  }
      
    
 
  private String getTableName(String beanClassName) {
    // performance improvement
    String tableName = (String) beanClassNameTableNameMap.get(beanClassName);
    if (tableName == null)  {
      tableName = GenericEntity.getStaticInstance(beanClassName).getTableName();
      beanClassNameTableNameMap.put(beanClassName, tableName);
    }
    return tableName;
  }
  
  private String getClassOfColumn(String beanClassName, String columnName)  {
    return GenericEntity.getStaticInstance(beanClassName).getStorageClassName(columnName);
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
    
    

	private String getUniqueNameForEntity(String entity)	{
		String tableName = getTableName(entity);
		StringBuffer buffer = new StringBuffer(ALIAS_PREFIX);
		//TODO: thi add something else perhaps the name of that query to handle subqueries
		// buffer.append....
		buffer.append(tableName);		
    return buffer.toString();
  }				
}
