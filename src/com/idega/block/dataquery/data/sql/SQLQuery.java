package com.idega.block.dataquery.data.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.idega.block.dataquery.data.xml.QueryBooleanExpressionPart;
import com.idega.block.dataquery.data.xml.QueryConditionPart;
import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.dataquery.data.xml.QueryOrderConditionPart;
import com.idega.block.dataquery.data.xml.QuerySQLPart;
import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;
import com.idega.util.datastructures.HashMatrix;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 30, 2003
 */
public class SQLQuery implements DynamicExpression {
	
	public static final String DYNAMIC_FIELD_VALUE_PREFIX = "dynamic_field_value_";
	public static final String DYNAMIC_FIELD_DESCRIPTION_PREFIX = "dynamic_field_description_";
  
  private final String DOT = ".";
  private final String ALIAS_PREFIX = "A_";
  
  private String name;
  
  private String uniqueIdentifier;
  
  private String postStatement = null;
  
  // tablename : path : number
  private HashMatrix aliasMatrix = new HashMatrix();
  private int counter = 0;
  
  // path (String)
  private Set entitiesUsedByField = new HashSet();
  // path (String)
  private Set entitiesUsedByCriterion = new HashSet();
  
  // path (String) : (QueryEntityPart)
  private Map entityQueryEntity= new HashMap();
  
  // field name (String) : (QueryFieldPart)
  private Map fieldNameQueryField = new HashMap();
  
  private List fieldOrder = new ArrayList();
  // conditions
  private List conditions = new ArrayList();
  
  // order of conditions
  private SortedSet orderConditions = 
  	new TreeSet(new Comparator() { 
  		public int  compare(Object first, Object second) {
  			return ((QueryOrderConditionPart) first).compare((QueryOrderConditionPart) second);
  		}
  	});
  
  private DynamicExpression query = null;
  
  private SQLQuery previousQuery = null;
  private SQLQuery nextQuery = null;
  
  // caching of table names
  private Map beanClassNameTableNameMap = new HashMap();
  
  /** The specified query helper is the very last query in a sequence, that is the specified query can have one or more
   * queries as source. 
   * Sequence: first_query -> second_query -> ..... -> specified query helper
   * To initialize the corresponding sql queries we have to go back to the very first query helper in the sequence but we
   * are returning the corresponding sql query of the specified query helper that is the very last sql query is returned.
   * first sql query -> second sql query -> ..... -> returned sql query
   * Do not change the behaviour because you have to work with last sql query.
   */ 
  static public  SQLQuery getInstance(QueryHelper queryHelper, String uniqueIdentifier)	{
  	// go back to the very first query helper
  	QueryHelper currentQueryHelper = queryHelper;
  	while (currentQueryHelper.hasPreviousQuery())	{
  		currentQueryHelper = currentQueryHelper.previousQuery();
  	}
  	SQLQuery currentQuery = new SQLQuery(currentQueryHelper, uniqueIdentifier, 0, new TreeMap(), new HashMap(), null);
  	// go forward to the very first query
  	while(currentQuery.hasNextQuery())	{
  		currentQuery = currentQuery.nextQuery();
  	}
  	return currentQuery;
  }
		
  	
  
	private SQLQuery(QueryHelper queryHelper, String uniqueIdentifier, int counter, Map queryTablesNames, Map entityQueryEntityMap, SQLQuery previousQuery)	{
  	initialize(queryHelper, uniqueIdentifier, counter, queryTablesNames,  entityQueryEntityMap, previousQuery);
  }
  
