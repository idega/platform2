package is.idega.idegaweb.member.business;

import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;

import com.idega.business.IBOServiceBean;
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


  /**
   * @returns A Collection of User object who are children of a user. Returns an empty Collection if no children found.
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
      return coll;
    }
    catch(FinderException e){
      throw new NoChildrenFound(userName);
    }
  }

  /**
   * @returns User object for the spouse of a user.
   * @throws NoSpouseFound if no spouse is found
   */
  public User getSpouseFor(User user)throws NoSpouseFound{
    String userName = null;
    try{
      userName = user.getName();
      Collection coll = user.getRelatedBy(this.RELATION_TYPE_GROUP_SPOUSE);
      return (User)coll.iterator().next();
    }
    catch(Exception e){
      throw new NoSpouseFound(userName);
    }
  }

  /**
   * @returns A Collection of User object who are custodians of a user. Returns an empty Collection if no custodians are found.
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
      return coll;
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
   * @returns True if the parent is the parent of childToCheck else false
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
   * @returns True if the personToCheck is a spouse of relatedPerson else false
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

  public void setAsChildFor(User personToSet,User parent)throws CreateException,RemoteException{
    personToSet.addRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.addRelation(personToSet,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void setAsParentFor(User parent,User child)throws CreateException,RemoteException{
    child.addRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.addRelation(child,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void setAsSpouseFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    personToSet.addRelation(relatedPerson,this.RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.addRelation(personToSet,this.RELATION_TYPE_GROUP_SPOUSE);
  }

  public void removeAsChildFor(User personToSet,User parent)throws RemoveException,RemoteException{
    personToSet.removeRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(personToSet,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsParentFor(User parent,User child)throws RemoveException,RemoteException{
    child.removeRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(child,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsSpouseFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(relatedPerson,this.RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.removeRelation(personToSet,this.RELATION_TYPE_GROUP_SPOUSE);
  }


}