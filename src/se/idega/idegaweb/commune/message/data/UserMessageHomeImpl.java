package se.idega.idegaweb.commune.message.data;


public class UserMessageHomeImpl extends com.idega.data.IDOFactory implements UserMessageHome
{
 protected Class getEntityInterfaceClass(){
  return UserMessage.class;
 }


 public UserMessage create() throws javax.ejb.CreateException{
  return (UserMessage) super.createIDO();
 }


public java.util.Collection findMessages(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserMessageBMPBean)entity).ejbFindMessages(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public UserMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserMessage) super.findByPrimaryKeyIDO(pk);
 }



}