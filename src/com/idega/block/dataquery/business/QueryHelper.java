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
	
	private XMLDocument doc = null;
	private XMLElement root = null;
	private QueryEntityPart sourceEntity = null;
	private List listOfRelatedEntities = null;
	private List listOfFields = null;
	private List listOfConditions = null;
		
	public QueryHelper(){
	}
		
	public QueryHelper(InputStream stream) throws XMLException,Exception{
		doc = new XMLParser().parse(stream);
		init();
	}
	
	public QueryHelper(XMLDocument document){
		doc = document;
		init();
	}
	
	private void init(){
		root = doc.getRootElement();
		if(root!=null){
		
		XMLElement source = root.getChild(QueryXMLConstants.SOURCE_ENTITY);
		if(source!=null){
			// SOURCE ENTITY PART (STEP 1)
			XMLElement entity = source.getChild(QueryXMLConstants.ENTITY);
			if(entity !=null){
				sourceEntity = new QueryEntityPart(entity);
				// RELATED PART ( STEP 2)
				if(sourceEntity!=null){
					XMLElement related= root.getChild(QueryXMLConstants.RELATED_ENTITIES);
					if(related!=null && related.hasChildren()){
						listOfRelatedEntities = new ArrayList();
						Iterator entities = related.getChildren().iterator();
						while(entities.hasNext()){
							XMLElement xmlEntity = (XMLElement) entities.next();
							listOfRelatedEntities.add(new QueryEntityPart(xmlEntity));
						}
						
						// FIELD PART (STEP 3)
						XMLElement fields = root.getChild(QueryXMLConstants.FIELDS);
						if(fields!=null && fields.hasChildren()){
							listOfFields = new ArrayList();
							Iterator iter = fields.getChildren().iterator();
							while(iter.hasNext()){
								XMLElement xmlField = (XMLElement) iter.next();
								listOfFields.add(new QueryFieldPart(xmlField));
							}
							
							// CONDITION PART (STEP 4)
							XMLElement conditions = root.getChild(QueryXMLConstants.CONDITIONS);
							if(conditions!=null && conditions.hasChildren()){
								listOfConditions = new ArrayList();
								Iterator conds = conditions.getChildren().iterator();
								while(conds.hasNext()){
									XMLElement xmlCondition = (XMLElement)conds.next();
									listOfConditions.add(new QueryConditionPart(xmlCondition));
								}
							}
						}
					}
				}
			}			
		}
		}
	}
	
	private XMLElement getRootElement(){
		if(root==null)
			root =  new XMLElement(QueryXMLConstants.ROOT);
		return root;
	}
	
	private XMLElement getRelatedElement(){
		return new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
	}
	private XMLElement getFieldsElement(){
		return new XMLElement(QueryXMLConstants.FIELDS);
	}
	
	public XMLDocument createDocument(){
		if(doc == null)
			doc = new XMLDocument(getRootElement());
		//		SOURCE ENTITY PART (STEP 1)
		if(sourceEntity!=null){
			root.addContent(sourceEntity.getQueryElement());
			//	RELATED PART ( STEP 2)
			if(listOfRelatedEntities!=null && !listOfRelatedEntities.isEmpty()){
				Iterator iter = listOfRelatedEntities.iterator();
				XMLElement related = new XMLElement(QueryXMLConstants.RELATED_ENTITIES);
				while(iter.hasNext()){
					related.addContent(((QueryPart)iter.next()).getQueryElement());
				}
				root.addContent(related);
				
				//	FIELD PART (STEP 3)
				if(listOfFields!=null && !listOfFields.isEmpty()){
					iter = listOfFields.iterator();
					XMLElement fields = new XMLElement(QueryXMLConstants.FIELDS);
					while(iter.hasNext()){
						fields.addContent(((QueryPart)iter.next()).getQueryElement());
					}
					root.addContent(fields);
					
					//					CONDITION PART (STEP 4)
					if(listOfConditions!=null && !listOfConditions.isEmpty()){
						iter = listOfConditions.iterator();
						XMLElement conditions = new XMLElement(QueryXMLConstants.CONDITIONS);
						while(iter.hasNext()){
							conditions.addContent(((QueryPart)iter.next()).getQueryElement());
						}
						root.addContent(conditions);
					}
				}
			}
		}
		return doc;
	}
	
	public boolean hasSourceEntity(){
		return sourceEntity!=null;
	}
	public boolean hasRelatedEntities(){
		return listOfRelatedEntities !=null;
	}
	public boolean hasFields(){
		return listOfFields!=null;
	}
	public boolean hasCondtions(){
		return listOfConditions!=null;
	}

	/**
	 * @return
	 */
	public XMLDocument getDoc() {
		return doc;
	}

	/**
	 * @return
	 */
	public List getListOfConditions() {
		return listOfConditions;
	}

	/**
	 * @return
	 */
	public List getListOfFields() {
		return listOfFields;
	}

	/**
	 * @return
	 */
	public List getListOfRelatedEntities() {
		return listOfRelatedEntities;
	}

	/**
	 * @return
	 */
	public XMLElement getRoot() {
		return root;
	}

	/**
	 * @return
	 */
	public QueryEntityPart getSourceEntity() {
		return sourceEntity;
	}

	/**
	 * @param document
	 */
	public void setDoc(XMLDocument document) {
		doc = document;
	}

	/**
	 * @param list
	 */
	public void setListOfConditions(List list) {
		listOfConditions = list;
	}

	/**
	 * @param list
	 */
	public void setListOfFields(List list) {
		listOfFields = list;
	}

	/**
	 * @param list
	 */
	public void setListOfRelatedEntities(List list) {
		listOfRelatedEntities = list;
	}

	/**
	 * @param element
	 */
	public void setRoot(XMLElement element) {
		root = element;
	}

	/**
	 * @param part
	 */
	public void setSourceEntity(QueryEntityPart part) {
		sourceEntity = part;
	}
	
	public void addRelatedEntity(QueryEntityPart entity){
		if(listOfRelatedEntities==null)
			listOfRelatedEntities = new ArrayList();
		listOfRelatedEntities.add(entity);
	}
	
	public void addField(QueryFieldPart field){
		if(listOfFields!=null)
			listOfFields = new ArrayList();
		listOfFields.add(field);
	}
	
	public void addCondition(QueryConditionPart condition){
		if(listOfConditions== null)
			listOfConditions = new ArrayList();
		listOfConditions.add(condition);
	}
	
	public void clearRelatedEntities(){
		listOfRelatedEntities = null;
	}
	public void clearFields(){
		listOfFields = null;
	}
	public void clearConditions(){
		listOfConditions = null;
	}

}
