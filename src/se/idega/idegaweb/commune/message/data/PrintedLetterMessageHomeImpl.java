package se.idega.idegaweb.commune.message.data;

import com.idega.util.IWTimestamp;


public class PrintedLetterMessageHomeImpl extends com.idega.data.IDOFactory implements PrintedLetterMessageHome
{
 protected Class getEntityInterfaceClass(){
  return PrintedLetterMessage.class;
 }


 public Message create() throws javax.ejb.CreateException{
  return (Message) super.createIDO();
 }
 public java.util.Collection findByBulkFile(int file,String letterType, String status) throws  javax.ejb.FinderException {
 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindByBulkFile(file,letterType,status);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }
public java.util.Collection findPrintedDefaultLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllUnPrintedLetters()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindAllUnPrintedLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllPrintedLetters()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindAllPrintedLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedPasswordLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedPasswordLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.User p0, String[] status)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedDefaultLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public java.util.Collection findPrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException{
 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedLettersByType(p0,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSinglePrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException{
 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSinglePrintedLettersByType(p0,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
 public java.util.Collection findUnPrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedLettersByType(p0,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public java.util.Collection findSingleUnPrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSingleUnPrintedLettersByType(p0,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSingleByTypeAndStatus(java.lang.String type,String status,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSingleByTypeAndStatus(type,status,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findLetterByChildcare(int p0, String ssn, String msgid)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindLettersByChildcare(p0, ssn, msgid);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Message) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfPrintedDefaultLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnPrintedDefaultLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPrintedPasswordLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnPrintedPasswordLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
public int getNumberOfLettersByStatusAndType(String caseStatus,String letterType){

	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfLettersByStatusAndType(caseStatus,letterType);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnprintedLettersByType(String letterType){

	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnprintedLettersByType(letterType);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

 public int getNumberOfPrintedLettersByType(String letterType){

	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnprintedLettersByType(letterType);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public String[] getPrintMessageTypes(){

	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	String[] theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetPrintMessageTypes();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}




}