  protected void initialize(QueryHelper queryHelper, String uniqueIdentifier, int counter, Map queryTablesNames, Map entityQueryEntityMap, SQLQuery previousQuery) {
  	this.previousQuery = previousQuery;
  	this.counter = counter;
  	this.uniqueIdentifier = uniqueIdentifier;
  	name = queryHelper.getName();
  	// add all already existing table names
  	beanClassNameTableNameMap.putAll(queryTablesNames);
  	entityQueryEntity.putAll(entityQueryEntityMap);
  	// create table name for this instance (unique identifier is user id) 
  	String myTableName = new StringBuffer("Q_").append(uniqueIdentifier).
  	append("_").append(++counter).toString();
  	// add the table name for this instance to the map
  	beanClassNameTableNameMap.put(name, myTableName);
  	QueryEntityPart queryEntity = new QueryEntityPart(name, name, name);
  	entityQueryEntityMap.put(name, queryEntity);
  	try {
      query = createQuery(queryHelper);
    }
    catch (IOException ex)  {
      query = null;
      System.err.println(ex.getMessage());
    }
    // go to the next query
    if (queryHelper.hasNextQuery())	{
    	QueryHelper nextQueryHelper = queryHelper.nextQuery();
    	SQLQuery sqlQuery = new SQLQuery(nextQueryHelper, uniqueIdentifier, this.counter , beanClassNameTableNameMap, entityQueryEntityMap ,this);
    	// get the generated dynamic expression
    	nextQuery = sqlQuery;
    }	
  }
  
  public boolean hasPreviousQuery()	{
  	return previousQuery() != null;
  }
  
  public boolean hasNextQuery()	{
  	return nextQuery() != null;
  }
  
  public SQLQuery nextQuery()	{
  	return nextQuery;
  }
  
  public SQLQuery previousQuery()	{
  	return previousQuery;
  }
  
  public boolean isDynamic()	{
  	//TODO: thi: temporary solution
  	boolean isDynamic = query.isDynamic();
  	// do not further ahead if you already know that the query is dynamic 
  	if ( (! isDynamic) && hasPreviousQuery())	{
  		return previousQuery().isDynamic();
  	}
  	return isDynamic;
  }
  
  public Map getIdentifierValueMap()	{
  	Map myMap = query.getIdentifierValueMap();
  	if (hasPreviousQuery()) 	{
  		myMap.putAll(previousQuery().getIdentifierValueMap());
  	}
  	return myMap;
  }
  
  public Map getIdentifierInputDescriptionMap()	{
  	Map myMap = query.getIdentifierInputDescriptionMap();
  	if (hasPreviousQuery()) {
  		myMap.putAll(previousQuery().getIdentifierInputDescriptionMap());
  	}
  	return myMap;
  }
  
  public void setIdentifierValueMap(Map identifierValueMap) {
  	query.setIdentifierValueMap(identifierValueMap);
  	if (hasPreviousQuery())	{
  		previousQuery().setIdentifierValueMap(identifierValueMap);
  	}
  }
  
  /**
   * Returns the corresponding sql statement
   */
  public String toSQLString() {
  	return query.toSQLString();
  }
  	
  public List getFields() {
  	return fieldOrder;
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

	public String getName()	{
		return name;
	}

  private void setSourceEntity(QueryHelper queryHelper) {
    QueryEntityPart queryEntity = queryHelper.getSourceEntity();
    if (queryEntity == null)  {
      return;
    }
    String path = queryEntity.getPath();
    entityQueryEntity.put(path, queryEntity);
  }
    
  private void setRelatedEntities(QueryHelper queryHelper)  {
    List entities = queryHelper.getListOfRelatedEntities();
    if (entities == null) {
      return;
    }
    Iterator iterator = entities.iterator();
    while (iterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) iterator.next();
      String path = queryEntity.getPath();
      entityQueryEntity.put(path, queryEntity);
      
    }
  }
  
  private void setFields(QueryHelper queryHelper)  {
    List fields = queryHelper.getListOfFields();
    if (fields == null) {
      return;
    }
    Iterator fieldIterator = fields.iterator();
    while (fieldIterator.hasNext())  {
      QueryFieldPart field = (QueryFieldPart) fieldIterator.next();
      setField(field);
      // mark that this entity is used
      if (! field.isHidden()) {
      	fieldOrder.add(field);
      }
    }
  }
  
