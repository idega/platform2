package com.idega.block.email.data;


public class MailTopicHomeImpl extends com.idega.data.IDOFactory implements MailTopicHome
{
 protected Class getEntityInterfaceClass(){
  return MailTopic.class;
 }


 public MailTopic create() throws javax.ejb.CreateException{
  return (MailTopic) super.createIDO();
 }


public MailTopic findOneByListId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((MailTopicBMPBean)entity).ejbFindOneByListId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public MailTopic findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailTopic) super.findByPrimaryKeyIDO(pk);
 }



}