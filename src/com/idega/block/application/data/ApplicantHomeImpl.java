package com.idega.block.application.data;


public class ApplicantHomeImpl extends com.idega.data.IDOFactory implements ApplicantHome
{
 protected Class getEntityInterfaceClass(){
  return Applicant.class;
 }

 public Applicant create() throws javax.ejb.CreateException{
  return (Applicant) super.idoCreate();
 }

 public Applicant createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Applicant findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Applicant) super.idoFindByPrimaryKey(id);
 }

 public Applicant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Applicant) super.idoFindByPrimaryKey(pk);
 }

 public Applicant findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

public java.util.Collection findBySSN(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicantBMPBean)entity).ejbFindBySSN(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


}