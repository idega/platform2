package com.idega.block.school.data;


public class SchoolTypeHomeImpl extends com.idega.data.IDOFactory implements SchoolTypeHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolType.class;
 }


 public SchoolType create() throws javax.ejb.CreateException{
  return (SchoolType) super.createIDO();
 }


 public SchoolType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllSchoolTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolType) super.findByPrimaryKeyIDO(pk);
 }


 public SchoolType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (SchoolType) super.findByPrimaryKeyIDO(id);
 }


 public SchoolType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}