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
  private static final String RELATION_TYPE_GROUP_CHILD="FAM_CHILD";
  private static final String RELATION_TYPE_GROUP_SPOUSE="FAM_SPOUSE";
  private static final String RELATION_TYPE_GROUP_SIBLING="FAM_SIBLING";

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
      throw new EJBException("Group "+group+" is not a UserGroup");
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
   * @return A Collection of User object who are children of a user. Returns an empty Collection if no children found.
   * @throws NoChildrenFound if no children are found
   */
  public Collection getChildrenFor(User user)throws NoChildrenFound,RemoteException{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getRelatedBy(this.RELATION_TYPE_GROUP_PARENT);
      if(coll==null){
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
      Collection coll = user.getRelatedBy(this.RELATION_TYPE_GROUP_SIBLING);
      if(coll==null){
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
      Collection coll = user.getRelatedBy(this.RELATION_TYPE_GROUP_SPOUSE);
      Group group = (Group)coll.iterator().next();
      return convertGroupToUser(group);
    }
    catch(Exception e){
      throw new NoSpouseFound(userName);
    }
  }

  /**
   * @return A Collection of User object who are custodians of a user. Returns an empty Collection if no custodians are found.
   * @throws NoCustodianFound if no custodians are found
   */
  public Collection getCustodiansFor(User user)throws NoCustodianFound,RemoteException{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getRelatedBy(this.RELATION_TYPE_GROUP_CHILD);
      if(coll==null){
	throw new NoCustodianFound(userName);
      }
      return convertGroupCollectionToUserCollection(coll);
    }
    catch(Exception e){
      throw new NoCustodianFound(userName);
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
    if ( this.hasPersonGotChildren(parent) ) {
      try {
	Collection coll = getChildrenFor(parent);
	return coll.contains(childToCheck);
      }
      catch (NoChildrenFound ex) {
      }
    }
    return false;
  }

  /**
   * @return True if the parent is the parent of childToCheck else false
   */
  public boolean isParentOf(User parentToCheck,User child) throws RemoteException {
    try {
      Collection coll = getCustodiansFor(child);
      return coll.contains(parentToCheck);
    }
    catch (NoCustodianFound ex) {
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
      personToSet.addRelation(convertUserToGroup(parent),this.RELATION_TYPE_GROUP_CHILD);
      parent.addRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_PARENT);
    }
  }

  public void setAsParentFor(User parent,User child)throws CreateException,RemoteException{
    if(!this.isParentOf(parent,child)){
      child.addRelation(convertUserToGroup(parent),this.RELATION_TYPE_GROUP_CHILD);
      parent.addRelation(convertUserToGroup(child),this.RELATION_TYPE_GROUP_PARENT);
    }
  }

  public void setAsSpouseFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    if(!this.isSpouseOf(personToSet,relatedPerson)){
      personToSet.addRelation(convertUserToGroup(relatedPerson),this.RELATION_TYPE_GROUP_SPOUSE);
      relatedPerson.addRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SPOUSE);
    }
  }

  public void setAsSiblingFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    if(!this.isSiblingOf(personToSet,relatedPerson)){
      personToSet.addRelation(convertUserToGroup(relatedPerson),this.RELATION_TYPE_GROUP_SIBLING);
      relatedPerson.addRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SIBLING);
    }
  }

  public void removeAsChildFor(User personToSet,User parent)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(parent),this.RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsParentFor(User parent,User child)throws RemoveException,RemoteException{
    child.removeRelation(convertUserToGroup(parent),this.RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(convertUserToGroup(child),this.RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsSpouseFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(relatedPerson),this.RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.removeRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SPOUSE);
  }

  public void removeAsSiblingFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(convertUserToGroup(relatedPerson),this.RELATION_TYPE_GROUP_SIBLING);
    relatedPerson.removeRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SIBLING);
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

	public UserBusiness getUserBusiness()throws RemoteException{
		return (UserBusiness)this.getServiceInstance(UserBusiness.class);	
	}

}