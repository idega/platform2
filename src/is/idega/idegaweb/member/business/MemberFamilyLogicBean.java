package is.idega.idegaweb.member.business;

import java.util.*;

import javax.ejb.*;
import java.rmi.RemoteException;

import com.idega.business.IBOServiceBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;

/**
 * Title:        idegaWeb Member User Subsystem
 * Description:  idegaWeb Member User Subsystem is the base system for Membership management
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class MemberFamilyLogicBean extends IBOServiceBean implements MemberFamilyLogic{

  private static final String RELATION_TYPE_GROUP_PARENT="FAM_PARENT";
	private static final String RELATION_TYPE_GROUP_CUSTODIAN="FAM_CUSTODIAN";
  private static final String RELATION_TYPE_GROUP_CHILD="FAM_CHILD";
  private static final String RELATION_TYPE_GROUP_SPOUSE="FAM_SPOUSE";
  private static final String RELATION_TYPE_GROUP_SIBLING="FAM_SIBLING";
  private static final String RELATION_TYPE_GROUP_COHABITANT="FAM_COHABITANT";

  protected UserHome getUserHome(){
    try{
      return (UserHome)this.getIDOHome(User.class);
    }
    catch(RemoteException e){
      throw new EJBException(e.getMessage());
    }
  }

  protected GroupHome getGroupHome(){
    try{
      return (GroupHome)this.getIDOHome(Group.class);
    }
    catch(RemoteException e){
      throw new EJBException(e.getMessage());
    }
  }

  protected GroupRelationHome getGroupRelationHome(){
    try{
      return (GroupRelationHome)this.getIDOHome(GroupRelation.class);
    }
    catch(RemoteException e){
      throw new EJBException(e.getMessage());
    }
  }

  protected Collection convertGroupCollectionToUserCollection(Collection coll){
    Collection newColl = new Vector();
    Iterator iter = coll.iterator();
    while (iter.hasNext()) {
      Group group = (Group)iter.next();
      newColl.add(convertGroupToUser(group));
    }
    return newColl;
  }

  protected User convertGroupToUser(Group group){
    try{
      return getUserBusiness().castUserGroupToUser(group);
    }
    catch(Exception e){
      throw new EJBException("Group "+group+" ("+group.getPrimaryKey().toString()+")  is not a UserGroup");
    }
  }

  protected Group convertUserToGroup(User user){
    try{
      return user.getUserGroup();
    }
    catch(Exception e){
      throw new EJBException("User "+user+" has no a UserGroup");
    }
  }


  /**
   * @return A Collection of User objects (children) who are children of this
   * user. Returns an empty Collection if no children found.
   * @throws NoChildrenFound if no children are found
   */
  public Collection getChildrenFor(User user)throws NoChildrenFound,RemoteException{
    String userName = null;
    try{
      userName = user.getName();
      
      Collection coll = user.getRelatedBy(RELATION_TYPE_GROUP_PARENT);
			
      if(coll==null || coll.isEmpty()){
				throw new NoChildrenFound(userName);
      }
      return convertGroupCollectionToUserCollection(coll);
      //return coll;
    }
    catch(FinderException e){
      throw new NoChildrenFound(userName);
    }
  }
  
	/**
		* @return A Collection of User objects (children) who in his custody. Returns
		* an empty Collection if no children found.
		* @throws NoChildrenFound if no children are found
		*/
	 public Collection getChildrenInCustodyOf(User user)throws NoChildrenFound,RemoteException{
		 String userName = null;
		 try{
			 userName = user.getName();
      
			 Collection coll = user.getRelatedBy(RELATION_TYPE_GROUP_CUSTODIAN);
			
			 if(coll==null || coll.isEmpty()){
				 throw new NoChildrenFound(userName);
			 }
			 return convertGroupCollectionToUserCollection(coll);
			 //return coll;
		 }
		 catch(FinderException e){
			 throw new NoChildrenFound(userName);
		 }
	 }

  /**
   * @return A Collection of User object who are siblings of a user. Returns an empty Collection if no siblings found.
   * @throws NoSiblingFound if no siblings are found
   */
  public Collection getSiblingsFor(User user)throws NoSiblingFound,RemoteException{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getRelatedBy(RELATION_TYPE_GROUP_SIBLING);
      if(coll==null || coll.isEmpty()){
				throw new NoSiblingFound(userName);
      }
      return convertGroupCollectionToUserCollection(coll);
      //return coll;
    }
    catch(FinderException e){
      throw new NoSiblingFound(userName);
    }
  }

  /**
   * @return User object for the spouse of a user.
   * @throws NoSpouseFound if no spouse is found
   */
  public User getSpouseFor(User user)throws NoSpouseFound{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getRelatedBy(RELATION_TYPE_GROUP_SPOUSE);
      Group group = (Group)coll.iterator().next();
      return convertGroupToUser(group);
    }
    catch(Exception e){
      throw new NoSpouseFound(userName);
    }
  }
  
  /**
	* @return User object for the spouse of a user.
	* @throws NoSpouseFound if no spouse is found
	*/
   public User getCohabitantFor(User user)throws NoCohabitantFound{
	 String userName = null;
	 try{
	   userName = user.getName();
	   Collection coll = user.getRelatedBy(RELATION_TYPE_GROUP_COHABITANT);
	   Group group = (Group)coll.iterator().next();
	   return convertGroupToUser(group);
	 }
	 catch(Exception e){
	   throw new NoCohabitantFound(userName);
	 }
   }

  /**
   * @return A Collection of User object who are custodians of a user. If
   * no custodian is found it will return the parents of that user. Returns an
   * empty Collection if no custodians or parents are found.
   * @throws NoCustodianFound if no custodians are found
   */
  public Collection getCustodiansFor(User user,boolean returnParentsIfNotFound)throws NoCustodianFound,RemoteException{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getReverseRelatedBy(RELATION_TYPE_GROUP_CUSTODIAN);
      if(coll==null || coll.isEmpty()){
      	if(returnParentsIfNotFound){
      		try{
      			coll = this.getParentsFor(user);//todo remove this when database is fixed
      			return coll;
      		}
      		catch(NoParentFound ex){
				throw new NoCustodianFound(userName);
      		}
      	}
      	else{
			throw new NoCustodianFound(userName);				
      	}
      }
      return convertGroupCollectionToUserCollection(coll);
    }
    catch(FinderException e){
      throw new NoCustodianFound(userName);
    }
  }
  
  public Collection getCustodiansFor(User user)throws NoCustodianFound,RemoteException{
  	return getCustodiansFor(user,true);
  }
  
	/**
	 * @return A Collection of User object who are parents of a user. Returns an
	 * empty Collection if no parents are found.
	 * @throws NoCustodianFound if no custodians are found
	 */
	public Collection getParentsFor(User user)throws NoParentFound,RemoteException{
		String userName = null;
		try{
			userName = user.getName();
			Collection coll = user.getReverseRelatedBy(RELATION_TYPE_GROUP_PARENT);
			if(coll==null || coll.isEmpty()){
				throw new NoParentFound(userName);
			}
			return convertGroupCollectionToUserCollection(coll);
		}
		catch(FinderException e){
			throw new NoParentFound(userName);
		}
	}

  public boolean hasPersonGotChildren(User person){
    /**
     * @todo Implement better
     */
    try{
      this.getChildrenFor(person);
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  public boolean hasPersonGotSpouse(User person){
    /**
     * @todo Implement better
     */
    try{
      this.getSpouseFor(person);
      return true;
    }
    catch(Exception e){
      return false;
    }
  }
  
  public boolean hasPersonGotCohabitant(User person){
	  /**
	   * @todo Implement better
	   */
	  try{
		this.getCohabitantFor(person);
		return true;
	  }
	  catch(Exception e){
		return false;
	  }
	}

  public boolean hasPersonGotSiblings(User person){
    /**
     * @todo Implement better
     */
    try{
      this.getSiblingsFor(person);
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  /**
   * @returns True if the childToCheck is a child of parent else false
   */
  public boolean isChildOf(User childToCheck,User parent) throws RemoteException {
   try {
			Collection coll = getChildrenFor(parent);
			return coll.contains(childToCheck);
   }
   catch (NoChildrenFound ex) {
   }
   return false;
  }
  
	/**
	 * @returns True if the childToCheck is a child in custody of parent else
	 * false
	 */
	public boolean isChildInCustodyOf(User childToCheck,User parent) throws RemoteException {
	 try {
			Collection coll = getChildrenInCustodyOf(parent);
			return coll.contains(childToCheck);
	 }
	 catch (NoChildrenFound ex) {
	 }
	 return false;
	}
  

  /**
   * @return True if the parentToCheck is the parent of childToCheck else false
   */
  public boolean isParentOf(User parentToCheck,User child) throws RemoteException {
    try {
      Collection coll = getParentsFor(child);
      return coll.contains(parentToCheck);
    }
    catch (NoParentFound ex) {
      return false;
    }
  }
  
	/**
	 * @return True if the custodianToCheck is the custodian of childToCheck else false
	 */
	public boolean isCustodianOf(User custodianToCheck,User child) throws RemoteException {
		try {
			Collection coll = getChildrenInCustodyOf(custodianToCheck);
						
			return coll.contains(child);
		}
		catch (NoChildrenFound ex) {
			return false;
		}
	}

  /**
   * @return True if the personToCheck is a spouse of relatedPerson else false
   */
  public boolean isSpouseOf(User personToCheck,User relatedPerson) throws RemoteException {
    if ( this.hasPersonGotSpouse(personToCheck) ) {
      try{
				User spouse = this.getSpouseFor(personToCheck);
				if( spouse.equals(relatedPerson) ) {
				  return true;
				}
      }
      catch (NoSpouseFound nsf) {
      }
    }
    return false;
  }
  
  /**
	* @return True if the personToCheck is a spouse of relatedPerson else false
	*/
   public boolean isCohabitantOf(User personToCheck,User relatedPerson) throws RemoteException {
	 if ( this.hasPersonGotCohabitant(personToCheck) ) {
	   try{
				 User cohabitant = this.getCohabitantFor(personToCheck);
				 if( cohabitant.equals(relatedPerson) ) {
				   return true;
				 }
	   }
	   catch (NoCohabitantFound nsf) {
	   }
	 }
	 return false;
   }

  /**
   * @return True if the personToCheck is a sibling of relatedPerson else false
   */
  public boolean isSiblingOf(User personToCheck,User relatedPerson) throws RemoteException {
    try {
      Collection coll = getSiblingsFor(relatedPerson);
      return coll.contains(personToCheck);
    }
    catch (NoSiblingFound ex) {
      return false;
    }
  }

  public void setAsChildFor(User personToSet,User parent)throws CreateException,RemoteException{
    if(!this.isChildOf(personToSet,parent)){
      personToSet.addUniqueRelation(convertUserToGroup(parent),RELATION_TYPE_GROUP_CHILD);
      parent.addUniqueRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_PARENT);
    }
  }

  public void setAsParentFor(User parent,User child)throws CreateException,RemoteException{
    if(!this.isParentOf(parent,child)){
      child.addUniqueRelation(convertUserToGroup(parent),RELATION_TYPE_GROUP_CHILD);
      parent.addUniqueRelation(convertUserToGroup(child),RELATION_TYPE_GROUP_PARENT);
    }
  }
  
	public void setAsCustodianFor(User custodian,User child)throws CreateException,RemoteException{
		if(!this.isCustodianOf(custodian,child)){
			child.addUniqueRelation(convertUserToGroup(custodian),RELATION_TYPE_GROUP_CHILD);
			custodian.addUniqueRelation(convertUserToGroup(child),RELATION_TYPE_GROUP_CUSTODIAN);
		}
	}

  public void setAsSpouseFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    if(!this.isSpouseOf(personToSet,relatedPerson)){
      personToSet.addUniqueRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_SPOUSE);
      relatedPerson.addRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_SPOUSE);
    }
  }
  
  public void setAsCohabitantFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
	  if(!this.isCohabitantOf(personToSet,relatedPerson)){
		personToSet.addUniqueRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_COHABITANT);
		relatedPerson.addRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_COHABITANT);
	  }
	}

  public void setAsSiblingFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    if(!this.isSiblingOf(personToSet,relatedPerson)){
      personToSet.addUniqueRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_SIBLING);
      relatedPerson.addRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_SIBLING);
      /* Changed from addUnique 2 addRelation so that relation would be created
       * relatedPerson.addUniqueRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SIBLING);
       */
    }
  }

  public void removeAsChildFor(User personToSet,User parent)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(parent),RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsParentFor(User parent,User child)throws RemoveException,RemoteException{
    child.removeRelation(convertUserToGroup(parent),RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(convertUserToGroup(child),RELATION_TYPE_GROUP_PARENT);
  }
  
	public void removeAsCustodianFor(User custodian,User child)throws RemoveException,RemoteException{
		child.removeRelation(convertUserToGroup(custodian),RELATION_TYPE_GROUP_CHILD);
		custodian.removeRelation(convertUserToGroup(child),RELATION_TYPE_GROUP_CUSTODIAN);
	}

  public void removeAsSpouseFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.removeRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_SPOUSE);
  }
  
  public void removeAsCohabitantFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
	 personToSet.removeRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_COHABITANT);
	 relatedPerson.removeRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_COHABITANT);
   }

  public void removeAsSiblingFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(relatedPerson),RELATION_TYPE_GROUP_SIBLING);
    relatedPerson.removeRelation(convertUserToGroup(personToSet),RELATION_TYPE_GROUP_SIBLING);
  }


	/**
	 * Returns the RELATION_TYPE_GROUP_CHILD.
	 * @return String
	 */
	public String getChildRelationType() {
		return RELATION_TYPE_GROUP_CHILD;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_PARENT.
	 * @return String
	 */
	public String getParentRelationType() {
		return RELATION_TYPE_GROUP_PARENT;
	}
	

	/**
	 * Returns the RELATION_TYPE_GROUP_SIBLING.
	 * @return String
	 */
	public String getSiblingRelationType() {
		return RELATION_TYPE_GROUP_SIBLING;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_SPOUSE.
	 * @return String
	 */
	public String getSpouseRelationType() {
		return RELATION_TYPE_GROUP_SPOUSE;
	}
	
	/**
	 * Returns the RELATION_TYPE_GROUP_COHABITANT.
	 * @return String
	 */
	public String getCohabitantRelationType() {
		return RELATION_TYPE_GROUP_COHABITANT;
	}
	
	/**
	 * Returns the RELATION_TYPE_GROUP_CUSTODIAN.
	 * @return String
	 */
	public String getCustodianRelationType() {
		return RELATION_TYPE_GROUP_CUSTODIAN;
	}

	public UserBusiness getUserBusiness()throws RemoteException{
		return (UserBusiness)this.getServiceInstance(UserBusiness.class);	
	}
	
	public void removeAllFamilyRelationsForUser(User user) throws RemoteException{
		try {
			Collection children = getChildrenFor(user);
			if( children != null ){
				Iterator kids = children.iterator();
				while (kids.hasNext()) {
					User child = (User) kids.next();
					removeAsChildFor(child,user);
				}
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x){}
		
		try {
			Collection children = getChildrenInCustodyOf(user);
			if( children != null ){
				Iterator kids = children.iterator();
				while (kids.hasNext()) {
					User child = (User) kids.next();
					removeAsCustodianFor(user,child);
				}
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x){}

		try {
			User spouse = getSpouseFor(user);
			if( spouse != null ){
				removeAsSpouseFor(spouse,user);
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch(NoSpouseFound x){}

		try {
			Collection parents = getParentsFor(user);
			if( parents != null ){
				Iterator ents = parents.iterator();
				while (ents.hasNext()) {
					User ent = (User) ents.next();
					removeAsParentFor(ent,user);
				}
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}		
		catch (NoParentFound x){}	
		
		try {
			Collection custodians = getCustodiansFor(user);
			if( custodians != null ){
				Iterator ents = custodians.iterator();
				while (ents.hasNext()) {
					User ent = (User) ents.next();
					removeAsParentFor(ent,user);
				}
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}		
		catch (NoCustodianFound x){}	
		
		
		try {
			Collection siblings = getSiblingsFor(user);
			if( siblings != null ){
				Iterator sibling = siblings.iterator();
				while (sibling.hasNext()) {
					User sibl = (User) sibling.next();
					removeAsSiblingFor(sibl,user);
				}
			}
	
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}		
		catch (NoSiblingFound x){}	
		
	}

}