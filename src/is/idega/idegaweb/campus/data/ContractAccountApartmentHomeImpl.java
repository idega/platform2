package is.idega.idegaweb.campus.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public class ContractAccountApartmentHomeImpl extends com.idega.data.IDOFactory implements ContractAccountApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return ContractAccountApartment.class;
 }


 public ContractAccountApartment create() throws javax.ejb.CreateException{
  return (ContractAccountApartment) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByAccount(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByAccount(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ContractAccountApartment findByAccountAndRented(java.lang.Integer p0,boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ContractAccountApartmentBMPBean)entity).ejbFindByAccountAndRented(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByAccountAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartment(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByApartment(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByTypeAndStatusAndOverlapPeriod(java.lang.String p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByTypeAndStatusAndOverlapPeriod(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

/* (non-Javadoc)
 * @see is.idega.idegaweb.campus.data.ContractAccountApartmentHome#findByTypeAndStatusAndOverLapPeriodMultiples(java.lang.String, java.lang.String[], java.sql.Date, java.sql.Date)
 */
public Collection findByTypeAndStatusAndOverLapPeriodMultiples(String type,	String[] status, Date from, Date to) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByTypeAndStatusAndOverLapPeriodMultiples(type,status,from,to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByTypeAndStatusAndOverlapPeriodAndNotInRound(java.lang.String p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3,java.lang.Integer p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountApartmentBMPBean)entity).ejbFindByTypeAndStatusAndOverlapPeriodAndNotInRound(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ContractAccountApartment findByUser(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ContractAccountApartmentBMPBean)entity).ejbFindByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ContractAccountApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractAccountApartment) super.findByPrimaryKeyIDO(pk);
 }



	
}