/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.block.dataquery.data.Query;
import com.idega.block.dataquery.data.sql.QuerySQL;
import com.idega.data.GenericEntity;
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

	private String name = null;
	private XMLDocument doc = null;
	private XMLElement root = null;
	private QuerySQLPart sqlPart = null;
	private QueryEntityPart sourceEntity = null;
	private List listOfRelatedEntities = null;
	private List listOfFields = new ArrayList();
	private List listOfConditions = null;
	private int step = 0;
	private boolean isTemplate = false;
	private boolean entitiesLock = false, fieldsLock = false;
	
	private QueryHelper previousQuery;
	private QueryHelper nextQuery;
	
	private QuerySQL mySQLQuery;
	
	public QueryHelper()	{
	}

	public QueryHelper(Query query) throws XMLException, Exception {
		this(query.getFileValue());
	}

	public QueryHelper(InputStream stream) throws XMLException, Exception {
		this(new XMLParser().parse(stream));
	}

	public QueryHelper(XMLDocument document) {
		doc = document;
		init(doc.getRootElement());
	}
	
	public QueryHelper(XMLElement root)	{
		init(root);
	}
	
	public QueryHelper(XMLElement root, QueryHelper nextQuery)	{
		this.nextQuery = nextQuery;
		init(root);
	}
		

	private void init(XMLElement root) {
		this.root = root; 
		if (root != null) {
			name = root.getAttribute(QueryXMLConstants.NAME).getValue();
			// check for an existing next query
			XMLElement previousQueryElement = root.getChild(QueryXMLConstants.ROOT);
			if (previousQueryElement != null)	{
				previousQuery = new QueryHelper(previousQueryElement, this);
			}
			XMLAttribute template = root.getAttribute(QueryXMLConstants.TEMPLATE);
			isTemplate = (template != null && Boolean.getBoolean(template.getValue()));
			// check for direct sql
			XMLElement sqlElement = root.getChild(QueryXMLConstants.SQL);
			if (sqlElement != null)	{
				sqlPart = new QuerySQLPart(sqlElement);
				List fields = sqlPart.getFields( name);
				listOfFields.addAll(fields);
			}
			XMLElement source = root.getChild(QueryXMLConstants.SOURCE_ENTITY);
			if (source != null) {
				// SOURCE ENTITY PART (STEP 1)
				XMLElement entity = source.getChild(QueryXMLConstants.ENTITY);
				if (entity != null) {
					sourceEntity = new QueryEntityPart(entity);
				}
			}
					// RELATED PART ( STEP 2)
			//		if (sourceEntity != null) {
			XMLElement related = root.getChild(QueryXMLConstants.RELATED_ENTITIES);
			if (related != null) {
				XMLAttribute entLock = related.getAttribute(QueryXMLConstants.LOCK);
				entitiesLock = (entLock != null && Boolean.getBoolean(entLock.getValue()));
			}
			if (related != null && related.hasChildren()) {
				listOfRelatedEntities = new ArrayList();
				Iterator entities = related.getChildren().iterator();
				while (entities.hasNext()) {
					XMLElement xmlEntity = (XMLElement) entities.next();
					listOfRelatedEntities.add(new QueryEntityPart(xmlEntity));
				}
			}
			// FIELD PART (STEP 3)
			XMLElement fields = root.getChild(QueryXMLConstants.FIELDS);

			XMLAttribute fieldLock = null;
			if (related != null)
				related.getAttribute(QueryXMLConstants.LOCK);
			fieldsLock = (fieldLock != null && Boolean.getBoolean(fieldLock.getValue()));
			if (fields != null && fields.hasChildren()) {
				Iterator iter = fields.getChildren().iterator();
				while (iter.hasNext()) {
					XMLElement xmlField = (XMLElement) iter.next();
					listOfFields.add(new QueryFieldPart(xmlField));
				}

				// CONDITION PART (STEP 4)
				XMLElement conditions = root.getChild(QueryXMLConstants.CONDITIONS);
				if (conditions != null && conditions.hasChildren()) {
					listOfConditions = new ArrayList();
					Iterator conds = conditions.getChildren().iterator();
					while (conds.hasNext()) {
						XMLElement xmlCondition = (XMLElement) conds.next();
						listOfConditions.add(new QueryConditionPart(xmlCondition));
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

	private XMLElement getRelatedElement() {
		return new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
	}
	private XMLElement getFieldsElement() {
		return new XMLElement(QueryXMLConstants.FIELDS);
	}

	public XMLDocument createDocument() {
		if (doc == null)  {
			doc = new XMLDocument(getUpdatedRootElement());
		}
		else {
			updateRootElement();
		}
		return doc;
	}
			
	protected XMLElement getUpdatedRootElement()	{
		if (root == null) {
			root = new XMLElement(QueryXMLConstants.ROOT);
		}
		updateRootElement();
		return root;
	}
		
	private void updateRootElement() {
		if (hasPreviousQuery())	{
			XMLElement previousQueryRootElement = previousQuery().getUpdatedRootElement();
			previousQueryRootElement = previousQueryRootElement.detach();
			root.addContent(previousQueryRootElement);
		}
		if (isTemplate()) {
			root.setAttribute(QueryXMLConstants.TEMPLATE, String.valueOf(isTemplate()));
		}
		root.setAttribute(QueryXMLConstants.NAME, name);
		//	SOURCE ENTITY PART (STEP 1)
		if (sourceEntity != null) {
			XMLElement sourceElement = getSourceEntityElement();
			sourceElement.addContent(sourceEntity.getQueryElement());
			root.addContent(sourceElement);
		}
		//	RELATED PART ( STEP 2)
		if (listOfRelatedEntities != null && !listOfRelatedEntities.isEmpty()) {
			Iterator iter = listOfRelatedEntities.iterator();
			XMLElement related = new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
			if (entitiesLock)
				related.setAttribute(QueryXMLConstants.LOCK, String.valueOf(entitiesLock));
			while (iter.hasNext()) {
				related.addContent(((QueryPart) iter.next()).getQueryElement());
			}
			root.addContent(related);
		}

		//	FIELD PART (STEP 3)
		if (listOfFields != null && !listOfFields.isEmpty() && sqlPart == null) {
			Iterator iter = listOfFields.iterator();
			XMLElement fields = new XMLElement(QueryXMLConstants.FIELDS);
			if (fieldsLock)
				fields.setAttribute(QueryXMLConstants.LOCK, String.valueOf(entitiesLock));
			while (iter.hasNext()) {
				fields.addContent(((QueryPart) iter.next()).getQueryElement());
			}
			root.addContent(fields);

			//	CONDITION PART (STEP 4)
			if (listOfConditions != null && !listOfConditions.isEmpty()) {
				iter = listOfConditions.iterator();
				XMLElement conditions = new XMLElement(QueryXMLConstants.CONDITIONS);
				while (iter.hasNext()) {
					conditions.addContent(((QueryPart) iter.next()).getQueryElement());
				}
				root.addContent(conditions);
			}
		}
	}

	/**
	 * @return <CODE>true</CODE> if the query has a source entity
	 */
	public boolean hasSourceEntity() {
		return sourceEntity != null;
	}
	/**
	 * @return <CODE>true</CODE> if the query has related entities
	 */
	public boolean hasRelatedEntities() {
		return listOfRelatedEntities != null && !listOfRelatedEntities.isEmpty();
	}
	/**
	 * @return <CODE>true</CODE> if the query has fields
	 */
	public boolean hasFields() {
		return listOfFields != null && !listOfFields.isEmpty();
	}
	/**
	 * 
	 * @return <CODE>true</CODE> if the query has conditions
	 */
	public boolean hasConditions() {
		return listOfConditions != null && !listOfConditions.isEmpty();
	}

	/**
	 * Gets the document element of the query xml document
	 * @return the document element
	 */
	public XMLDocument getDoc() {
		return doc;
	}

	/**
	 * Gets the list of conditions of the query
	 * @return the list of conditions
	 */
	public List getListOfConditions() {
		return listOfConditions;
	}

	/**
	 *Gets the list of fields of the query
	 * @return the list of fields
	 */
	public List getListOfFields() {
		return listOfFields;
	}

	/**
	 * Gets the list of related entities of the query
	 * @return the list of related entities
	 */
	public List getListOfRelatedEntities() {
		return listOfRelatedEntities;
	}

	/**
	 * Gets the root element of the query xml
	 * @return the root element
	 */
	public XMLElement getRoot() {
		return root;
	}

	public QuerySQLPart getSQL()	{
		return sqlPart;
	}

	/**
	 * Gets the source entity of the query
	 * @return the source entity part
	 */
	public QueryEntityPart getSourceEntity() {
		return sourceEntity;
	}

	/**
	 * Sets the document of the xml
	 * @param document
	 */
	public void setDoc(XMLDocument document) {
		doc = document;
	}

	/**
	 * Sets the list of conditions of the query
	 * @param list
	 */
	public void setListOfConditions(List list) {
		listOfConditions = list;
		checkStep();
	}

	/**
	 * Sets the list of fields of the query
	 * @param list
	 */
	public void setListOfFields(List list) {
		listOfFields = list;
		checkStep();
	}

	/**
	 * Sets the list of related entities of the query
	 * @param list
	 */
	public void setListOfRelatedEntities(List list) {
		listOfRelatedEntities = list;
		checkStep();
	}

	/**
	 * Sets the root element of the XML document
	 * @param element
	 */
	public void setRoot(XMLElement element) {
		root = element;
	}

	/**
	 * Sets the source entity part of the query
	 * @param the  entity
	 */
	public void setSourceEntity(QueryEntityPart part) {
		QueryEntityPart oldPart = sourceEntity;
		sourceEntity = part;
		if (oldPart != null && !part.encode().equals(oldPart.encode())) {
			clearRelatedEntities();
			clearFields();
			clearConditions();
		}
		checkStep();
	}

	/**
	 * Sets the source entity part of the query
	 * @param the entity class
	 */
	public void setSourceEntity(Class entityClass) {
		QueryEntityPart entityPart = getQueryEntityPart(entityClass);
		if (entityPart != null) {
			setSourceEntity(entityPart);
		}
	}

	private QueryEntityPart getQueryEntityPart(Class entityClass) {
		GenericEntity entity = getEntity(entityClass);
		if (entity != null)
			return new QueryEntityPart(entity.getEntityName(), entityClass.getName());
		return null;
	}

	/**
	 * Adds a new related entity to the related entity part of the query
	 * @param new  entity class
	 */
	public void addRelatedEntity(Class entityClass) {
		QueryEntityPart entityPart = getQueryEntityPart(entityClass);
		if (entityPart != null)
			addRelatedEntity(entityPart);
	}

	/**
	 * Adds a new related entity to the related entity part of the query
	 * @param the new entity
	 */
	public void addRelatedEntity(QueryEntityPart entity) {
		if (listOfRelatedEntities == null) {
			listOfRelatedEntities = new ArrayList();
		}
		if(!hasRelatedEntity(entity)){
			listOfRelatedEntities.add(entity);
			checkStep();
		}
	}

	/**
	 * Adds a new field to the field part of the query
	 * @param the new field
	 */
	public void addField(QueryFieldPart field) {
		if (listOfFields == null) {
			listOfFields = new ArrayList();
		}
		if(!hasFieldPart(field)){
			listOfFields.add(field);
			checkStep();
		}
	}
	
	public void addHiddenField(QueryFieldPart field)	{
		field.setHidden(false);
		if (hasFieldPart(field)) {
			// a visible field is already there and not hidden, do not change that field
			field.setHidden(true);
			return;
		}
		// add the hidden field
		field.setHidden(true);
		addField(field);
	}
	
	public void addQuery(QueryHelper queryHelper)	{
		this.previousQuery = queryHelper;
	}

	/**
	 * Adds a new condition to the condition part of the query
	 * @param the new condition
	 */
	public void addCondition(QueryConditionPart condition) {
		if (listOfConditions == null) {
			listOfConditions = new ArrayList();
		}
		if(!hasCondition(condition)){
			listOfConditions.add(condition);
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
		sourceEntity = null;
		checkStep();
	}

	/**
	 *  Clears the related entity part of the query
	 * and updates the current step.
	 */
	public void clearRelatedEntities() {
		listOfRelatedEntities = null;
		checkStep();
	}

	/**
	 *  Clears the field part of the query and updates the current step
	 */
	public void clearFields() {
		listOfFields = null;
		checkStep();
	}

	/**
	 * Clears the condition part of the query
	 * and updates the current step.
	 */
	public void clearConditions() {
		listOfConditions = null;
		checkStep();
	}

	/**
	 * Gets the current step in the query building process
	 * @return the current step 
	 */
	public int getStep() {
		return step;
	}

	private void checkStep() {
		if (hasConditions())
			step = 4;
		else if (hasFields())
			step = 3;
		else if (hasRelatedEntities())
			step = 2;
		else if (hasSourceEntity() || hasPreviousQuery())
			step = 1;
		else
			step = 0;
		//System.out.println("checkstep : step is now "+step);
	}

	private GenericEntity getEntity(Class entityClass) {
		return (GenericEntity) GenericEntity.getStaticInstance(entityClass);
	}

	/**
	 * @return <CODE>true</CODE> if the query is a template
	 */
	public boolean isTemplate() {
		return isTemplate;
	}

	/**
	 * Sets if the query should be a template for other queries.
	 *
	 */
	public void setTemplate(boolean b) {
		isTemplate = b;
	}

	/**
	 * @return
	 */
	public boolean isEntitiesLock() {
		return entitiesLock;
	}

	/**
	 * @return
	 */
	public boolean isFieldsLock() {
		return fieldsLock;
	}

	/**
	 * @param b
	 */
	public void setEntitiesLock(boolean b) {
		entitiesLock = b;
	}

	/**
	 * @param b
	 */
	public void setFieldsLock(boolean b) {
		fieldsLock = b;
	}

	/**
	 * Searches the entity with the given name
	 * @param name
	 * @return query entity part if found, else null
	 */
	public QueryEntityPart getEntityPart(String name) {
		if (hasSourceEntity() && getSourceEntity().getName().equals(name)) {
			return getSourceEntity();
		}
		else if (hasRelatedEntities()) {
			Iterator iter = getListOfRelatedEntities().iterator();
			while (iter.hasNext()) {
				QueryEntityPart part = (QueryEntityPart) iter.next();
				if (part.getName().equals(name))
					return part;
			}
		}
		return null;
	}

	public boolean hasFieldPart(QueryFieldPart field) {
		if (!hasFields())
			return false;
		else {
			for (Iterator iter = listOfFields.iterator(); iter.hasNext();) {
				QueryFieldPart element = (QueryFieldPart) iter.next();
				if (element.encode().equals(field.encode()))
					return true;
			}
		}
		return false;
	}

	public boolean hasCondition(QueryConditionPart condition) {
		if (!hasConditions())
			return false;
		else {
			for (Iterator iter = listOfConditions.iterator(); iter.hasNext();) {
				QueryConditionPart element = (QueryConditionPart) iter.next();
				if (element.encode().equals(condition.encode()))
					return true;
			}
		}
		return false;
	}

	public boolean hasRelatedEntity(QueryEntityPart entity) {
		if (!hasRelatedEntities())
			return false;
		else {
			for (Iterator iter = listOfRelatedEntities.iterator(); iter.hasNext();) {
				QueryEntityPart element = (QueryEntityPart) iter.next();
				if (element.encode().equals(entity.encode()))
					return true;
			}
		}
		return false;
	}
	
	public String getName()	{
		return name;
	}
	
	public void setName(String name)	{
		this.name = name;
	}
	
	public boolean hasNextQuery() {
		return nextQuery() != null;
	}
	
	public boolean hasPreviousQuery()	{
		return previousQuery() != null;
	}
	
	public QueryHelper previousQuery()	{
		return previousQuery;
	}

	public QueryHelper nextQuery()	{
		return nextQuery;
	}

}
