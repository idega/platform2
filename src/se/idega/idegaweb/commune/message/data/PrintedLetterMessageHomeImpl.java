package se.idega.idegaweb.commune.message.data;


public class PrintedLetterMessageHomeImpl extends com.idega.data.IDOFactory implements PrintedLetterMessageHome
{
 protected Class getEntityInterfaceClass(){
  return PrintedLetterMessage.class;
 }


 public Message create() throws javax.ejb.CreateException{
	return (Message) super.createIDO();
 }



 public java.util.Collection findMessages(com.idega.user.data.Group p0, String[] status)throws javax.ejb.FinderException{
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }

public java.util.Collection findAllLettersBySchool(int p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindAllLettersBySchool(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllPrintedLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindAllPrintedLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllUnPrintedLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindAllUnPrintedLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByBulkFile(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindByBulkFile(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findLetters(java.lang.String[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindLetters(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findLettersByChildcare(int p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindLettersByChildcare(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessages(com.idega.user.data.User p0, String[] status)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessagesByStatus(p0, status);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findMessagesByStatus(com.idega.user.data.User p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindMessagesByStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedDefaultLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedLettersByType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedPasswordLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSingleByTypeAndStatus(java.lang.String p0,java.lang.String p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSingleByTypeAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSinglePrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSinglePrintedLettersByType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSingleUnPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindSingleUnPrintedLettersByType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedDefaultLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedLettersByType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedPasswordLetters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindUnPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
 return (Message) super.findByPrimaryKeyIDO(pk);
}


public java.lang.String[] getLetterTypes(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String[] theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetLetterTypes();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfLettersByStatusAndType(java.lang.String p0,java.lang.String p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfLettersByStatusAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPrintedDefaultLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPrintedLettersByType(java.lang.String p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfPrintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPrintedPasswordLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnPrintedDefaultLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnPrintedDefaultLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnPrintedPasswordLetters(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnPrintedPasswordLetters();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnprintedLettersByType(java.lang.String p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetNumberOfUnprintedLettersByType(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String[] getPrintMessageTypes(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String[] theReturn = ((PrintedLetterMessageBMPBean)entity).ejbHomeGetPrintMessageTypes();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}