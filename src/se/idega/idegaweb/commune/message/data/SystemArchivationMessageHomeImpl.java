package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.user.data.Group;
import com.idega.user.data.User;


public class SystemArchivationMessageHomeImpl extends com.idega.data.IDOFactory implements SystemArchivationMessageHome
{
 protected Class getEntityInterfaceClass(){
  return SystemArchivationMessage.class;
 }


 public Message create() throws javax.ejb.CreateException{
  return (Message) super.createIDO();
 }


public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindMessages(p0);
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

public java.util.Collection findPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindPrintedMessages(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
 
public java.util.Collection findUnPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindUnPrintedMessages(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedMessages()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindPrintedMessages();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedMessages()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindUnPrintedMessages();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Message) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfUnPrintedMessages(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SystemArchivationMessageBMPBean)entity).ejbHomeGetNumberOfUnPrintedMessages();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String[] getPrintMessageTypes(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String[] theReturn = ((SystemArchivationMessageBMPBean)entity).ejbHomeGetPrintMessageTypes();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
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
public int getNumberOfMessages(User p0, String[] status) throws com.idega.data.IDOException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((UserMessageBMPBean)entity).ejbHomeGetCountCasesByUserAndStatusArray(p0,status);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

/* (non-Javadoc)
 * @see se.idega.idegaweb.commune.message.data.MessageHome#getNumberOfMessages(com.idega.user.data.User, java.util.Collection, java.lang.String[])
 */
public int getNumberOfMessages(User p0, Collection groups, String[] status) throws com.idega.data.IDOException {
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