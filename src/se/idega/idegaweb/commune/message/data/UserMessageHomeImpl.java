package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public class UserMessageHomeImpl extends com.idega.data.IDOFactory implements UserMessageHome{
 protected Class getEntityInterfaceClass(){
  return UserMessage.class;
 }


 public Message create() throws javax.ejb.CreateException{
  return (Message) super.createIDO();
 }


public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.User p0, String[] status)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.Group p0, String[] status)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Message) super.findByPrimaryKeyIDO(pk);
 }

 /* (non-Javadoc)
	* @see se.idega.idegaweb.commune.message.data.MessageHome#findMessages(com.idega.user.data.User, java.lang.String[], int, int)
	*/
 public Collection findMessages(User p0, String[] status, int numberOfEntries, int startingEntry) throws FinderException {
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status, numberOfEntries, startingEntry);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }


 /* (non-Javadoc)
	* @see se.idega.idegaweb.commune.message.data.MessageHome#findMessages(com.idega.user.data.Group, java.lang.String[], int, int)
	*/
 public Collection findMessages(Group p0, String[] status, int numberOfEntries, int startingEntry) throws FinderException {
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status, numberOfEntries, startingEntry);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }


/* (non-Javadoc)
 * @see se.idega.idegaweb.commune.message.data.MessageHome#getNumberOfMessages(com.idega.user.data.User, java.lang.String[])
 */
public int getNumberOfMessages(User p0, String[] status) throws IDOException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((UserMessageBMPBean)entity).ejbHomeGetCountCasesByUserAndStatusArray(p0,status);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


/* (non-Javadoc)
 * @see se.idega.idegaweb.commune.message.data.MessageHome#getNumberOfMessages(com.idega.user.data.User, java.util.Collection, java.lang.String[])
 */
public int getNumberOfMessages(User p0, Collection groups, String[] status) throws IDOException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((UserMessageBMPBean)entity).ejbHomeGetCountCasesByUserAndGroupsAndStatusArray(p0,groups,status);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


/* (non-Javadoc)
 * @see se.idega.idegaweb.commune.message.data.MessageHome#findMessages(com.idega.user.data.Group, java.util.Collection, java.lang.String[], int, int)
 */
public Collection findMessages(User p0, Collection groups, String[] status, int numberOfEntries, int startingEntry) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, groups, status, numberOfEntries, startingEntry);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

}