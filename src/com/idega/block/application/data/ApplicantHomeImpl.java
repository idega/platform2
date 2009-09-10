package com.idega.block.application.data;


public class ApplicantHomeImpl extends com.idega.data.IDOFactory implements ApplicantHome
{
 protected Class getEntityInterfaceClass(){
  return Applicant.class;
 }


 public Applicant create() throws javax.ejb.CreateException{
  return (Applicant) super.createIDO();
 }


public java.util.Collection findByApplicationStatusOrderedBy(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicantBMPBean)entity).ejbFindByApplicationStatusOrderedBy(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicantBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySSN(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicantBMPBean)entity).ejbFindBySSN(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Applicant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Applicant) super.findByPrimaryKeyIDO(pk);
 }



}