/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.data.Query;
import com.idega.block.dataquery.data.QueryConstants;
import com.idega.block.dataquery.data.QuerySequence;
import com.idega.block.dataquery.data.UserQuery;
import com.idega.business.IBOLookup;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import com.idega.util.StringHandler;
import com.idega.util.datastructures.HashMatrix;
import com.idega.util.xml.XMLData;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: A helper class for Query objects</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryHelper {

	// important note: 
	// if this query helper hasn't a next query, path and name are equal!
	// comment-label: "prequerypath"
	// search for "prequerypath"
	private String name = null;
	private String path = null;
	
	private XMLDocument doc = null;
	private XMLElement root = null;
	private QuerySQLPart sqlPart = null;
	private QueryEntityPart sourceEntity = null;
	private List listOfRelatedEntities = null;
	private List listOfFields = null;
	private List listOfConditions = null;
	private List orderConditions = null;
	private QueryBooleanExpressionPart booleanExpression = null;
	private int step = 0;
	private boolean selectDistinct = true;
	private String description = null;
	private boolean isTemplate = false;
	private boolean entitiesLock = false;
	private boolean fieldsLock = false;
	private UserQuery userQuery = null;
	
	// the matrix below is only used during the query builder process as a temporary value holder
	// example: 
	// a field (identified by path and name) has a (predefined) handler.
	// if the user changes the handler of that field in the query builder the new value is first stored only in the matrix below,
	// but when the query is saved the handler is stored in the query.
	private HashMatrix entityFieldHandler = new HashMatrix();
	
	private List previousQueries = new ArrayList(1);
	private QueryHelper nextQuery;
	
	public QueryHelper()	{
		// default constructor
	}

	// used by report generator
	public QueryHelper(Query query, IWContext iwc) throws XMLException, Exception {
		this(query.getFileValue(), iwc);
	}

	// used by report generator
	public QueryHelper(InputStream stream, IWContext iwc) throws XMLException, Exception {
		this.doc = new XMLParser().parse(stream);
		this.userQuery = null;
		init(this.doc.getRootElement(), iwc);
	}

	public QueryHelper(XMLData data,  IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException {
		this(data, null, iwc);
	}
	
	public QueryHelper(XMLData data, UserQuery userQuery, IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException  {
		this.doc = data.getDocument();
		this.userQuery = userQuery;
		init(this.doc.getRootElement(), iwc);
	}
	
	public QueryHelper(XMLElement root, QueryHelper nextQuery, IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException	{
		this.nextQuery = nextQuery;
		init(root, iwc);
	}
	
	private void init(XMLElement rootElement, IWContext iwc) throws NumberFormatException, RemoteException, FinderException, IOException {
		this.root = rootElement; 
		if (rootElement != null) {
			this.name = rootElement.getAttribute(QueryXMLConstants.NAME).getValue();
			// the query itself doesn't know the path - set the path equal to name
			// see comment below
			// comment-label: "prequerypath"
			// search for "prequerypath"
			this.path = this.name;
			// check for an existing next query
			List copiedPreQueries = rootElement.getChildren(QueryXMLConstants.ROOT);
			Iterator copiedPreQueriesIterator = copiedPreQueries.iterator();
			while (copiedPreQueriesIterator.hasNext()) {
				XMLElement previousQueryElement = (XMLElement) copiedPreQueriesIterator.next();
				QueryHelper previousQuery = new QueryHelper(previousQueryElement, this, iwc);
				this.previousQueries.add(previousQuery);
			}
			XMLElement previousQueryElements = rootElement.getChild(QueryXMLConstants.SOURCE_QUERY);
			if (previousQueryElements != null)	{
				List preQueries =  previousQueryElements.getChildren(QueryXMLConstants.ENTITY);
				Iterator iterator = preQueries.iterator();
				while (iterator.hasNext()) {
					XMLElement queryXMLElement = (XMLElement) iterator.next();
					String preQueryName = queryXMLElement.getText(QueryXMLConstants.NAME);
					String preQueryPath = queryXMLElement.getText(QueryXMLConstants.PATH);
					QueryService queryService = getQueryService(iwc);
					QueryHelper previousQuery = queryService.getQueryHelperByNameAndPathToQuerySequence(preQueryName, preQueryPath, iwc);		
					previousQuery.setNextQuery(this);
					this.previousQueries.add(previousQuery);
					// set the path of the prequery
					// comment-label: "prequerypath"
					// search for "prequerypath"
					previousQuery.setPath(preQueryPath);
				}
			}
			XMLAttribute template = rootElement.getAttribute(QueryXMLConstants.TEMPLATE);
			this.isTemplate = (template != null && Boolean.getBoolean(template.getValue()));
			// check for direct sql
			XMLElement sqlElement = rootElement.getChild(QueryXMLConstants.SQL);
			if (sqlElement != null)	{
				this.sqlPart = new QuerySQLPart(sqlElement);
				List fields = this.sqlPart.getFields( this.name);
				addFields(fields);
			}
			// description
			this.description = rootElement.getTextTrim(QueryXMLConstants.DESCRIPTION);
			// distinct
			String distinct = rootElement.getTextTrim(QueryXMLConstants.DISTINCT);
			this.selectDistinct = Boolean.valueOf(distinct).booleanValue();
			//source 
			XMLElement source = rootElement.getChild(QueryXMLConstants.SOURCE_ENTITY);
			if (source != null) {
				// SOURCE ENTITY PART (STEP 1)
				XMLElement entity = source.getChild(QueryXMLConstants.ENTITY);
				if (entity != null) {
					this.sourceEntity = new QueryEntityPart(entity);
				}
			}
			// RELATED PART ( STEP 2)
			//		if (sourceEntity != null) {
			XMLElement related = rootElement.getChild(QueryXMLConstants.RELATED_ENTITIES);
			if (related != null) {
				XMLAttribute entLock = related.getAttribute(QueryXMLConstants.LOCK);
				this.entitiesLock = (entLock != null && Boolean.getBoolean(entLock.getValue()));
			}
			if (related != null && related.hasChildren()) {
				this.listOfRelatedEntities = new ArrayList();
				Iterator entities = related.getChildren().iterator();
				while (entities.hasNext()) {
					XMLElement xmlEntity = (XMLElement) entities.next();
					this.listOfRelatedEntities.add(new QueryEntityPart(xmlEntity));
				}
			}
			// FIELD PART (STEP 3)
			XMLElement fields = rootElement.getChild(QueryXMLConstants.FIELDS);

			XMLAttribute fieldLock = null;
			if (related != null) {
				related.getAttribute(QueryXMLConstants.LOCK);
			}
			this.fieldsLock = (fieldLock != null && Boolean.getBoolean(fieldLock.getValue()));
			if (fields != null && fields.hasChildren()) {
				Iterator iter = fields.getChildren().iterator();
				while (iter.hasNext()) {
					XMLElement xmlField = (XMLElement) iter.next();
					addField(new QueryFieldPart(xmlField));
				}
				// boolean expression for conditions
				XMLElement booleanExpressionXML = rootElement.getChild(QueryXMLConstants.BOOLEAN_EXPRESSION);
				if (booleanExpressionXML != null)	{
					this.booleanExpression = new QueryBooleanExpressionPart(booleanExpressionXML);
				}
				
				// CONDITION PART (STEP 4)
				XMLElement conditions = rootElement.getChild(QueryXMLConstants.CONDITIONS);
				if (conditions != null && conditions.hasChildren()) {
					this.listOfConditions = new ArrayList();
					Iterator conds = conditions.getChildren().iterator();
					while (conds.hasNext()) {
						XMLElement xmlCondition = (XMLElement) conds.next();
						this.listOfConditions.add(new QueryConditionPart(xmlCondition));
					}
				}
				
				XMLElement xmlOrderConditions = rootElement.getChild(QueryXMLConstants.ORDER_CONDITIONS);
				if (xmlOrderConditions != null && xmlOrderConditions.hasChildren())	{
					this.orderConditions = new ArrayList();
					Iterator orderConds = xmlOrderConditions.getChildren().iterator();
					while (orderConds.hasNext())	{
						XMLElement xmlOrderCondition = (XMLElement) orderConds.next();
						this.orderConditions.add(new QueryOrderConditionPart(xmlOrderCondition));
					}
				}
			}

//
			checkStep();

		}
	}

	private XMLElement getSourceEntityElement() {
		return new XMLElement(QueryXMLConstants.SOURCE_ENTITY);
	}

//	private XMLElement getRelatedElement() {
//		return new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
//	}
//	private XMLElement getFieldsElement() {
//		return new XMLElement(QueryXMLConstants.FIELDS);
//	}

	public XMLDocument createDocument() {
		//if (doc == null)  {
			this.doc = new XMLDocument(getUpdatedRootElement());
		//}
		//else {
			//getUpdatedRootElement();
			//initializeRootElement();
		//}
		return this.doc;
	}
			
	protected XMLElement getUpdatedRootElement()	{
		this.root = new XMLElement(QueryXMLConstants.ROOT);
		initializeRootElement();
		return this.root;
	}
		
	private void initializeRootElement() {
		if (hasPreviousQuery())	{
			XMLElement preQuery = new XMLElement(QueryXMLConstants.SOURCE_QUERY);
			Iterator iterator = this.previousQueries.iterator();
			while (iterator.hasNext()) {
				QueryHelper previousQuery = (QueryHelper) iterator.next();
				XMLElement preQueryElement = getPreQueryElement(previousQuery);
				preQuery.addContent(preQueryElement);
			}
			this.root.addContent(preQuery);
		}
		if (isTemplate()) {
			this.root.setAttribute(QueryXMLConstants.TEMPLATE, String.valueOf(isTemplate()));
		}
		this.root.setAttribute(QueryXMLConstants.NAME, this.name);
		// check for direct sql
		if (this.sqlPart != null)	{
			this.root.addContent(this.sqlPart.getQueryElement());
		}
		// add description 
		if (this.description != null && this.description.length() > 0) {
			XMLElement xmlDescription = new XMLElement(QueryXMLConstants.DESCRIPTION);
			xmlDescription.addContent(this.description);
			this.root.addContent(xmlDescription);
		}
		// add distinct
		XMLElement distinct = new XMLElement(QueryXMLConstants.DISTINCT);
		distinct.addContent(Boolean.toString(this.selectDistinct));
		this.root.addContent(distinct);
		//	SOURCE ENTITY PART (STEP 1)
		if (this.sourceEntity != null) {
			XMLElement sourceElement = getSourceEntityElement();
			sourceElement.addContent(this.sourceEntity.getQueryElement());
			this.root.addContent(sourceElement);
		}
		//	RELATED PART ( STEP 2)
		if (this.listOfRelatedEntities != null && !this.listOfRelatedEntities.isEmpty()) {
			Iterator iter = this.listOfRelatedEntities.iterator();
			XMLElement related = new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
			if (this.entitiesLock) {
				related.setAttribute(QueryXMLConstants.LOCK, String.valueOf(this.entitiesLock));
			}
			while (iter.hasNext()) {
				related.addContent(((QueryPart) iter.next()).getQueryElement());
			}
			this.root.addContent(related);
		}

		//	FIELD PART (STEP 3)
		if (this.listOfFields != null && !this.listOfFields.isEmpty() && this.sqlPart == null) {
			XMLElement fields = new XMLElement(QueryXMLConstants.FIELDS);
			if (this.fieldsLock) {
				fields.setAttribute(QueryXMLConstants.LOCK, String.valueOf(this.entitiesLock));
			}
			Iterator iter = this.listOfFields.iterator();
			while (iter.hasNext()) {
				// set inputhandler 
				QueryFieldPart fieldPart = (QueryFieldPart) iter.next();
				String inputHandler = (String) this.entityFieldHandler.get(fieldPart.getPath(),fieldPart.getName());
				if (inputHandler != null) {
					fieldPart.setHandlerClass(inputHandler);
				}
				fields.addContent(fieldPart.getQueryElement());
			}
			this.root.addContent(fields);
			
			// boolean expression for conditions
			if (this.booleanExpression != null)	{
				this.root.addContent(this.booleanExpression.getQueryElement());
			}

			//	CONDITION PART (STEP 4)
			if (this.listOfConditions != null && !this.listOfConditions.isEmpty()) {
				iter = this.listOfConditions.iterator();
				XMLElement conditions = new XMLElement(QueryXMLConstants.CONDITIONS);
				while (iter.hasNext()) {
					conditions.addContent(((QueryPart) iter.next()).getQueryElement());
				}
				this.root.addContent(conditions);
			}
			
			// order conditions
			if (this.orderConditions != null && !this.orderConditions.isEmpty())	{
				iter = this.orderConditions.iterator();
				XMLElement orderConditionsElement = new XMLElement(QueryXMLConstants.ORDER_CONDITIONS);
				while (iter.hasNext())	{
					orderConditionsElement.addContent(((QueryPart) iter.next()).getQueryElement());
				}
				this.root.addContent(orderConditionsElement);
			}				
		}
	}

	/**
	 * @return <CODE>true</CODE> if the query has a source entity
	 */
	public boolean hasSourceEntity() {
		return this.sourceEntity != null;
	}
	/**
	 * @return <CODE>true</CODE> if the query has related entities
	 */
	public boolean hasRelatedEntities() {
		return this.listOfRelatedEntities != null && !this.listOfRelatedEntities.isEmpty();
	}
	/**
	 * @return <CODE>true</CODE> if the query has fields
	 */
	public boolean hasFields() {	
		return this.listOfFields != null && ! getListOfVisibleFields().isEmpty();
	}
	
	public boolean hasOrderConditions()	{
		return this.orderConditions != null && ! this.orderConditions.isEmpty();
	}
	
	/**
	 * 
	 * @return <CODE>true</CODE> if the query has conditions
	 */
	public boolean hasConditions() {
		return this.listOfConditions != null && !this.listOfConditions.isEmpty();
	}

	/**
	 * Gets the document element of the query xml document
	 * @return the document element
	 */
	public XMLDocument getDoc() {
		return this.doc;
	}

	/**
	 * Gets the list of conditions of the query
	 * @return the list of conditions
	 */
	public List getListOfConditions() {
		return this.listOfConditions;
	}
	
	public int getNextIdForCondition() {
		if (this.listOfConditions == null) {
			return 1;
		}
		int maxNumber = 0;
		int idNumber;
		Iterator iterator = this.listOfConditions.iterator();
		while (iterator.hasNext()) {
			QueryConditionPart part = (QueryConditionPart) iterator.next();
			if ((idNumber = part.getIdNumber()) > maxNumber) {
				maxNumber = idNumber;
			}
		}
		return ++maxNumber;
	}
	
	public QueryBooleanExpressionPart getBooleanExpressionForConditions()	{
		return this.booleanExpression;
	}

	/**
	 *Gets the list of fields of the query
	 * @return the list of fields or null
	 */
	public List getListOfFields() {
		return this.listOfFields;
	}
	
	// its very unlikely that there aren't any visible fields
	// therefore we return always a list, even if the list might be empty
	// the caller doesn't need to check if the return value is null
	public List getListOfVisibleFields()	{
		if (this.listOfFields == null) {
			return new ArrayList(0);
		}
		// in most cases the numbers of  fields and the number of visible fields are the same 
		List visibleFields = new ArrayList(this.listOfFields.size());
		Iterator fieldIterator = this.listOfFields.iterator();
		while (fieldIterator.hasNext()) {
			QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
			if (! fieldPart.isHidden())	{
				visibleFields.add(fieldPart);
			}
		}
		return visibleFields;
	}

	/**
	 * Gets the list of related entities of the query
	 * @return the list of related entities
	 */
	public List getListOfRelatedEntities() {
		return this.listOfRelatedEntities;
	}

	/**
	 * Gets the root element of the query xml
	 * @return the root element
	 */
	public XMLElement getRoot() {
		return this.root;
	}

	public QuerySQLPart getSQL()	{
		return this.sqlPart;
	}

	/**
	 * Gets the source entity of the query
	 * @return the source entity part
	 */
	public QueryEntityPart getSourceEntity() {
		return this.sourceEntity;
	}

	/**
	 * Sets the document of the xml
	 * @param document
	 */
	public void setDoc(XMLDocument document) {
		this.doc = document;
	}

	/**
	 * Sets the list of conditions of the query
	 * @param list
	 */
	public void setListOfConditions(List list) {
		this.listOfConditions = list;
		checkStep();
	}

	/**
	 * Sets the list of related entities of the query
	 * @param list
	 */
	public void setListOfRelatedEntities(List list) {
		this.listOfRelatedEntities = list;
		checkStep();
	}

	/**
	 * Sets the root element of the XML document
	 * @param element
	 */
	public void setRoot(XMLElement element) {
		this.root = element;
	}

	/**
	 * Sets the source entity part of the query
	 * @param the  entity
	 */
	public void setSourceEntity(QueryEntityPart part) {
		QueryEntityPart oldPart = this.sourceEntity;
		this.sourceEntity = part;
		if (oldPart != null && !part.encode().equals(oldPart.encode())) {
//			clearRelatedEntities();
//			clearFields();
//			clearConditions();
		}
		checkStep();
	}

	public void addQuery(QueryHelper queryHelper)	{
		UserQuery newUserQuery = queryHelper.getUserQuery();
		String newUserQueryPrimaryKey = newUserQuery.getPrimaryKey().toString();
		Iterator iterator = this.previousQueries.iterator();
		while (iterator.hasNext()) {
			QueryHelper tempQueryHelper = (QueryHelper) iterator.next();
			UserQuery tempUserQuery = tempQueryHelper.getUserQuery();
			String primaryKey = tempUserQuery.getPrimaryKey().toString();
			if (newUserQueryPrimaryKey.equals(primaryKey)) {
				// nothing to do
				return;
			}
		}
		// comment-label: "prequerypath"
		String prequeryPath = queryHelper.getPathToMyQuerySequence();
		queryHelper.setPath(prequeryPath);
		this.previousQueries.add(queryHelper);
//		clearFields();
//		clearOrderConditions();
//		clearConditions();
	}
	
		private QueryEntityPart getQueryEntityPart(Class entityClass) {
		GenericEntity entity = getEntity(entityClass);
		if (entity != null) {
			return new QueryEntityPart(entity.getEntityName(), entityClass.getName());
		}
		return null;
	}

	/**
	 * Adds a new related entity to the related entity part of the query
	 * @param new  entity class
	 */
	public void addRelatedEntity(Class entityClass) {
		QueryEntityPart entityPart = getQueryEntityPart(entityClass);
		if (entityPart != null) {
			addRelatedEntity(entityPart);
		}
	}

	/**
	 * Adds a new related entity to the related entity part of the query
	 * @param the new entity
	 */
	public void addRelatedEntity(QueryEntityPart entity) {
		if (this.listOfRelatedEntities == null) {
			this.listOfRelatedEntities = new ArrayList();
		}
		if(!hasRelatedEntity(entity)){
			this.listOfRelatedEntities.add(entity);
			checkStep();
		}
	}

	/**
	 * Adds a new field to the field part of the query
	 * @param the new field
	 */
	public void addField(QueryFieldPart field) {
		if (this.listOfFields == null) {
			this.listOfFields = new ArrayList();
		}
		if (addFieldWithoutChecks(field)) {
			checkStep();
		}
	}
	
	public void addFields(List listOfNewFields) {
		if (this.listOfFields == null) {
			this.listOfFields = new ArrayList(listOfNewFields.size());
		}
		boolean atLeastOneFieldWasAdded = false;
		Iterator iterator = listOfNewFields.iterator();
		while (iterator.hasNext()) {
			QueryFieldPart newFieldPart = (QueryFieldPart) iterator.next();
			atLeastOneFieldWasAdded= addFieldWithoutChecks(newFieldPart);
		}
		if (atLeastOneFieldWasAdded) {
			checkStep();
		}
	}
		
	private boolean addFieldWithoutChecks(QueryFieldPart field) {
		if(!hasFieldPart(field)){
			createAliasNameIfNecessary(field);
			this.listOfFields.add(field);
			return true;
		}
		return false;
	}
	
	private void createAliasNameIfNecessary(QueryFieldPart field) {
		if (!hasFields()) {
			return;
		}
		Collection names = new ArrayList(this.listOfFields.size());
		Iterator iter = this.listOfFields.iterator(); 
		while (iter.hasNext()) {
			QueryFieldPart element = (QueryFieldPart) iter.next();
			names.add(element.getName());
		}
		String newName = field.getName();
		String checkedName = StringHandler.addOrIncreaseCounterIfNecessary(newName, QueryConstants.COUNTER_TOKEN, names);
		if (! newName.equals(checkedName)) {
			field.setAliasName(checkedName);
		}
	}

	
	
	public void addHiddenField(QueryFieldPart field)	{
		field.setHidden(true);
		addField(field);
	}
	
	public void addOrderCondition(QueryOrderConditionPart orderCondition)	{
		List orderConditionsTemp = getOrderConditions();
		int orderNumber = orderConditionsTemp.size();
		orderCondition.setOrderPriority(++orderNumber);
		orderConditionsTemp.add(orderCondition);
	}
		
		
	public List getOrderConditions() {
		if (this.orderConditions == null)	{
			this.orderConditions = new ArrayList(1);
		}
		return this.orderConditions;
	}
	
	public void setBooleanExpressionForConditions(QueryBooleanExpressionPart booleanExpression)	{
		this.booleanExpression = booleanExpression;
	}
	
	
	/**
	 * Adds a new condition to the condition part of the query
	 * @param the new condition
	 */
	public void addCondition(QueryConditionPart condition) {
		if (this.listOfConditions == null) {
			this.listOfConditions = new ArrayList();
		}
		if(!hasCondition(condition)){
			this.listOfConditions.add(condition);
			checkStep();
		}
	}

	/**
	 * Clears all part of the query
	 */
	public void clearAll() {
		clearSourceEntity();
		clearRelatedEntities();
		clearFields();
		clearConditions();
	}

	/**
	 *  Clears the source entity part of the query
	 * 	and updates the current step.
	 */
	public void clearSourceEntity() {
		this.sourceEntity = null;
		checkStep();
	}

	/**
	 *  Clears the related entity part of the query
	 * and updates the current step.
	 */
	public void clearRelatedEntities() {
		this.listOfRelatedEntities = null;
		checkStep();
	}

	/**
	 *  Clears the field part of the query and updates the current step
	 */
	public void clearFields() {
		if (this.listOfFields == null) {
			return;
		}
		// prepare a matrix that stores all used fields
		HashMatrix usedFields = new HashMatrix();
		if (this.listOfConditions != null) {
			Iterator listOfConditionsIterator = this.listOfConditions.iterator();
			while (listOfConditionsIterator.hasNext()) {
				QueryConditionPart condition = (QueryConditionPart) listOfConditionsIterator.next();
				String condPath = condition.getPath();
				String condField = condition.getField();
				String patternPath = condition.getPatternPath();
				String patternField = condition.getPatternField();
				if (condPath != null) {
					usedFields.put(condPath, condField, null);
				}
				if (patternPath != null) {
					usedFields.put(patternPath, patternField, null);
				}
			}
		}
		if (this.orderConditions != null) {
			Iterator orderConditionsIterator = this.orderConditions.iterator();
			while (orderConditionsIterator.hasNext()) {
				QueryOrderConditionPart orderCondition = (QueryOrderConditionPart) orderConditionsIterator.next();
				String orderPath = orderCondition.getPath();
				String orderField = orderCondition.getField();
				if (orderPath != null) {
					usedFields.put(orderPath, orderField, null);
				}
			}
		}
		List invisibleFields = new ArrayList();
		Iterator fieldIterator = this.listOfFields.iterator();
		while (fieldIterator.hasNext()) {
			QueryFieldPart fieldPart = (QueryFieldPart) fieldIterator.next();
			String fieldPath = fieldPart.getPath();
			String fieldName = fieldPart.getName();
			if (usedFields.containsKey(fieldPath, fieldName))	{
				// in most cases it is already hidden but sometimes the field was a visible field 
				fieldPart.setHidden(true);
				invisibleFields.add(fieldPart);
			}
		}
		this.listOfFields = invisibleFields;
		checkStep();
	}

	public void clearOrderConditions()	{
		this.orderConditions = null;
		checkStep();
	}
	
	/**
	 * Clears the condition part of the query
	 * and updates the current step.
	 */
	public void clearConditions() {
		this.listOfConditions = null;
		checkStep();
	}

	/**
	 * Gets the current step in the query building process
	 * @return the current step 
	 */
	public int getStep() {
		return this.step;
	}

	private void checkStep() {
		if (hasConditions()) {
			this.step = 5;
		}
		else if (hasOrderConditions()) {
			this.step = 4;
		}
		else if (hasFields()) {
			this.step = 3;
		}
		else if (hasRelatedEntities()) {
			this.step = 2;
		}
		else if (hasSourceEntity() || hasPreviousQuery()) {
			this.step = 1;
		}
		else {
			this.step = 0;
		//System.out.println("checkstep : step is now "+step);
		}
	}

	private GenericEntity getEntity(Class entityClass) {
		return (GenericEntity) GenericEntity.getStaticInstance(entityClass);
	}

	/**
	 * @return <CODE>true</CODE> if the query is a template
	 */
	public boolean isTemplate() {
		return this.isTemplate;
	}

	/**
	 * Sets if the query should be a template for other queries.
	 *
	 */
	public void setTemplate(boolean b) {
		this.isTemplate = b;
	}

	/**
	 * @return
	 */
	public boolean isEntitiesLock() {
		return this.entitiesLock;
	}

	/**
	 * @return
	 */
	public boolean isFieldsLock() {
		return this.fieldsLock;
	}

	/**
	 * @param b
	 */
	public void setEntitiesLock(boolean b) {
		this.entitiesLock = b;
	}

	/**
	 * @param b
	 */
	public void setFieldsLock(boolean b) {
		this.fieldsLock = b;
	}

	/**
	 * Searches the entity with the given name
	 * @param entityName
	 * @return query entity part if found, else null
	 */
	public QueryEntityPart getEntityPart(String entityName) {
		if (hasSourceEntity() && getSourceEntity().getName().equals(entityName)) {
			return getSourceEntity();
		}
		else if (hasRelatedEntities()) {
			Iterator iter = getListOfRelatedEntities().iterator();
			while (iter.hasNext()) {
				QueryEntityPart part = (QueryEntityPart) iter.next();
				if (part.getName().equals(entityName)) {
					return part;
				}
			}
		}
		return null;
	}

	public boolean hasFieldPart(QueryFieldPart field) {
		if (!hasFields()) {
			return false;
		}
		Iterator iter = this.listOfFields.iterator();
		while (iter.hasNext()) {
			QueryFieldPart element = (QueryFieldPart) iter.next();
			if (element.encode().equals(field.encode())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasCondition(QueryConditionPart condition) {
		if (!hasConditions()) {
			return false;
		}
		for (Iterator iter = this.listOfConditions.iterator(); iter.hasNext();) {
			QueryConditionPart element = (QueryConditionPart) iter.next();
			if (element.encode().equals(condition.encode())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasRelatedEntity(QueryEntityPart entity) {
		if (!hasRelatedEntities()) {
			return false;
		}
		for (Iterator iter = this.listOfRelatedEntities.iterator(); iter.hasNext();) {
			QueryEntityPart element = (QueryEntityPart) iter.next();
			if (element.encode().equals(entity.encode())) {
				return true;
			}
		}
		return false;
	}
	
	public String getName()	{
		return this.name;
	}
	
	public void setName(String name)	{
		this.name = name;
	}
	
	public boolean hasNextQuery() {
		return nextQuery() != null;
	}
	
	public boolean hasPreviousQuery()	{
		return ! this.previousQueries.isEmpty();
	}
	
	public List previousQueries()	{
		return this.previousQueries;
	}

	public QueryHelper nextQuery()	{
		return this.nextQuery;
	}
	
	public void setInputHandler(String entityPath, String field, String handlerClass) {
		this.entityFieldHandler.put(entityPath, field, handlerClass);
	}
	
	public String getInputHandler(String entityPath, String field) {
		return (String) this.entityFieldHandler.get(entityPath, field);
	}
	
	public boolean isSelectDistinct() {
		return this.selectDistinct;
	}
	
	/**
	 * @param selectDistinct The selectDistinct to set.
	 */
	public void setSelectDistinct(boolean selectDistinct) {
		this.selectDistinct = selectDistinct;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the userQuery.
	 */
	public UserQuery getUserQuery() {
		return this.userQuery;
	}

	/**
	 * @param userQuery The userQuery to set.
	 */
	public void setUserQuery(UserQuery userQuery) {
		this.userQuery = userQuery;
	}

	public QueryService getQueryService(IWContext iwc) throws RemoteException {
		return (QueryService) IBOLookup.getServiceInstance( iwc, QueryService.class);
	}
	
	public XMLElement getPreQueryElement(QueryHelper preQuery) {
		XMLElement el = new XMLElement(QueryXMLConstants.ENTITY);
		XMLElement xmlName = new XMLElement(QueryXMLConstants.NAME);
		QuerySequence previousQuerySequence = preQuery.getUserQuery().getRoot();
		String preQueryName = previousQuerySequence.getName();
		String className = QuerySequence.class.getName();
		String preQueryPath = getPathToQuerySequence(previousQuerySequence);
		xmlName.addContent(preQueryName);
		XMLElement xmlClass = new XMLElement(QueryXMLConstants.BEANCLASS);
		xmlClass.addContent(className);
		XMLElement xmlPath = new XMLElement(QueryXMLConstants.PATH);
		xmlPath.addContent(preQueryPath);
		el.addContent(xmlName);
		el.addContent(xmlClass);
		el.addContent(xmlPath);
		return el;
	}
	/**
	 * @param nextQuery The nextQuery to set.
	 */
	public void setNextQuery(QueryHelper nextQuery) {
		this.nextQuery = nextQuery;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getPathToMyQuerySequence() {
		return getPathToQuerySequence(getUserQuery().getRoot());
	}
		
	private String getPathToQuerySequence(QuerySequence previousQuerySequence) {	
		String preQueryId = previousQuerySequence.getPrimaryKey().toString();
		String idColumnName = previousQuerySequence.getIDColumnName();
		String className = QuerySequence.class.getName();
		StringBuffer buffer = new StringBuffer(className);
		buffer.append('.');
		buffer.append(idColumnName);
		buffer.append('(');
		buffer.append(preQueryId);
		buffer.append(')');
		return buffer.toString();
	}

}
