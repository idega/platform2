package is.idega.idegaweb.campus.data;


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

public ContractAccountApartment findByAccount(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ContractAccountApartmentBMPBean)entity).ejbFindByAccount(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
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