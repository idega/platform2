/*
 * Created on May 22, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.idega.business.IBOServiceBean;
import com.idega.core.IWTreeNode;
import com.idega.data.EntityAttribute;
import com.idega.data.EntityControl;
import com.idega.data.GenericEntity;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.xml.XMLData;
import com.idega.util.xml.XMLFile;

/**
 * @author aron
 */
public class QueryServiceBean extends IBOServiceBean implements QueryService {

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
	
		
	//TODO do properly
	public Collection getSourceQueryEntityParts(){
		Collection coll = new ArrayList(2);
		GenericEntity ent1 = getEntity(com.idega.user.data.User.class);
		GenericEntity ent2 = getEntity(com.idega.user.data.Group.class);
		coll.add(new QueryEntityPart(ent1.getEntityName(),ent1.getClass().getName()));
		coll.add(new QueryEntityPart(ent2.getEntityName(),ent2.getClass().getName()));
		return coll;
	}
	
	public Collection getRelatedQueryEntityParts(QueryEntityPart sourceEntityPart, int relationDepth)throws ClassNotFoundException{
			return getRelatedQueryEntityParts(Class.forName(sourceEntityPart.getBeanClassName()),relationDepth);	
		}
	
	public Collection getRelatedQueryEntityParts(String sourceEntity, int relationDepth)throws ClassNotFoundException{
		return getRelatedQueryEntityParts(Class.forName(sourceEntity),relationDepth);	
	}
	
	public Collection getManyToManyEntities(QueryEntityPart entityPart){
		try {
			return EntityControl.getManyToManyRelationShipClasses(Class.forName(entityPart.getBeanClassName()));
		}
		catch (ClassNotFoundException e) {
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
			return (GenericEntity) GenericEntity.getStaticInstance(entityClass);
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
	
	private void generateEntityTree(QueryEntityPart node,int level){
		if(node !=null){
			// one-to-may entities
			Collection manyToManyEntities = getManyToManyEntities(node); 
			Iterator iter ;
			if(manyToManyEntities!=null && !manyToManyEntities.isEmpty()){
				iter = manyToManyEntities.iterator();
				while (iter.hasNext()) {
					Class entityClass = (Class) iter.next();
					GenericEntity relatedEntity = getEntity(entityClass);
					QueryEntityPart child2 = new QueryEntityPart (relatedEntity.getEntityName(),relatedEntity.getClass().getName());
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
	
	private IWTreeNode getTreeNode(QueryEntityPart entityPart){
		return new IWTreeNode(entityPart.getName(),entityPart.encode().hashCode(),entityPart);
	}
	
	public Collection getListOfFieldParts(IWResourceBundle iwrb,QueryEntityPart entityPart){
		Vector list = new Vector();
		Iterator iter = getEntityAttributes(entityPart).iterator();
		while (iter.hasNext()) {
			EntityAttribute element = (EntityAttribute) iter.next();
			list.add( createQueryFieldPart(iwrb,entityPart.getName(),element));
		}
		return list;
	}
	
	public QueryFieldPart createQueryFieldPart(IWResourceBundle iwrb,String entityName,EntityAttribute attribute){
		return new QueryFieldPart(attribute.getName(),entityName,attribute.getColumnName(),(String)null,iwrb.getLocalizedString(attribute.getName(),attribute.getName()),attribute.getStorageClassName());
	}

}

