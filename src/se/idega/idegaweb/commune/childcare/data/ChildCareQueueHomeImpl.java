package se.idega.idegaweb.commune.childcare.data;


public class ChildCareQueueHomeImpl extends com.idega.data.IDOFactory implements ChildCareQueueHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareQueue.class;
 }


 public ChildCareQueue create() throws javax.ejb.CreateException{
  return (ChildCareQueue) super.createIDO();
 }


public ChildCareQueue findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindApplicationByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findQueueByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareQueue findQueueByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCareQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareQueue) super.findByPrimaryKeyIDO(pk);
 }



}