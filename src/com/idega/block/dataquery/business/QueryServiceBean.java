/*
 * Created on May 22, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;


import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.dataquery.data.QueryConstants;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.dataquery.data.QuerySequence;
import com.idega.block.dataquery.data.QuerySequenceHome;
import com.idega.block.dataquery.data.UserQuery;
import com.idega.block.dataquery.data.UserQueryHome;
import com.idega.block.dataquery.data.sql.SQLQuery;
import com.idega.block.dataquery.data.xml.QueryEntityPart;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.component.data.ICObject;
import com.idega.core.component.data.ICObjectBMPBean;
import com.idega.core.component.data.ICObjectHome;
import com.idega.core.file.data.ICFile;
import com.idega.data.EntityAttribute;
import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.xml.XMLData;

/**
 * @author aron
 * @author thomas
 */
public class QueryServiceBean extends IBOServiceBean implements QueryService  {
	
	private static final String COUNTER_TOKEN = "_"; 
	private static final String DEFAULT_QUERY_NAME = "my query";

	
	private UserQueryHome userQueryHome;
	private QuerySequenceHome querySequenceHome;

	public QueryHelper getQueryHelper(UserQuery userQuery, IWContext iwc ) throws NumberFormatException, FinderException, IOException{
		XMLData data = XMLData.getInstanceForFile(userQuery.getSource());
		return new QueryHelper(data, userQuery, iwc);
	}
	
	public QueryHelper getQueryHelper(int userQueryID, IWContext  iwc) throws NumberFormatException, FinderException, IOException{
		UserQuery userQuery = getUserQueryHome().findByPrimaryKey(new Integer(userQueryID));
		return getQueryHelper(userQuery, iwc);
	}
	