  private void setConditions(QueryHelper queryHelper) {
    List list = queryHelper.getListOfConditions();
    if (list == null) {
      return;
    }
    conditions.addAll(list);
  }
  
  private void setOrderConditions(QueryHelper queryHelper)	{
  	// do not use order statements in views, they are not allowed in views
  	if (queryHelper.hasNextQuery())	{
  		return;
  	}
  	List list = queryHelper.getOrderConditions();
  	if (list == null)	{
  		return;
  	}
  	orderConditions.addAll(list);
  }
    
      
  private DynamicExpression createQuery(QueryHelper queryHelper) throws IOException {
  	
  	// direct sql ?
  	QuerySQLPart querySQLPart = queryHelper.getSQL();
  	if (querySQLPart != null)	{
  		String identifier = Integer.toString(++counter);
  		setFields(queryHelper);
  		// set post statement
  		DirectSQLStatement statement =  new DirectSQLStatement(querySQLPart, identifier, uniqueIdentifier, this);
  		postStatement = statement.getPostStatement();
  		// byebye
  		return statement;
  	}
  	// no direct sql !
    
    SelectStatement statement = (queryHelper.isSelectDistinct()) ? 
				SelectStatement.getInstanceWithDistinctFunction() : SelectStatement.getInstance();
    // prepare everything
    setSourceEntity(queryHelper);
    setRelatedEntities(queryHelper);
    setFields(queryHelper);
    setConditions(queryHelper);
    setOrderConditions(queryHelper);

    // set fields (select clause)
    Iterator fieldIterator = fieldOrder.iterator(); 
    while (fieldIterator.hasNext()) {
      QueryFieldPart queryField = (QueryFieldPart) fieldIterator.next();
      String path = queryField.getPath();
      // test if entity is supported
      if (! entityQueryEntity.containsKey(path))  {
        throw new IOException("[SQLQuery] criteria could not be created, table is unknown");
      }
   		// create expression
   		FunctionExpression functionExpression = FunctionExpression.getInstance(queryField, this);
   		if (functionExpression.isValid()) {
   			// mark used entity
   			entitiesUsedByField.add(path);
    		statement.addSelectClause(functionExpression);
   		}
    }
    // set conditions (where clause)
    QueryBooleanExpressionPart booleanExpressionPart = queryHelper.getBooleanExpressionForConditions();
    boolean booleanExpressionIsUsed = (booleanExpressionPart != null);
    CriteriaExpression criteriaExpression = (booleanExpressionIsUsed) ? new CriteriaExpression(booleanExpressionPart) : null;
    Iterator conditionsIterator = conditions.iterator();
    while (conditionsIterator.hasNext())  {
      QueryConditionPart condition = (QueryConditionPart) conditionsIterator.next();
      // use the counter as identifier
      String identifier = Integer.toString(++counter);
      CriterionExpression criterion = new CriterionExpression(condition, identifier, this);
      if (criterion.isValid()) {
      	// mark used entities
      	//String fieldName = condition.getField();
      	String path = condition.getPath();
      	entitiesUsedByCriterion.add(path);
      	if (! booleanExpressionIsUsed) {
      		statement.addWhereClause(criterion);
      	}
      	else {
      		criteriaExpression.add(criterion);
      	}      		
      }
    }
    if (booleanExpressionIsUsed)	{
    	statement.addWhereClause(criteriaExpression);
    }
    // set order conditions (order by)
    Iterator orderConditionsIterator = orderConditions.iterator();
    while(orderConditionsIterator.hasNext())	{
    	QueryOrderConditionPart orderConditionPart = (QueryOrderConditionPart) orderConditionsIterator.next();
    	OrderConditionExpression orderCriterion = new OrderConditionExpression(orderConditionPart, this);
    	if (orderCriterion.isValid()) {
    		String path = orderCriterion.getPath();
    		entitiesUsedByCriterion.add(path);
    		statement.addOrderByClause(orderCriterion); 
    	}
    }
    // set tables (from clause)
    Iterator entityIterator = entityQueryEntity.values().iterator();
    List innerJoins = new ArrayList();
    List outerJoins = new ArrayList();
    while (entityIterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) entityIterator.next();
     	String path = queryEntity.getPath();
      // add only entities that are actually used
      if (entitiesUsedByCriterion.contains(path))	{
      	// if an entity is used by a criterion use strong conditions, that is do not use left outer join
      	PathCriterionExpression pathCriterionExpression = new PathCriterionExpression(queryEntity, this);
       	if (pathCriterionExpression.isValid())	{
      		List joins = pathCriterionExpression.getInnerJoins();
      		innerJoins.addAll(joins);
      		if (pathCriterionExpression.hasCriteria())	{
      			// if the path contains only one entity there are't any criterias 
	       		statement.addWhereClause(pathCriterionExpression);
      		}
        }
      }
      else if (entitiesUsedByField.contains(path))	{
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
				statement.addInnerJoin(element);
			}
    }		
    Iterator outer = outerJoins.iterator();
    while (outer.hasNext()) {
			LeftOuterJoinExpression element = (LeftOuterJoinExpression) outer.next();
			String table = element.getTable();
			if (! addedTables.contains(table)) {
				addedTables.add(table);
				statement.addOuterJoin(element);
			}
    }		
    return statement;
  }
      

	public void addTableNamesForQueries(Map queryNameTableName)	{
		beanClassNameTableNameMap.putAll(queryNameTableName);
	}
	
	public String getMyTableName()	{
		return (String) beanClassNameTableNameMap.get(name);
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
		String entityPath = queryFieldPart.getPath();
		String uniqueName = getUniqueNameForEntity(entityName, entityPath);
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

  protected List getUniqueNameForField(String path, String fieldName)  {
    QueryFieldPart field = getField(path, fieldName);
    return getUniqueNameForField(field);
  }
  
  protected String getInputHandlerForField(String path, String fieldName) {
  	QueryFieldPart field = getField(path, fieldName);
  	return field.getHandlerClass();
  }
  
  protected String getHandlerDescriptionForField(String path, String fieldName) {
  	QueryFieldPart field = getField(path, fieldName);
  	return field.getHandlerDescription();
  }
  
  protected String getUniqueNameForEntityByTableName(String tableName, String entityPath)	{
		//TODO: thi add something else perhaps the name of that query to handle subqueries
		String alias = (String) aliasMatrix.get(tableName, entityPath);
		if (alias == null)	{
			StringBuffer buffer = new StringBuffer(ALIAS_PREFIX);
			buffer.append(++counter);
			alias = buffer.toString();
			aliasMatrix.put(tableName, entityPath, alias);
		}
		return alias;
  }


	protected String getUniqueNameForEntity(String entity, String entityPath)	{
		String tableName = getTableName(entity);
		return getUniqueNameForEntityByTableName(tableName, entityPath);
	}		
	
	protected String getEntityForField(String path, String fieldName)	{
		QueryFieldPart field = getField(path, fieldName);
		return field.getEntity();
	}
	
	private QueryFieldPart getField(String entityPath, String fieldName)	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(entityPath).append("#").append(fieldName);
		QueryFieldPart field = (QueryFieldPart) fieldNameQueryField.get(buffer.toString());
		return field;
	}	
	
	private void setField(QueryFieldPart fieldPart)	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(fieldPart.getPath()).append("#").append(fieldPart.getName());
		fieldNameQueryField.put(buffer.toString(), fieldPart);
	}
		 
	
	protected String getTypeClassForField(String path, String fieldName)	{
		QueryFieldPart field = getField(path, fieldName);
		return field.getTypeClass();
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.data.sql.Expression#isValid()
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/** Returns true if creating a view is possible and desired. 
	 * @return true if creating a view is possible and desired else false
	 */
	public boolean isUsableForCreatingAView()	{
		// If there aren't any fields creating a view makes no sense and is not desired
		return ! fieldOrder.isEmpty();
	}
	
	public String getPostStatement()	{
		return postStatement;
	}
		
		
}
