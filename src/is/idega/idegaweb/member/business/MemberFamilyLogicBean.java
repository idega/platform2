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

public class MemberFamilyLogicBean extends IBOServiceBean {

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
   * @throws NoSpouseFound if no NoChildrenFound is found
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


  public boolean isChildOf(User childToCheck,User parent){
    throw new java.lang.UnsupportedOperationException("Not implemented yet");
  }

  public boolean isSpouseOf(User personToCheck,User relatedPerson){
    throw new java.lang.UnsupportedOperationException("Not implemented yet");
  }

  public void setAsChildFor(User personToSet,User parent)throws CreateException,RemoteException{
    personToSet.addRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.addRelation(personToSet,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void setAsSpouseFor(User personToSet,User relatedPerson)throws CreateException,RemoteException{
    personToSet.addRelation(relatedPerson,this.RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.addRelation(personToSet,this.RELATION_TYPE_GROUP_SPOUSE);
  }

  public void removeAsChildFor(User personToSet,User parent)throws RemoveException,RemoteException{
    personToSet.removeRelation(parent,this.RELATION_TYPE_GROUP_CHILD);
    parent.removeRelation(personToSet,this.RELATION_TYPE_GROUP_PARENT);
  }

  public void removeAsSpouseFor(User personToSet,User relatedPerson)throws RemoveException,RemoteException{
    personToSet.removeRelation(relatedPerson,this.RELATION_TYPE_GROUP_SPOUSE);
    relatedPerson.removeRelation(personToSet,this.RELATION_TYPE_GROUP_SPOUSE);
  }


}