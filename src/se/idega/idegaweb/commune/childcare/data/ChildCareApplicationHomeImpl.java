package se.idega.idegaweb.commune.childcare.data;


public class ChildCareApplicationHomeImpl extends com.idega.data.IDOFactory implements ChildCareApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareApplication.class;
 }


 public ChildCareApplication create() throws javax.ejb.CreateException{
  return (ChildCareApplication) super.createIDO();
 }


public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderStatusNotRejected(int p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderStatusNotRejected(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareApplicationBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareApplication) super.findByPrimaryKeyIDO(pk);
 }



}