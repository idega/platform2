package se.idega.idegaweb.commune.childcare.data;


public class AfterSchoolChoiceHomeImpl extends com.idega.data.IDOFactory implements AfterSchoolChoiceHome
{
 protected Class getEntityInterfaceClass(){
  return AfterSchoolChoice.class;
 }


 public AfterSchoolChoice create() throws javax.ejb.CreateException{
  return (AfterSchoolChoice) super.createIDO();
 }


public AfterSchoolChoice findActiveApplicationByChild(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindActiveApplicationByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findActiveApplicationByChildAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindActiveApplicationByChildAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndNotInStatus(java.lang.Integer p0,int p1,java.sql.Date p2,java.sql.Date p3,java.lang.String[] p4,int p5,int p6)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndNotInStatus(java.lang.Integer p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(java.lang.Integer p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(java.lang.Integer p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatusNotRejected(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderStatusNotRejected(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByUserAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllChildCasesByProvider(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllChildCasesByProvider(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationByChild(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public AfterSchoolChoice findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findApplicationByChildAndChoiceNumber(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findApplicationByChildAndChoiceNumberInStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberInStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findApplicationByChildAndChoiceNumberNotInStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberNotInStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findApplicationByChildAndChoiceNumberWithStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndChoiceNumberWithStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findApplicationByChildAndNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public AfterSchoolChoice findApplicationByChildAndProvider(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findApplicationByChildAndProviderAndStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationByChildAndProviderAndStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findApplicationsByProvider(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationsByProvider(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindApplicationsByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndChoiceNumberAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByChildAndSeason(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AfterSchoolChoice) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfActiveApplications(java.lang.Integer p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfActiveApplications(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.Integer p0,java.lang.String[] p1,int p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsByProviderAndChoiceNumber(java.lang.Integer p0,java.lang.Integer p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplicationsByProviderAndChoiceNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsForChild(java.lang.Integer p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplicationsForChild(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsForChild(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplicationsForChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplicationsForChildNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplicationsForChildNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfPlacedApplications(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AfterSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfPlacedApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}