package se.idega.idegaweb.commune.message.data;


public class SystemArchivationMessageHomeImpl extends com.idega.data.IDOFactory implements SystemArchivationMessageHome
{
 protected Class getEntityInterfaceClass(){
  return PrintedLetterMessage.class;
 }


 public Message create() throws javax.ejb.CreateException{
  return (PrintedLetterMessage) super.createIDO();
 }


public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PrintedLetterMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PrintedLetterMessage) super.findByPrimaryKeyIDO(pk);
 }



}