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
import com.idega.presentation.IWContext;
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
  
  private String path;
  private String name;
  
  private String uniqueIdentifier;
  
  private String postStatement = null;
  
  private String queryDescription = null;
  
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
  private HashMatrix fieldNameQueryField = new HashMatrix();
  
  private List fieldOrder = new ArrayList();
  
  private List selectHiddenFields = new ArrayList();
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
  private Map entityPathTableNameMap = new HashMap();
  
  /** The specified query helper is the very last query in a sequence, that is the specified query can have one or more
   * queries as source. 
   * Sequence: first_query -> second_query -> ..... -> specified query helper
   * To initialize the corresponding sql queries we have to go back to the very first query helper in the sequence but we
   * are returning the corresponding sql query of the specified query helper that is the very last sql query is returned.
   * first sql query -> second sql query -> ..... -> returned sql query
   * Do not change the behaviour because you have to work with last sql query.
   */ 
  static public  SQLQuery getInstance(QueryHelper queryHelper, String uniqueIdentifier, IWContext iwc)	{
  	// go back to the very first query helper
//  	QueryHelper currentQueryHelper = queryHelper;
//  	while (currentQueryHelper.hasPreviousQuery())	{
//  		currentQueryHelper = currentQueryHelper.previousQuery();
//  	}
  	List queries = new ArrayList();
  	collectAllChildren(queries, queryHelper);
  	SQLQuery currentQuery = null;
  	int tempCounter = 0;
  	Map queryTableNames = new HashMap();
  	Map entityQueryEntityMap = new HashMap();
  	for (int i  = queries.size()-1 ; i >= 0; i--) {
  		QueryHelper currentQueryHelper = (QueryHelper) queries.get(i);
  		//  does the query already exist?
  		String path = currentQueryHelper.getPath();
  		if (! queryTableNames.containsKey(path)) {
  			currentQuery = new SQLQuery(currentQueryHelper, uniqueIdentifier, tempCounter, queryTableNames, entityQueryEntityMap, currentQuery, iwc);
  			// java for beginners: primitves aren't objects, therefore set counter to the right number
  			tempCounter = currentQuery.counter;
  		}
  	}
//  	// go forward to the very first query
//  	while(currentQuery.hasNextQuery())	{
//  		currentQuery = currentQuery.nextQuery();
//  	}
  	return currentQuery;
  }
		
  static private void collectAllChildren(List result, QueryHelper queryHelper) {
  	result.add(queryHelper);
  	List children = queryHelper.previousQueries();
  	Iterator iterator = children.iterator();
  	while (iterator.hasNext()) {
  		QueryHelper child = (QueryHelper) iterator.next();
  		collectAllChildren(result, child);
  	}
  }
  		
  
	private SQLQuery(QueryHelper queryHelper, String uniqueIdentifier, int counter, Map queryTablesNames, Map entityQueryEntityMap, SQLQuery previousQuery, IWContext iwc)	{
  	initialize(queryHelper, uniqueIdentifier, counter, queryTablesNames,  entityQueryEntityMap, previousQuery, iwc);
  }
  
  protected void initialize(QueryHelper queryHelper, String uniqueIdentifier, int counter, Map queryTablesNames, Map entityQueryEntityMap, SQLQuery previousQuery, IWContext iwc) {
  	if (previousQuery != null) {
  		previousQuery.nextQuery = this;
  	}
  	this.previousQuery = previousQuery;
  	this.counter = counter;
  	this.uniqueIdentifier = uniqueIdentifier;
  	name = queryHelper.getName();
  	path = queryHelper.getPath();
  	queryDescription = queryHelper.getDescription();
  	
  	// table names.... 
  	 // create table name for this instance (unique identifier is user id) 
  	String myTableName = new StringBuffer("Q_").append(uniqueIdentifier).
  	append("_").append(++counter).toString();
  	 // add the table name for this instance to the map
  	 queryTablesNames.put(path, myTableName);
  	// add the new one AND all already existing table names
  	entityPathTableNameMap.putAll(queryTablesNames);
  	
  	// query entities...
  	QueryEntityPart queryEntity = new QueryEntityPart(path, path, path);
  	// add the queryEntity to the map
  	entityQueryEntityMap.put(path, queryEntity);
  	// add th enew one AND all already existing query entities
  	entityQueryEntity.putAll(entityQueryEntityMap);

  	try {
      query = createQuery(queryHelper, iwc);
    }
    catch (IOException ex)  {
      query = null;
      System.err.println(ex.getMessage());
    }
//    // go to the next query
//    if (queryHelper.hasNextQuery())	{
//    	QueryHelper nextQueryHelper = queryHelper.nextQuery();
//    	SQLQuery sqlQuery = new SQLQuery(nextQueryHelper, uniqueIdentifier, this.counter , entityPathTableNameMap, entityQueryEntityMap ,this);
//    	// get the generated dynamic expression
//    	nextQuery = sqlQuery;
//    }	
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
      String displayName = queryField.getDisplay(); 
      displayNames.add(displayName);
    }
    return displayNames;
  }
  
  public List getAliasFieldNames() {
    List fieldNames = new ArrayList();
    Iterator fieldOrderIterator = fieldOrder.iterator();
    while (fieldOrderIterator.hasNext())  {
      QueryFieldPart queryField = (QueryFieldPart) fieldOrderIterator.next();
      String fieldName = queryField.getAliasName(); 
      fieldNames.add(fieldName);
    }
    return fieldNames;
  }

	public String getPath()	{
		return path;
	}

  private void setSourceEntity(QueryHelper queryHelper) {
    QueryEntityPart queryEntity = queryHelper.getSourceEntity();
    if (queryEntity == null)  {
      return;
    }
    String entityPath = queryEntity.getPath();
    entityQueryEntity.put(entityPath, queryEntity);
  }
    
  private void setRelatedEntities(QueryHelper queryHelper)  {
    List entities = queryHelper.getListOfRelatedEntities();
    if (entities == null) {
      return;
    }
    Iterator iterator = entities.iterator();
    while (iterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) iterator.next();
      String entityPath = queryEntity.getPath();
      entityQueryEntity.put(entityPath, queryEntity);
      
    }
  }
  
  private void addSelectHiddenField(QueryFieldPart field) {
  	String entityPath = field.getPath();
  	String fieldName = field.getName();
  	Iterator iterator = fieldOrder.iterator();
  	while (iterator.hasNext()) {
  		QueryFieldPart fieldPart = (QueryFieldPart) iterator.next();
  		String partPath = fieldPart.getPath();
  		String partName = fieldPart.getName();
  		if (entityPath.equals(partPath) && fieldName.equals(partName)) {
  			// nothing to do
  			return;
  		}
  	}
  	// select...
  	selectHiddenFields.add(field);
  	// but do not display it
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
    
      
  private DynamicExpression createQuery(QueryHelper queryHelper, IWContext iwc) throws IOException {
  	
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
    
    // set order conditions (order by) (must be executed before set fields)
    Iterator orderConditionsIterator = orderConditions.iterator();
    while(orderConditionsIterator.hasNext())	{
    	QueryOrderConditionPart orderConditionPart = (QueryOrderConditionPart) orderConditionsIterator.next();
    	OrderConditionExpression orderCriterion = new OrderConditionExpression(orderConditionPart, this);
    	if (orderCriterion.isValid()) {
	    	// microsoft sql server hack START -------------------------------------------------------------------------------------------------------------------------
	    	String fieldName = orderConditionPart.getField();
	    	String fieldPath = orderConditionPart.getPath();
	    	QueryFieldPart queryFieldPart = getField(fieldPath, fieldName);
	    	addSelectHiddenField(queryFieldPart);
	    	// microsoft sql server hack END -----------------------------------------------------------------------------------------------------------------------------
    		String entityPath = orderCriterion.getPath();
    		entitiesUsedByCriterion.add(entityPath);
    		statement.addOrderByClause(orderCriterion); 
    	}
    }


    // set fields (select clause)
    List selectFields = (new ArrayList(fieldOrder));
    selectFields.addAll(selectHiddenFields);
    Iterator fieldIterator = selectFields.iterator(); 
    while (fieldIterator.hasNext()) {
      QueryFieldPart queryField = (QueryFieldPart) fieldIterator.next();
      String entityPath = queryField.getPath();
      // test if entity is supported
      if (! entityQueryEntity.containsKey(entityPath))  {
        throw new IOException("[SQLQuery] criteria could not be created, table is unknown");
      }
   		// create expression
   		FunctionExpression functionExpression = FunctionExpression.getInstance(queryField, this);
   		if (functionExpression.isValid()) {
   			// mark used entity
   			entitiesUsedByField.add(entityPath);
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
      CriterionExpression criterion = new CriterionExpression(condition, identifier, this, iwc);
      if (criterion.isValid()) {
      	// mark used entities
      	String entityPath = condition.getPath();
      	entitiesUsedByCriterion.add(entityPath);
      	String patternPath = condition.getPatternPath();
      	if (patternPath != null) {
      		entitiesUsedByCriterion.add(patternPath);
      	}
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
    // set tables (from clause)
    Iterator entityIterator = entityQueryEntity.values().iterator();
    List innerJoins = new ArrayList();
    List outerJoins = new ArrayList();
    while (entityIterator.hasNext())  {
      QueryEntityPart queryEntity = (QueryEntityPart) entityIterator.next();
     	String entityPath = queryEntity.getPath();
      // add only entities that are actually used
      if (entitiesUsedByCriterion.contains(entityPath))	{
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
      else if (entitiesUsedByField.contains(entityPath))	{
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
		entityPathTableNameMap.putAll(queryNameTableName);
	}
	
	public String getMyTableName()	{
		return (String) entityPathTableNameMap.get(path);
	}
	
  protected String getTableName(String entity, String entityPath) {
    // performance improvement
    String tableName = (String) entityPathTableNameMap.get(entityPath);
    if (tableName == null)  {
    	// Important note:
    	// if the table name was not found the entity should be a name of a bean class
    	// if the entity is not a name of a bean class it should be a name of a query. In that case
    	// the "table name" (actually the name of the corresponding view)  
    	// should have been already added to the entityPathTableNameMap.
      tableName = ((IDOEntity) GenericEntity.getStaticInstance(entity)).getEntityDefinition().getSQLTableName();
      // important: store the tablename using the path as key NOT the entity!
      entityPathTableNameMap.put(entityPath, tableName);
    }
    return tableName;
  }
  
  protected String getUniqueNameForField(QueryFieldPart queryFieldPart)	{
  	if (queryFieldPart == null) 	{
  		return null;
  	}
		String entityPath = queryFieldPart.getPath();
		String entity = queryFieldPart.getEntity();
		String uniqueName = getUniqueNameForEntity(entity, entityPath);
		String fieldName = queryFieldPart.getName();
		StringBuffer buffer = new StringBuffer(uniqueName);
		buffer.append(DOT).append(fieldName);
		return buffer.toString();
  }

  protected String getUniqueNameForField(String entityPath, String fieldName)  {
    QueryFieldPart field = getField(entityPath, fieldName);
    return getUniqueNameForField(field);
  }
  
  protected String getInputHandlerForField(String entityPath, String fieldName) {
  	QueryFieldPart field = getField(entityPath, fieldName);
  	return field.getHandlerClass();
  }
  
  protected String getHandlerDescriptionForField(String entityPath, String fieldName) {
  	QueryFieldPart field = getField(entityPath, fieldName);
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
		String tableName = getTableName(entity, entityPath);
		return getUniqueNameForEntityByTableName(tableName, entityPath);
	}		
	
	protected String getEntityForField(String entityPath, String fieldName)	{
		QueryFieldPart field = getField(entityPath, fieldName);
		return field.getEntity();
	}
	
	private QueryFieldPart getField(String entityPath, String fieldName)	{
		return (QueryFieldPart) fieldNameQueryField.get(entityPath, fieldName);
	}	
	
	private void setField(QueryFieldPart fieldPart)	{
		fieldNameQueryField.put(fieldPart.getPath(), fieldPart.getName(), fieldPart);
	}
		 
	
	protected String getTypeClassForField(String entityPath, String fieldName)	{
		QueryFieldPart field = getField(entityPath, fieldName);
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
		
		
	/**
	 * @return Returns the queryDescription.
	 */
	public String getQueryDescription() {
		return queryDescription;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

}
