package se.idega.idegaweb.commune.childcare.data;


public class ChildCareApplicationHomeImpl extends com.idega.data.IDOFactory implements ChildCareApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareApplication.class;
 }


 public ChildCareApplication create() throws javax.ejb.CreateException{
  return (ChildCareApplication) super.createIDO();
 }


public ChildCareApplication findActiveApplicationByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindActiveApplicationByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,int p1,java.sql.Date p2,java.sql.Date p3,java.lang.String[] p4,int p5,int p6)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(int p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatusNotRejected(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatusNotRejected(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByUserAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllChildCasesByProvider(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllChildCasesByProvider(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareApplication findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareApplication findApplicationByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareApplication findApplicationByChildAndChoiceNumberInStatus(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberInStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareApplication findApplicationByChildAndChoiceNumberNotInStatus(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberNotInStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareApplication findApplicationByChildAndChoiceNumberWithStatus(int p0,int p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberWithStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findApplicationByChildAndNotInStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareApplication findApplicationByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationByChildAndProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findApplicationsByProviderAndDate(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationsByProviderAndDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareApplication findNewestApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindNewestApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareApplication findOldestApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareApplicationBMPBean)entity).ejbFindOldestApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareApplication) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfActiveApplications(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfActiveApplications(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(int p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(int p0,java.lang.String[] p1,int p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(int p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsForChild(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfApplicationsForChild(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsForChild(int p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfApplicationsForChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPlacedApplications(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetNumberOfPlacedApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPositionInQueue(java.sql.Date p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetPositionInQueue(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPositionInQueue(java.sql.Date p0,int p1,java.lang.String p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetPositionInQueue(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPositionInQueueByDate(int p0,java.sql.Date p1,int p2,java.lang.String p3)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetPositionInQueueByDate(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPositionInQueueByDate(int p0,java.sql.Date p1,int p2,java.lang.String[] p3)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetPositionInQueueByDate(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getQueueSizeByAreaInStatus(int p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetQueueSizeByAreaInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getQueueSizeByAreaNotInStatus(int p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetQueueSizeByAreaNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getQueueSizeInStatus(int p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetQueueSizeInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getQueueSizeNotInStatus(int p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareApplicationBMPBean)entity).ejbHomeGetQueueSizeNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}