package com.idega.block.dataquery.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
  
  // source
  private String sourceEntityTable;
  // tables
  private Map entityTable = new HashMap();
  // select fields
  private Map fieldColumns = new HashMap();
  private Map fieldTable = new HashMap();
  private Map fieldDisplayName = new HashMap();
  private Map fieldFunction = new HashMap();
  
  private SQLQueryExpression query = null;
  
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

  private void setSourceEntity(QueryHelper queryHelper) {
    QueryEntityPart queryEntity = queryHelper.getSourceEntity();
    if (queryEntity == null)  {
      return;
    }
    sourceEntityTable = queryEntity.getBeanClassName();
    String tableName = getTableName(sourceEntityTable);
    String name = queryEntity.getName();
    entityTable.put(name, tableName);
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
      String beanClassName = queryEntity.getBeanClassName();
      String tableName = getTableName(beanClassName);
      entityTable.put(name, tableName);
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
      String displayName = field.getDisplay();
      String function = field.getFunction();
      String entity = field.getEntity();
      String table = (String) entityTable.get(entity);
      if (table == null)  {
        throw new IOException("[QuerySQL] Entity is unknown");
      }
      // properties are column names
      String[] columns = field.getColumns();
      fieldColumns.put(name, Arrays.asList(columns));
      fieldTable.put(name, table);
      fieldDisplayName.put(name, displayName);
      fieldFunction.put(name, function);
    }
  }
      
  private SQLQueryExpression createQuery(QueryHelper queryHelper) throws IOException {
    
    SQLQueryExpression query = new SQLQueryExpression();
    
    // prepare everything
    setSourceEntity(queryHelper);
    setRelatedEntities(queryHelper);
    setFields(queryHelper);
    
    // set tables (from clause)
    query.addFromClauses(entityTable.values());

    
    // set fields (select clause)
    Iterator columnsIterator = fieldColumns.entrySet().iterator();
    while (columnsIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) columnsIterator.next();
      Collection columns = (Collection) entry.getValue();
      String field = (String) entry.getKey();
      String table = (String) fieldTable.get(field);
      if (table == null)  {
        throw new IOException("[QuerySQL] criteria could not be created, table is unknown");
      }
      StringBuffer tableWithDot = new StringBuffer(table).append(DOT);
      // add table name to each column
      Iterator columnIterator = columns.iterator();
      while (columnIterator.hasNext())  {
        String column = (String) columnIterator.next();
        String tableDotColumn = tableWithDot.append(column).toString(); 
        query.addSelectClause(tableDotColumn);
      }
    }
    
    // set conditions (where clause)
    List conditionList = queryHelper.getListOfConditions();
    Iterator conditionsIterator = conditionList.iterator();
    while (conditionsIterator.hasNext())  {
      QueryConditionPart condition = (QueryConditionPart) conditionsIterator.next();
      SQLCriterionExpression criterion = createCriterion(condition);
      query.addWhereClause(criterion);
    }
    return query;
  }
      
    
  private SQLCriterionExpression createCriterion(QueryConditionPart condition) throws IOException {

    String field = condition.getField();
    String table = (String) fieldTable.get(field);
    if (table == null)  {
      throw new IOException("[QuerySQL] criteria could not be created, table is unknown");
    }
    List columns = (List) fieldColumns.get(field);
    if (columns == null || columns.size() != 1) {
      throw new IOException("[QuerySQL] criteria could not be created, column is unknown");
    }
    // get the only one
    String column = (String) columns.get(0);
    String type = condition.getType();
    SQLCriterionExpression criterion = new SQLCriterionExpression();
    String pattern = condition.getPattern();
    criterion.add(table, column, pattern, type);
    return criterion;
  }
  
  private String getTableName(String beanClassName) {
    return GenericEntity.getStaticInstance(beanClassName).getTableName();
  }
    
}
