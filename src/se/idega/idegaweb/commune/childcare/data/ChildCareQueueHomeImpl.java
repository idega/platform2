package se.idega.idegaweb.commune.childcare.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


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
//Malin
public ChildCareQueue findQueueByChildAndChoiceNumberAndProviderID(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareQueueBMPBean)entity).ejbFindQueueByChildAndChoiceNumberAndProviderID(p0,p1,p2);
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

public Collection getDistinctNotExportedChildIds() {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = null;
	Collection col = null;
	try {
		ids = ((ChildCareQueueBMPBean) entity).ejbHomeGetDistinctNotExportedChildIds();
		col = this.getEntityCollectionForPrimaryKeys(ids);
	}
	catch (javax.ejb.FinderException e) {
		this.idoCheckInPooledEntity(entity);
		return null;
	}
	this.idoCheckInPooledEntity(entity);
		
	HashMap childIds = new HashMap();
	if (col != null) {
		System.out.println("Found "+col.size()+" old queue positions.");
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			ChildCareQueue q = (ChildCareQueue) iter.next();
			childIds.put(new Integer(q.getChildId()),null);
		}
	}
	System.out.println("Placed "+childIds.size()+" children in map.");
		
	Set keys = childIds.keySet();
	if (keys != null)
		return childIds.keySet();
	else
		return null;
}
}