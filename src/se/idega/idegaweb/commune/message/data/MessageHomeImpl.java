package se.idega.idegaweb.commune.message.data;


public class MessageHomeImpl extends com.idega.data.IDOFactory implements MessageHome
{
 protected Class getEntityInterfaceClass(){
  return Message.class;
 }


 public Message create() throws javax.ejb.CreateException{
  return (Message) super.createIDO();
 }


public java.util.Collection findMessages(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Message) super.findByPrimaryKeyIDO(pk);
 }



}