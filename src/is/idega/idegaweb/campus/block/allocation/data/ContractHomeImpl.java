package is.idega.idegaweb.campus.block.allocation.data;

import java.sql.Date;
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


public java.util.Collection findByApartmentAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndRented(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicant(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicant(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantInCreatedStatus(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantInCreatedStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6,int p7,int p8)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindBySearchConditions(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(pk);
 }


public int countBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6)throws com.idega.data.IDOException{
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


	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#ejbFindAll()
	 */
	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByApartmentAndStatus(java.lang.Integer, java.lang.String)
	 */
	public Collection findByApartmentAndStatus(Integer ID, String status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndStatus(ID,status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByApartmentAndUser(java.lang.Integer, java.lang.Integer)
	 */
	public Collection findByApartmentAndUser(Integer AID, Integer UID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentAndUser(AID,UID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByApartmentID(java.lang.Integer)
	 */
	public Collection findByApartmentID(Integer ID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApartmentID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByApplicantAndRented(java.lang.Integer, java.lang.Boolean)
	 */
	public Collection findByApplicantAndRented(Integer ID, Boolean rented) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantAndRented(ID,rented);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByApplicantID(java.lang.Integer)
	 */
	public Collection findByApplicantID(Integer ID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicantID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findBySQL(java.lang.String)
	 */
	public Collection findBySQL(String sql) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByStatus(java.lang.String)
	 */
	public Collection findByStatus(String status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatus(status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByUserAndRented(java.lang.Integer, java.lang.Boolean)
	 */
	public Collection findByUserAndRented(Integer ID, Boolean rented) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserAndRented(ID,rented);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByUserID(java.lang.Integer)
	 */
	public Collection findByUserID(Integer ID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByUserID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByComplexAndBuildingAndApartmentName(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public Collection findByComplexAndBuildingAndApartmentName(Integer complexID, Integer buildingID,
			String apartmentName) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByComplexAndBuildingAndApartmentName(complexID,buildingID,apartmentName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#findByPersonalID(java.lang.String)
	 */
	public Collection findByPersonalID(String ID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByPersonalID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#getUnsignedApplicants(java.lang.String)
	 */
	public Collection getUnsignedApplicants(String personalID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbHomeGetUnsignedApplicants(personalID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractHome#FindByStatusAndBeforeDate(java.lang.String, java.sql.Date)
	 */
	public Collection findByStatusAndBeforeDate(String status, Date date)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByStatusAndBeforeDate(status,date);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}