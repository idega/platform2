package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class ContractHomeImpl extends com.idega.data.IDOFactory implements ContractHome
{
 protected Class getEntityInterfaceClass(){
  return Contract.class;
 }


 public Contract create() throws javax.ejb.CreateException{
  return (Contract) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndRented(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentAndUser(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndUser(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentID(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicant(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicant(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantAndRented(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantID(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantInCreatedStatus(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantInCreatedStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByComplexAndBuildingAndApartmentName(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByComplexAndBuildingAndApartmentName(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPersonalID(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByPersonalID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,int p6,int p7,int p8)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindBySearchConditions(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatus(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndChangeDate(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatusAndChangeDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndOverLapPeriodMultiples(java.lang.String[] p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatusAndOverLapPeriodMultiples(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndValidBeforeDate(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatusAndValidBeforeDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByUserAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserAndRented(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByUserID(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(pk);
 }


public int countBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,int p6)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ContractBMPBean)entity).ejbHomeCountBySearchConditions(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.sql.Date getLastValidFromForApartment(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.sql.Date theReturn = ((ContractBMPBean)entity).ejbHomeGetLastValidFromForApartment(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.sql.Date getLastValidToForApartment(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.sql.Date theReturn = ((ContractBMPBean)entity).ejbHomeGetLastValidToForApartment(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getUnsignedApplicants(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ContractBMPBean)entity).ejbHomeGetUnsignedApplicants(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}




	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByUserAndStatus(java.lang.Integer, java.lang.String)
	 */
	public Collection findByUserAndStatus(Integer userId, String status)throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserAndStatus(userId,status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByUserAndStatus(java.lang.Integer, java.lang.String[])
	 */
	public Collection findByUserAndStatus(Integer userId, String[] status)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserAndStatus(userId,status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}