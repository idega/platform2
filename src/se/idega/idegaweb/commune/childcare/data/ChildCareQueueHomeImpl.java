package se.idega.idegaweb.commune.childcare.data;


public class ChildCareQueueHomeImpl extends com.idega.data.IDOFactory implements ChildCareQueueHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareQueue.class;
 }


 public ChildCareQueue create() throws javax.ejb.CreateException{
  return (ChildCareQueue) super.createIDO();
 }


public java.util.Collection findQueueByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareQueue findQueueByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareQueue findQueueByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareQueue findQueueByChildChoiceNumberAndQueueType(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChildChoiceNumberAndQueueType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findQueueByProviderAndDate(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareQueueBMPBean)entity).ejbFindQueueByProviderAndDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ChildCareQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareQueue) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberInQueue(int p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareQueueBMPBean)entity).ejbHomeGetNumberInQueue(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfNotExported(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareQueueBMPBean)entity).ejbHomeGetNumberOfNotExported(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotalCount(java.lang.String[] p0,boolean p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareQueueBMPBean)entity).ejbHomeGetTotalCount(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}