	public QueryHelper getQueryHelperByNameAndPathToQuerySequence(String name, String path, IWContext iwc) throws NumberFormatException, FinderException, IOException {
		String id = StringHandler.substringEnclosedBy(path, "(",")");
		QuerySequence querySequence;
		if (id != null && id.length() > 0) {
			querySequence = getQuerySequenceHome().findByPrimaryKey(new Integer(id));
		}
		else {
			querySequence = getQuerySequenceHome().findByName(name);
		}
		UserQuery userQuery = querySequence.getRealQuery();
		return getQueryHelper(userQuery, iwc);
	}
		
	
	public QueryHelper getQueryHelper(){
		return new QueryHelper();
	}
	
		
	public Collection getInputHandlerNames()	{
		try {
			ICObjectHome objectHome = (ICObjectHome)IDOLookup.getHome(ICObject.class);
			Collection coll = objectHome.findAllByObjectType(ICObjectBMPBean.COMPONENT_TYPE_INPUTHANDLER);
			ArrayList list = new ArrayList(coll.size());
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				ICObject ICObj = (ICObject) iter.next();
        String className = ICObj.getClassName();
	      list.add(className);
			}
			return list;
		}
		catch (IDOLookupException e) {
			log(e);
			log("[QueryService] Can't find ICObject home.");
		}
		catch (FinderException e) {
			log(e);
			log("[QueryService] Can't find InputHandler");
		}
		return null;
	}

	
	/**
	 * @return null if nothing found
	 */
	public Collection getSourceQueryEntityParts() {
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
			GenericEntity entity =  GenericEntity.getStaticInstance(entityPart.getBeanClassName());
			return entity.getAttributes();
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
          String queryEntityPartName = entityDef.getInterfaceClass().getName();
          QueryEntityPart child2 = new QueryEntityPart (queryEntityPartName, queryEntityPartName);
          StringBuffer buffer = new StringBuffer(node.getPath());
          buffer.append(QueryConstants.ENTITY_PATH_DELIMITER);
          buffer.append(queryEntityPartName);
          child2.setPath(buffer.toString());
					resultList.add(child2);
					if(level >0)
						getRelatedEntities(resultList, child2,level-1);
				}
			}
			Collection attributes = getEntityAttributes(node);
			iter = attributes.iterator();
			//IWTreeNode child;
			while (iter.hasNext()) {
				EntityAttribute attribute = (EntityAttribute) iter.next();
				if(attribute.isPartOfManyToOneRelationship()){
					QueryEntityPart child = new QueryEntityPart(attribute.getName(),attribute.getRelationShipClassName());
          StringBuffer buffer = new StringBuffer(node.getPath());
          buffer.append(QueryConstants.ENTITY_PATH_DELIMITER);
          buffer.append(attribute.getName());
          child.setPath(buffer.toString());
					resultList.add(child);
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
		// name, aliasName, entity, path, column,function, display, typeClass, handlerClass, handlerDescription
		return new QueryFieldPart(attribute.getName(), null, entityName, path, attribute.getColumnName(),(String)null,null,attribute.getStorageClassName(), null, null);
	}
	
	
	public QueryResult generateQueryResult(Integer userQueryID, IWContext iwc) throws QueryGenerationException{	
		try {
			QueryHelper queryHelper = getQueryHelper(userQueryID.intValue(),iwc);
			QueryToSQLBridge bridge = getQueryToSQLBridge();
			SQLQuery query = bridge.createQuerySQL(queryHelper, iwc);
			System.out.println("QueryServece#generateQueryResult - SQL: ");
			//System.out.println(sqlStatement);
			QueryResult queryResult = bridge.executeQueries(query);
			return queryResult;
		}
		catch (FinderException finderEx) {
			throw new QueryGenerationException(finderEx.getMessage());
		}
		catch (RemoteException e) {
			throw new QueryGenerationException(e.getMessage());
		}
		catch (IOException ioEx) {
			throw new QueryGenerationException(ioEx.getMessage());
		}
	}
	
	public QueryToSQLBridge getQueryToSQLBridge() throws RemoteException {
		return (QueryToSQLBridge)getServiceInstance(QueryToSQLBridge.class);
	  }
	
	public UserQuery storeOrUpdateQuery(String name, QueryHelper queryHelper, boolean isPrivate, boolean overwriteQuery, IWUserContext iwuc) {
		UserQuery userQuery = null;
		UserTransaction transaction = getSessionContext().getUserTransaction();
  	try {
			transaction.begin();
	  	userQuery = storeOrUpdateQueryWithoutTransaction(name, queryHelper, isPrivate, overwriteQuery, iwuc);
	  	transaction.commit();
	  	
  	}
		catch (Exception e) {
			logError("[QueryService] Could not store or update UserQuery");
			log(e);
			userQuery = null;
			if (transaction != null) {
				try {
					transaction.rollback();
				}
				catch (SystemException se) {
					logError("[QueryService] Could not rollback (store or update query)");
					log(se);
				}
			}
		}
		return userQuery;
	}
	
	/**
	 *  queryFile might be null.
	 */ 
	private UserQuery storeOrUpdateQueryWithoutTransaction(String name, QueryHelper queryHelper, boolean isPrivate, boolean overwriteQuery, IWUserContext iwuc) 
			throws IDOStoreException, IOException, CreateException, SQLException, FinderException {
		Group group = getCorrespondingGroup(iwuc);
		// get user query, get xml data
		XMLData data = null;
		UserQuery userQuery = queryHelper.getUserQuery();
		if (userQuery !=null  && overwriteQuery) {
			name = modifyNameIfNameAlreadyExistsIgnoreUserQuery(userQuery,name, group);
			// case: query is modified and should be overwritten
			data = XMLData.getInstanceForFile(userQuery.getSource());
			// update user query and data
			// store old name before updating
			String oldName = userQuery.getName();
			updateQueryData(name, userQuery, data, queryHelper, group, isPrivate);
			// do we have to create a new query sequence?
			if (!oldName.equals(name)) {
				createQuerySequence(name, userQuery, queryHelper);
			}
		}
		else {
			// case: brand new query
			name = modifyNameIfNameAlreadyExists(name, group);
			userQuery = getUserQueryHome().create();
			// store to be sure that the primary key is set !
			data = XMLData.getInstanceWithoutExistingFile();
			// update user query and data
			updateQueryData(name, userQuery, data, queryHelper, group, isPrivate);
			createQuerySequence(name, userQuery, queryHelper);
		}
		return userQuery;
	}
	
	
	private Group getTopGroupForCurrentUser(IWUserContext iwuc) throws RemoteException {
		User currentUser = iwuc.getCurrentUser();
		UserBusiness userBusiness = getUserBusiness();
		// TODO: thi solve problem with group types
		String[] groupTypes = 
			{ "iwme_federation", "iwme_union", "iwme_regional_union",  "iwme_league", "iwme_club", "iwme_club_division"};
		Group group = userBusiness.getUsersHighestTopGroupNode(currentUser, Arrays.asList(groupTypes), iwuc);
		if (group == null) {
			List groupType = new ArrayList();
			groupType.add("general");
			group = userBusiness.getUsersHighestTopGroupNode(currentUser, groupType, iwuc);
		}
		return group;
	}

	 	
	public UserQuery storeQuery(String name, ICFile file, boolean isPrivate, Object userQueryToBeReplacedId, IWUserContext iwuc) {
		UserQuery userQuery = null;
		UserTransaction transaction = getSessionContext().getUserTransaction();
  	try {
			transaction.begin();
	  	userQuery = storeQueryWithoutTransaction(name, file, isPrivate, userQueryToBeReplacedId, iwuc);
	  	transaction.commit();
	  	
  	}
		catch (Exception e) {
			logError("[QueryService] Could not store UserQuery");
			log(e);
			userQuery = null;
			if (transaction != null) {
				try {
					transaction.rollback();
				}
				catch (SystemException se) {
					logError("[QueryService] Could not rollback (store query)");
					log(se);
				}
			}
		}
		return userQuery;
	}
	
	
	private UserQuery storeQueryWithoutTransaction(String name, ICFile file, boolean isPrivate, Object userQueryToBeReplacedId, IWUserContext iwuc) throws CreateException, FinderException, RemoteException {
		Group group = getCorrespondingGroup(iwuc);
		name = modifyNameIfNameAlreadyExists(name, group);
		UserQueryHome queryHome = getUserQueryHome();
		UserQuery userQuery = queryHome.create();
		userQuery.setName(name);
		userQuery.setOwnership(group);
		userQuery.setSource(file);
		if (isPrivate) {
			userQuery.setPermission(QueryConstants.PERMISSION_PRIVATE_QUERY);
		}
		else {
			userQuery.setPermission(QueryConstants.PERMISSION_PUBLIC_QUERY);
		}
		userQuery.store();
		QuerySequenceHome sequenceHome = getQuerySequenceHome();
		QuerySequence querySequence = sequenceHome.create();
		// be sure that user query was stored before ((otherwise there is a problem with non existing primary key)
		querySequence.setRealQuery(userQuery);
		querySequence.setName(name);
		// first store (otherwise there is a problem with non existing primary key)
		querySequence.store();
		userQuery.setRoot(querySequence);
		userQuery.store();
		// set new pointers
		if (userQueryToBeReplacedId != null) {
			//  create new sequence
			UserQuery oldUserQuery = queryHome.findByPrimaryKey(userQueryToBeReplacedId);
			Collection sequences = sequenceHome.findAllByRealQuery(oldUserQuery);
			Iterator iterator = sequences.iterator();
			while (iterator.hasNext()) {
				QuerySequence sequence = (QuerySequence) iterator.next();
				sequence.setRealQuery(userQuery);
				sequence.store();
			}
			// create new sequence for the existing old user query
			QuerySequence sequence = sequenceHome.create();
			String oldUserQueryName = oldUserQuery.getName();
			sequence.setName(oldUserQueryName);
			sequence.setRealQuery(oldUserQuery);
			sequence.store();
			oldUserQuery.setRoot(sequence);
			oldUserQuery.store();
		}
		return userQuery;
	}
	
	/**
	 * @param iwuc
	 * @return
	 * @throws RemoteException
	 */
	private Group getCorrespondingGroup(IWUserContext iwuc) throws RemoteException {
		Group group = null;
		if (iwuc.isSuperAdmin()) {
	  		User user = iwuc.getCurrentUser();
	  		group = user;
		}
		else {
		 group = getTopGroupForCurrentUser(iwuc);
		}
		return group;
	}

	private String modifyNameIfNameAlreadyExists(String name, Group group) throws FinderException {
		return modifyNameIfNameAlreadyExistsIgnoreUserQuery(null, name, group );
	}
	
	
	private String modifyNameIfNameAlreadyExistsIgnoreUserQuery(UserQuery ignoredUserQuery, String name, Group group) throws FinderException {
		if (name == null || name.length() ==0) {
			name = DEFAULT_QUERY_NAME;
		}
		String ignoredId = null;
		if (ignoredUserQuery != null) {
			ignoredId = ignoredUserQuery.getPrimaryKey().toString();
		}
		Collection coll = getUserQueriesByGroup(group);
		Collection existingStrings = new ArrayList(coll.size()); 
		Iterator iterator = coll.iterator();
		while (iterator.hasNext()) {
			UserQuery userQuery = (UserQuery) iterator.next();
			String id = userQuery.getPrimaryKey().toString();
			if (! id.equals(ignoredId)) {
				existingStrings.add(userQuery.getName());
			}
		}
		return StringHandler.addOrIncreaseCounterIfNecessary(name,COUNTER_TOKEN,existingStrings);
	}

	
	private void updateQueryData(String name, UserQuery userQuery, XMLData data, QueryHelper queryHelper, Group owner, boolean isPrivate) throws IOException {
		// name within the query
		queryHelper.setName(name);
		// name of the file 
		data.setName(name);
		// name of the user query
		userQuery.setName(name);
		data.setDocument(queryHelper.createDocument());
		ICFile modifiedQuery =  data.store();
		// connect file with query again or the first time
		userQuery.setSource(modifiedQuery);
		userQuery.setOwnership(owner);
		if (isPrivate) {
			userQuery.setPermission(QueryConstants.PERMISSION_PRIVATE_QUERY);
		}
		else {
			userQuery.setPermission(QueryConstants.PERMISSION_PUBLIC_QUERY);
		}
		userQuery.store();
	}

	private Collection getUserQueriesByGroup(Group group) throws FinderException {
  	 UserQueryHome queryHome = getUserQueryHome();
  	 Collection privateUserQueries = queryHome.findByGroupAndPermission(group, QueryConstants.PERMISSION_PRIVATE_QUERY);
  	 Collection publicUserQueries = queryHome.findByGroupAndPermission(group, QueryConstants.PERMISSION_PUBLIC_QUERY);
  	 privateUserQueries.addAll(publicUserQueries);
  	 return privateUserQueries;
  }
		

	private QuerySequence createQuerySequence(String name, UserQuery userQuery, QueryHelper queryHelper) throws CreateException, SQLException {
			QuerySequence querySequence = getQuerySequenceHome().create();
			// be sure that user query was stored before ((otherwise there is a problem with non existing primary key)
			querySequence.setRealQuery(userQuery);
			querySequence.setName(name);
			// first store (otherwise there is a problem with non existing primary key)
			querySequence.store();
			// add previous query
			List previousQueries = queryHelper.previousQueries();
			Iterator iterator = previousQueries.iterator();
			while (iterator.hasNext()) {
				QueryHelper previousQuery = (QueryHelper) iterator.next();
				UserQuery previousUserQuery = previousQuery.getUserQuery();
				QuerySequence previousRoot = previousUserQuery.getRoot();
				querySequence.addChild(previousRoot);
			}
			querySequence.store();
			// first store (otherwise there is a problem with non existing primary key)
			userQuery.setRoot(querySequence);
			userQuery.store();
			return querySequence;
	}

	public UserBusiness getUserBusiness()	{
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve UserBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve UserBusiness");
		}
	}	
	
	
	
		
  public QuerySequenceHome getQuerySequenceHome(){
    if(querySequenceHome==null){
      try{
        querySequenceHome = (QuerySequenceHome)IDOLookup.getHome(QuerySequence.class);
      }
      catch(RemoteException rme){
        throw new RuntimeException(rme.getMessage());
      }
    }
    return querySequenceHome;
  }
		
  private UserQueryHome getUserQueryHome(){
    if(userQueryHome==null){
      try{
        userQueryHome = (UserQueryHome)IDOLookup.getHome(UserQuery.class);
      }
      catch(RemoteException rme){
        throw new RuntimeException(rme.getMessage());
      }
    }
    return userQueryHome;
  }
		
  public void removeUserQuery(Integer userQueryId, User user) {
  	UserTransaction transaction = getSessionContext().getUserTransaction();
  	try {
			transaction.begin();
	  	UserQuery userQuery = getUserQueryHome().findByPrimaryKey(userQueryId);
	  	userQuery.setDeleted(true);
	  	userQuery.setDeletedBy(user);
	  	userQuery.setDeletedWhen(IWTimestamp.getTimestampRightNow());
	  	userQuery.store();
	  	transaction.commit();
  	}
		catch (Exception e) {
			logError("[QueryService] Could not mark UserQuery as deleted");
			log(e);
			if (transaction != null) {
				try {
					transaction.rollback();
				}
				catch (SystemException se) {
					logError("[QueryService] Could not rollback");
					log(se);
				}
			}
		}
  }


		
}

