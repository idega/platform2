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
  
  private Set usedEntities = new HashSet();
  // source
  private String sourceEntityTable;
  // entities
  private Map entityQueryEntity = new HashMap();
  // select fields
  private Map fieldQueryField = new HashMap();
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
      String fieldName  = (String) fieldOrderIterator.next();
      QueryFieldPart field = (QueryFieldPart) fieldQueryField.get(fieldName);
      String display = field.getDisplay(); 
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
      String name = queryEntity.getName();
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
      fieldQueryField.put(name, field);
      fieldOrder.add(name);
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
      String name = (String) fieldIterator.next();
      QueryFieldPart queryField = (QueryFieldPart) fieldQueryField.get(name);
      String[] columns = queryField.getColumns();
      String entity = queryField.getEntity();
      // alias test
      if (! entityQueryEntity.containsKey(entity))  {
        throw new IOException("[QuerySQL] criteria could not be created, table is unknown");
      }
      // note that this entity is used
	  usedEntities.add(entity);
      // use alias name
      String alias = getUniqueAliasName(entity);
      StringBuffer aliasPlusDot = new StringBuffer(alias);
      aliasPlusDot.append(DOT);
      // add alias name to each column
      int i;
      for (i=0; i < columns.length ; i++) {
        String column = columns[i];
        StringBuffer aliasPlusDotColumn = new StringBuffer();
        aliasPlusDotColumn.append(aliasPlusDot).append(column);
        String aliasPlusDotColumnAsString = aliasPlusDotColumn.toString();
        query.addSelectClause(aliasPlusDotColumnAsString);
      }
    }
    // set conditions (where clause)
    Iterator conditionsIterator = conditions.iterator();
    while (conditionsIterator.hasNext())  {
      QueryConditionPart condition = (QueryConditionPart) conditionsIterator.next();
      SQLCriterionExpression criterion = createCriterion(condition);
      if (criterion.isValid()) {
        query.addWhereClause(criterion);
      }
    }
    // set tables (from clause)
    Iterator entityIterator = entityQueryEntity.values().iterator();
    while (entityIterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) entityIterator.next();
      String table = getTableName(queryEntity.getBeanClassName());
      String entity = queryEntity.getBeanClassName();
      // add only entities that are actually used
      if (usedEntities.contains(entity))  {
        // use alias name
        String alias = getUniqueAliasName(entity);
        query.addFromClause(table, alias);
      }
    }    
    return query;
  }
      
    
  private SQLCriterionExpression createCriterion(QueryConditionPart condition) throws IOException {

    String field = condition.getField();
    QueryFieldPart queryField = (QueryFieldPart) fieldQueryField.get(field);
    if (queryField == null) {
      throw new IOException("[QuerySQL] criteria could not be created, field is unknown");
    }
    String entity = queryField.getEntity();
    QueryEntityPart queryEntity = (QueryEntityPart) entityQueryEntity.get(entity);
    if (queryEntity == null) {
      throw new IOException("[QuerySQL] criteria could not be created, table is unknown");
    }
    // note that this entity is used
    usedEntities.add(entity);
    String[] columns = queryField.getColumns();
    if (columns == null || columns.length != 1) {
      throw new IOException("[QuerySQL] criteria could not be created, column is unknown");
    }
    // get the only one
    String column = columns[0];
    String type = condition.getType();
    String pattern = condition.getPattern();
    // get columnclass
    String columnClass = getClassOfColumn(queryEntity.getBeanClassName(), column);
    SQLCriterionExpression criterion = new SQLCriterionExpression();
    // use alias name
    String alias = getUniqueAliasName(entity);
    criterion.add( alias , column, columnClass, pattern, type);
    return criterion;
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
  
  private String getUniqueAliasName(String beanClassName) {
    StringBuffer buffer = new StringBuffer(ALIAS_PREFIX);
    buffer.append(getTableName(beanClassName));
    return buffer.toString();
  }
    
    
}
