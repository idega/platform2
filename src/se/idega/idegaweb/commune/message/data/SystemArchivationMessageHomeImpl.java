package se.idega.idegaweb.commune.message.data;


public class SystemArchivationMessageHomeImpl extends com.idega.data.IDOFactory implements SystemArchivationMessageHome
{
 protected Class getEntityInterfaceClass(){
  return SystemArchivationMessage.class;
 }


 public SystemArchivationMessage create() throws javax.ejb.CreateException{
  return (SystemArchivationMessage) super.createIDO();
 }


public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindPrintedMessages(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SystemArchivationMessageBMPBean)entity).ejbFindUnPrintedMessages(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SystemArchivationMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SystemArchivationMessage) super.findByPrimaryKeyIDO(pk);
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


}