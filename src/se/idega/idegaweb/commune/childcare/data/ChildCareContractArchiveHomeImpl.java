package se.idega.idegaweb.commune.childcare.data;


public class ChildCareContractArchiveHomeImpl extends com.idega.data.IDOFactory implements ChildCareContractArchiveHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareContractArchive.class;
 }


 public ChildCareContractArchive create() throws javax.ejb.CreateException{
  return (ChildCareContractArchive) super.createIDO();
 }


public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByChildAndProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContractArchive findByContractFileID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindByContractFileID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findFutureContractsByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindFutureContractsByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContractArchive findLatestContractByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindLatestContractByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContractArchive findLatestTerminatedContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindLatestTerminatedContractByChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContractArchive findValidContractByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindValidContractByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContractArchive findApplicationByContract(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindApplicationtByContract(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContractArchive findValidContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindValidContractByChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCareContractArchive findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareContractArchive) super.findByPrimaryKeyIDO(pk);
 }


public int getFutureContractsCountByApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractArchiveBMPBean)entity).ejbHomeGetFutureContractsCountByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getContractsCountByApplication(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractArchiveBMPBean)entity).ejbHomeGetContractsCountByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfActiveForApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractArchiveBMPBean)entity).ejbHomeGetNumberOfActiveForApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfActiveNotWithProvider(int p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractArchiveBMPBean)entity).ejbHomeGetNumberOfActiveNotWithProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfTerminatedLaterNotWithProvider(int p0,int p1,java.sql.Date p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractArchiveBMPBean)entity).ejbHomeGetNumberOfTerminatedLaterNotWithProvider(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}