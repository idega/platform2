package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public class ChildCareContractHomeImpl extends com.idega.data.IDOFactory implements ChildCareContractHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareContract.class;
 }


 public ChildCareContract create() throws javax.ejb.CreateException{
  return (ChildCareContract) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContract findApplicationByContract(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindApplicationByContract(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndDateRange(com.idega.user.data.User p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByChildAndDateRange(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByChildAndProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContract findByContractFileID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindByContractFileID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByDateRange(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByDateRange(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByDateRangeAndProviderWhereStatusActive(java.sql.Date p0,java.sql.Date p1,com.idega.block.school.data.School p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByDateRangeAndProviderWhereStatusActive(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByDateRangeWhereStatusActive(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByDateRangeWhereStatusActive(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContract findBySchoolClassMember(com.idega.block.school.data.SchoolClassMember p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindBySchoolClassMember(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findContractByChildAndPeriod(com.idega.user.data.User p0,com.idega.util.TimePeriod p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindContractByChildAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findFirstContractByApplication(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindFirstContractByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findFutureContractsByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindFutureContractsByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContract findLatestContractByApplication(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindLatestContractByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findLatestContractByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindLatestContractByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findLatestTerminatedContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindLatestTerminatedContractByChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findValidContractByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindValidContractByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ChildCareContract findValidContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractBMPBean)entity).ejbFindValidContractByChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findValidContractByProvider(int p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindValidContractByProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ChildCareContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareContract) super.findByPrimaryKeyIDO(pk);
 }


public int getContractsCountByApplication(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetContractsCountByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getContractsCountByDateRangeAndProvider(java.sql.Date p0,java.sql.Date p1,int p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetContractsCountByDateRangeAndProvider(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getFutureContractsCountByApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetFutureContractsCountByApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfActiveForApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetNumberOfActiveForApplication(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfActiveNotWithProvider(int p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetNumberOfActiveNotWithProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfTerminatedLaterNotWithProvider(int p0,int p1,java.sql.Date p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ChildCareContractBMPBean)entity).ejbHomeGetNumberOfTerminatedLaterNotWithProvider(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractHome#findByInvoiceReceiver(java.lang.Integer)
	 */
	public Collection findByInvoiceReceiver(Integer invoiceReceiverID)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByInvoiceReceiver(invoiceReceiverID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);

	}
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.childcare.data.ChildCareContractHome#findByInvoiceReceiverActiveOrFuture(java.lang.Integer, java.sql.Date)
	 */
	public Collection findByInvoiceReceiverActiveOrFuture(	Integer invoiceReceiverID, Date fromDate) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ChildCareContractBMPBean)entity).ejbFindByInvoiceReceiverActiveOrFuture(invoiceReceiverID,fromDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}