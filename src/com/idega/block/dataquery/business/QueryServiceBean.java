/*
 * Created on May 22, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.*;
import com.idega.business.IBOServiceBean;
import com.idega.core.component.data.ICObject;
import com.idega.core.component.data.ICObjectBMPBean;
import com.idega.core.component.data.ICObjectHome;
import com.idega.core.data.IWTreeNode;
import com.idega.data.EntityAttribute;
import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.xml.XMLData;
import com.idega.util.xml.XMLFile;

/**
 * @author aron
 */
public class QueryServiceBean extends IBOServiceBean   implements QueryService {

	public QueryHelper getQueryHelper(XMLFile xmlFile){
		XMLData data = XMLData.getInstanceForFile(xmlFile);
		return new QueryHelper(data.getDocument());
	}
	
	public QueryHelper getQueryHelper(int xmlFileID){
		XMLData data = XMLData.getInstanceForFile(xmlFileID);
		return new QueryHelper(data.getDocument());
	}
	
	public QueryHelper getQueryHelper(){
		return new QueryHelper();
	}
	
		
	/**
	 * @return null if nothing found
	 */
	public Collection getSourceQueryEntityParts()throws RemoteException{
		try {
			ICObjectHome objectHome = (ICObjectHome)IDOLookup.getHome(ICObject.class);
			Collection coll = objectHome.findAllByObjectType(ICObjectBMPBean.COMPONENT_TYPE_DATA);
			ArrayList list = new ArrayList(coll.size());
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				ICObject ICObj = (ICObject) iter.next();
        String queryEntityPartName = ICObj.getClassName();
				// thi comment list.add(new QueryEntityPart(ICObj.getName(),ICObj.getClassName()));
        list.add(new QueryEntityPart(queryEntityPartName, queryEntityPartName));
			}
			return list;
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection getRelatedQueryEntityParts(QueryEntityPart sourceEntityPart, int relationDepth)throws ClassNotFoundException{
			return getRelatedQueryEntityParts(Class.forName(sourceEntityPart.getBeanClassName()),relationDepth);	
		}
	
	public Collection getRelatedQueryEntityParts(String sourceEntity, int relationDepth)throws ClassNotFoundException{
		return getRelatedQueryEntityParts(Class.forName(sourceEntity),relationDepth);	
	}
	
	public Collection getManyToManyEntityDefinitions(QueryEntityPart entityPart){
		try {
			IDOEntity entity = IDOLookup.create(Class.forName(entityPart.getBeanClassName()));
			return Arrays.asList(entity.getEntityDefinition().getManyToManyRelatedEntities());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Retrieves a collection of entities related to a givin entity provided the depth
	 *  of interrogation.
	 * @param sourceEntity
	 * @param relationDepth
	 * @return Collection, null if nothing found
	 */
	public Collection getRelatedQueryEntityParts(Class sourceEntity, int relationDepth){
		System.out.println("Investigating "+sourceEntity.getName()+" depth : "+relationDepth);
		GenericEntity entity = (GenericEntity) GenericEntity.getStaticInstance(sourceEntity);
		if(entity==null || relationDepth == 0){
			System.err.println("returns null"); 
			return null;
		}
		 Map map = new HashMap();
		 Iterator iterator =entity.getAttributes().iterator();
		 while (iterator.hasNext()) {
			EntityAttribute attribute = (EntityAttribute) iterator.next();
			Class relationClass = attribute.getRelationShipClass();
			if(relationClass!=null){
				System.out.println("found relation to "+relationClass.getName()+"\t "+attribute.getName());
				 GenericEntity.getStaticInstance(relationClass);
				QueryEntityPart entityPart = new QueryEntityPart(attribute.getName(),relationClass.getName());
				map.put(attribute.getName(),entityPart);
				//Collection relatedColl = getRelatedQueryEntityParts(relationClass,relationDepth--);
				//if(relatedColl!=null)
				//	coll.addAll(relatedColl);				
			}			
		}		
		return map.values();
	}
	
	/** Retrieves a Collection of EntityAttribute objects of the given entity class
	 * @param entityClass
	 * @return Collection, null if nothing found.
	 */
	public Collection getEntityAttributes(Class entityClass){
		GenericEntity entity =  (GenericEntity) GenericEntity.getStaticInstance(entityClass);
		return entity.getAttributes();
	}
	
	public Collection getEntityAttributes(QueryEntityPart entityPart){
			GenericEntity entity =  (GenericEntity) GenericEntity.getStaticInstance(entityPart.getBeanClassName());
			return entity.getAttributes();
	}
	
	private GenericEntity getEntity(Class entityClass){
			return (GenericEntity) GenericEntity.getStaticInstanceIDO(entityClass);
		}
		
	public QueryEntityPart getEntityTree(QueryHelper helper,int level){
		if(helper.hasSourceEntity()){		
			QueryEntityPart source = helper.getSourceEntity();
			QueryEntityPart root = new QueryEntityPart(source.getName(),source.getBeanClassName());
			root.setAsRootNode();
			generateEntityTree(root,level-1);
			return root;
		}
		return null;	
	}
	
	public List getRelatedEntities(QueryHelper helper, int level) {
		List resultList = new ArrayList();
		if(helper.hasSourceEntity()){		
			QueryEntityPart source = helper.getSourceEntity();
			QueryEntityPart root = new QueryEntityPart(source.getName(),source.getBeanClassName());
			root.setPath(source.getBeanClassName());
			resultList.add(root);
			getRelatedEntities(resultList, root,level-1);
		}
		return resultList;	
	}

	
	private void generateEntityTree(QueryEntityPart node,int level){
		if(node !=null){
			// many-to-may entities
			Collection manyToManyEntities = getManyToManyEntityDefinitions(node); 
			Iterator iter ;
			if(manyToManyEntities!=null && !manyToManyEntities.isEmpty()){
				iter = manyToManyEntities.iterator();
				while (iter.hasNext()) {
					IDOEntityDefinition entityDef = (IDOEntityDefinition) iter.next();
					//GenericEntity relatedEntity = getEntity(entityClass);
          String queryEntityPartName = entityDef.getInterfaceClass().getName();
					// thi comment QueryEntityPart child2 = new QueryEntityPart (entityDef.getUniqueEntityName(),entityDef.getInterfaceClass().getName());
          QueryEntityPart child2 = new QueryEntityPart (queryEntityPartName, queryEntityPartName);
					node.addChild(child2);
					if(level >0)
						generateEntityTree(child2,level-1);
					//System.out.println(child2.getNodePath());
				}
			}
			//QueryEntityPart part = (QueryEntityPart) node;
			Collection attributes = getEntityAttributes(node);
			iter = attributes.iterator();
			//IWTreeNode child;
			while (iter.hasNext()) {
				EntityAttribute attribute = (EntityAttribute) iter.next();
				if(attribute.isPartOfManyToOneRelationship()){
					QueryEntityPart child = new QueryEntityPart(attribute.getName(),attribute.getRelationShipClassName());
					//child = getTreeNode(entityPart);
					node.addChild(child);
					//entityPart.setPath(child.getNodePath());
					if(level>0){
						generateEntityTree(child,level-1);
					}	
				}				
			}
			// many to many entities
			
		}
		else
			System.out.println("no object");
	}
	
	private void getRelatedEntities(List resultList, QueryEntityPart node, int level)	{
		if(node !=null){
			// many-to-may entities
			Collection manyToManyEntities = getManyToManyEntityDefinitions(node); 
			Iterator iter ;
			if(manyToManyEntities!=null && !manyToManyEntities.isEmpty()){
				iter = manyToManyEntities.iterator();
				while (iter.hasNext()) {
					IDOEntityDefinition entityDef = (IDOEntityDefinition) iter.next();
					//GenericEntity relatedEntity = getEntity(entityClass);
          String queryEntityPartName = entityDef.getInterfaceClass().getName();
					// thi comment QueryEntityPart child2 = new QueryEntityPart (entityDef.getUniqueEntityName(),entityDef.getInterfaceClass().getName());
          QueryEntityPart child2 = new QueryEntityPart (queryEntityPartName, queryEntityPartName);
          String path = node.getPath() + "#" + queryEntityPartName;
          child2.setPath(path);
					resultList.add(child2);
					if(level >0)
						getRelatedEntities(resultList, child2,level-1);
					//System.out.println(child2.getNodePath());
				}
			}
			//QueryEntityPart part = (QueryEntityPart) node;
			Collection attributes = getEntityAttributes(node);
			iter = attributes.iterator();
			//IWTreeNode child;
			while (iter.hasNext()) {
				EntityAttribute attribute = (EntityAttribute) iter.next();
				if(attribute.isPartOfManyToOneRelationship()){
					QueryEntityPart child = new QueryEntityPart(attribute.getName(),attribute.getRelationShipClassName());
					//child = getTreeNode(entityPart);
					String path = node.getPath() + "#" + attribute.getName();
					child.setPath(path);
					resultList.add(child);
					//entityPart.setPath(child.getNodePath());
					if(level>0){
						getRelatedEntities(resultList, child,level-1);
					}	
				}				
			}
			// many to many entities
			
		}
		else {
			System.out.println("no object");
		}
	}

	
	private IWTreeNode getTreeNode(QueryEntityPart entityPart){
		return new IWTreeNode(entityPart.getName(),entityPart.encode().hashCode(),entityPart);
	}
	
	public Collection getListOfFieldParts(IWResourceBundle iwrb,QueryEntityPart entityPart, boolean expertMode){
		Vector list = new Vector();
		Iterator iter = getEntityAttributes(entityPart).iterator();
		while (iter.hasNext()) {
			EntityAttribute element = (EntityAttribute) iter.next();
			// added by thomas, filter out confusing entities if the query builder does not work in the expert mode
			if (	expertMode || 
						! (	element.isOneToNRelationship() || 
								element.isPartOfManyToOneRelationship() ||
								element.isPrimaryKey())) {
				list.add( createQueryFieldPart(iwrb,entityPart.getBeanClassName(), entityPart.getPath(), element));
			}
		}
		return list;
	}
	
	public QueryFieldPart createQueryFieldPart(IWResourceBundle iwrb,String entityName, String path, EntityAttribute attribute){
		return new QueryFieldPart(attribute.getName(),entityName, path, attribute.getColumnName(),(String)null,iwrb.getLocalizedString(attribute.getName(),attribute.getName()),attribute.getStorageClassName(), false);
	}
	
	
	public QueryResult generateQueryResult(Integer queryID, IWContext iwc) throws QueryGenerationException{
		
		try {
			QueryHelper queryHelper = getQueryHelper(queryID.intValue());
			QueryToSQLBridge bridge = getQueryToSQLBridge();
			SQLQuery query = bridge.createQuerySQL(queryHelper, iwc);
			System.out.println("QueryServece#generateQueryResult - SQL: ");
			//System.out.println(sqlStatement);
			QueryResult queryResult = bridge.executeQueries(query);
			return queryResult;
		}
		catch (RemoteException e) {
			throw new QueryGenerationException(e.getMessage());
		}
	}
	
	public QueryToSQLBridge getQueryToSQLBridge() throws RemoteException {
		return (QueryToSQLBridge)getServiceInstance(QueryToSQLBridge.class);
	  }

}

