package is.idega.idegaweb.campus.block.allocation.data;


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


}